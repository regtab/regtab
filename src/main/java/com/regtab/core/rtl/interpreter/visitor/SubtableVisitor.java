package com.regtab.core.rtl.interpreter.visitor;

import com.regtab.core.model.semantics.Action;
import com.regtab.core.model.semantics.Condition;
import com.regtab.core.rtl.interpreter.pattern.RowPattern;
import com.regtab.core.rtl.interpreter.pattern.SubtablePattern;
import com.regtab.core.rtl.parser.RTLBaseVisitor;
import com.regtab.core.rtl.parser.RTLParser.*;

import java.util.List;

final class SubtableVisitor extends RTLBaseVisitor<SubtablePattern> {
    private static final RowVisitor rowVisitor = new RowVisitor();
    private static final CondVisitor condVisitor = new CondVisitor();
    private static final QuantifierVisitor quantifierVisitor = new QuantifierVisitor();
    private static final ActionVisitor actionVisitor = new ActionVisitor();

    @Override
    public SubtablePattern visitSubtable(SubtableContext ctx) {
        final SubtablePattern subtablePattern = new SubtablePattern(ctx);

        final List<RowContext> rowContexts = ctx.row();
        if (rowContexts != null && !rowContexts.isEmpty()) {
            final boolean result = apply(rowContexts, subtablePattern);
            if (!result)
                return null; // Impossible

            return subtablePattern;
        }

        final RowsContext rowsContext = ctx.rows();
        if (rowsContext != null) {
            final boolean result = apply(rowsContext, subtablePattern);
            if (!result)
                return null; // Impossible

            return subtablePattern;
        }

        return null; // Impossible
    }

    private boolean apply(List<RowContext> rowContexts, SubtablePattern pattern) {
        for (RowContext ctx : rowContexts) {
            RowPattern rowPattern = rowVisitor.visit(ctx);
            if (rowPattern == null)
                return false; // Impossible

            pattern.add(rowPattern);
        }

        final Quantifier quantifier = new Quantifier(Quantifier.Times.UNDEFINED, null);
        pattern.setQuantifier(quantifier);

        return true;
    }

    private boolean apply(RowsContext rowsContext, SubtablePattern pattern) {
        pattern.rowsContext = rowsContext;

        // Instruction order matter
        final List<RowContext> rowContexts = rowsContext.row();
        for (RowContext ctx : rowContexts) {
            RowPattern rowPattern = rowVisitor.visit(ctx);
            if (rowPattern == null)
                return false; // Impossible
            pattern.add(rowPattern);
        }

        final CondContext condContext = rowsContext.cond();
        if (condContext != null) {
            final Condition condition = condVisitor.visit(condContext);
            if (condition == null)
                return false; // Impossible

            pattern.setCondition(condition);
        }

        final Quantifier quantifier;
        final QuantifierContext quantifierContext = rowsContext.quantifier();
        if (quantifierContext == null) {
            quantifier = new Quantifier(Quantifier.Times.UNDEFINED, null);
        } else {
            quantifier = quantifierVisitor.visit(quantifierContext);
            if (quantifier == null)
                return false; // Impossible
        }
        pattern.setQuantifier(quantifier);

        final ActionsContext actionsCtx = rowsContext.actions();
        if (actionsCtx != null) {
            final List<ActionContext> actionCtxList = actionsCtx.action();
            if (actionCtxList != null) {
                for (ActionContext actionCtx : actionCtxList) {
                    Action action = actionVisitor.visit(actionCtx);
                    if (action == null)
                        return false; // Impossible
                    pattern.add(action);
                }
            }
        }

        return true;
    }
}
