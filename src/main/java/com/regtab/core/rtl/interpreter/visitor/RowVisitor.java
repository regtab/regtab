package com.regtab.core.rtl.interpreter.visitor;

import com.regtab.core.model.Action;
import com.regtab.core.model.Condition;
import com.regtab.core.rtl.interpreter.pattern.RowPattern;
import com.regtab.core.rtl.interpreter.pattern.SubrowPattern;
import lombok.extern.java.Log;
import com.regtab.core.rtl.parser.TTLBaseVisitor;
import com.regtab.core.rtl.parser.TTLParser.*;

import java.util.HashMap;
import java.util.List;

@Log
final class RowVisitor extends TTLBaseVisitor<RowPattern> {
    private static final SubrowVisitor subrowVisitor = new SubrowVisitor();
    private static final CondVisitor condVisitor = new CondVisitor();
    private static final QuantifierVisitor quantifierVisitor = new QuantifierVisitor();
    private static final ActionVisitor actionVisitor = new ActionVisitor();

    private static final HashMap<String, SubrowsContext> store = new HashMap<>();

    @Override
    public RowPattern visitRow(RowContext ctx) {
        final RowPattern rowTemplate = new RowPattern(ctx);

        final SubrowsContext subrowsContext;
        final ReplacementContext replacementContext = ctx.replacement();
        if (replacementContext != null) {
            String label = replacementContext.TAG().getText();
            subrowsContext = store.get(label);
            if (subrowsContext == null) {
                final String message = String.format("No a such label [%s]", label);
                log.warning(message);
                return null;
            }
        } else {
            subrowsContext = ctx.subrows();
        }

        final LabelContext labelContext = ctx.label();
        if (labelContext != null) {
            String label = labelContext.TAG().getText();
            store.put(label, subrowsContext);
        }

        rowTemplate.subrowsContext = subrowsContext;

        // Instruction order matter
        final List<SubrowContext> subrowContexts = subrowsContext.subrow();
        for (SubrowContext subrowContext : subrowContexts) {
            final SubrowPattern subrowTemplate = subrowVisitor.visit(subrowContext);
           rowTemplate.add(subrowTemplate);
        }

        final CondContext condContext = subrowsContext.cond();
        if (condContext != null) {
            final Condition condition = condVisitor.visit(condContext);
            rowTemplate.setCondition(condition);
        }

        final QuantifierContext quantifierContext = ctx.quantifier();
        final Quantifier quantifier;
        if (quantifierContext != null)
            quantifier = quantifierVisitor.visit(quantifierContext);
        else
            quantifier = new Quantifier(Quantifier.Times.EXACTLY, 1);

        rowTemplate.setQuantifier(quantifier);

        final ActionsContext actionsCtx = subrowsContext.actions();
        if (actionsCtx != null) {
            List<ActionContext> actionCtxList = actionsCtx.action();
            if (actionCtxList != null) {
                for (ActionContext actionCtx : actionCtxList) {
                    Action action = actionVisitor.visit(actionCtx);
                    if (action == null)
                        return null; // TODO test
                    rowTemplate.add(action);
                }
            }
        }

        return rowTemplate;
    }

}
