package com.regtab.core.rtl.interpreter.visitor;

import com.regtab.core.model.Action;
import com.regtab.core.model.Condition;
import com.regtab.core.rtl.interpreter.pattern.RowPattern;
import com.regtab.core.rtl.interpreter.pattern.SubtablePattern;

import java.util.List;

import com.regtab.core.rtl.parser.RTLBaseVisitor;
import com.regtab.core.rtl.parser.RTLParser.*;

final class SubtableVisitor extends RTLBaseVisitor<SubtablePattern> {
    private static final RowVisitor rowVisitor = new RowVisitor();
    private static final CondVisitor condVisitor = new CondVisitor();
    private static final QuantifierVisitor quantifierVisitor = new QuantifierVisitor();
    private static final ActionVisitor actionVisitor = new ActionVisitor();

    @Override
    public SubtablePattern visitSubtable(SubtableContext ctx) {
        final SubtablePattern subtableTemplate = new SubtablePattern(ctx);

        final List<RowContext> rowContexts = ctx.row();
        if (rowContexts != null && !rowContexts.isEmpty()) {
            boolean result = apply(rowContexts, subtableTemplate);
            if (!result)
                return null; // TODO test

            return subtableTemplate;
        }

        final RowsContext rowsContext = ctx.rows();
        if (rowsContext != null) {
            boolean result = apply(rowsContext, subtableTemplate);
            if (!result)
                return null; // TODO test

            return subtableTemplate;
        }

        return null; // Impossible
    }

    private boolean apply(List<RowContext> rowContexts, SubtablePattern tmpl) {
        for (RowContext ctx : rowContexts) {
            RowPattern rowTemplate = rowVisitor.visit(ctx);
            if (rowTemplate == null)
                return false; // TODO log

            tmpl.add(rowTemplate);
        }

        //tmpl.quantifier = new Quantifier(EXACTLY, 1);
        final Quantifier quantifier = new Quantifier(Quantifier.Times.EXACTLY, 1);
        tmpl.setQuantifier(quantifier);

        return true;
    }

    private boolean apply(RowsContext rowsContext, SubtablePattern tmpl) {
        tmpl.rowsContext = rowsContext;

        // Instruction order matter
        List<RowContext> rowContexts = rowsContext.row();
        for (RowContext ctx : rowContexts) {
            RowPattern rowTemplate = rowVisitor.visit(ctx);
            if (rowTemplate == null)
                return false; // TODO log
            tmpl.add(rowTemplate);
        }

        final CondContext condContext = rowsContext.cond();
        if (condContext != null) {
            final Condition condition = condVisitor.visit(condContext);
            if (condition == null)
                return false; // TODO log

            tmpl.setCondition(condition);
        }

        final Quantifier quantifier;
        final QuantifierContext quantifierContext = rowsContext.quantifier();
        if (quantifierContext == null) {
            quantifier = new Quantifier(Quantifier.Times.EXACTLY, 1);
        } else {
            quantifier = quantifierVisitor.visit(quantifierContext);
            if (quantifier == null)
                return false; // TODO log
        }
        tmpl.setQuantifier(quantifier);

        final ActionsContext actionsCtx = rowsContext.actions();
        if (actionsCtx != null) {
            List<ActionContext> actionCtxList = actionsCtx.action();
            if (actionCtxList != null) {
                for (ActionContext actionCtx : actionCtxList) {
                    Action action = actionVisitor.visit(actionCtx);
                    if (action == null)
                        return false; // TODO test
                    tmpl.add(action);
                }
            }
        }

        return true;
    }
}
