package com.regtab.core.rtl.interpreter.visitor;

import com.regtab.core.model.semantics.Action;
import com.regtab.core.model.semantics.Condition;
import com.regtab.core.rtl.Configurator;
import com.regtab.core.rtl.interpreter.pattern.CellPattern;
import com.regtab.core.rtl.interpreter.pattern.SubrowPattern;

import java.util.List;

import com.regtab.core.rtl.parser.RTLBaseVisitor;
import com.regtab.core.rtl.parser.RTLParser.*;

final class SubrowVisitor extends RTLBaseVisitor<SubrowPattern> {
    private static final CellVisitor cellVisitor = new CellVisitor();
    private static final CondVisitor condVisitor = new CondVisitor();
    private static final QuantifierVisitor quantifierVisitor = new QuantifierVisitor();
    private static final ActionVisitor actionVisitor = new ActionVisitor();

    @Override
    public SubrowPattern visitSubrow(SubrowContext ctx) {
        final SubrowPattern subrowPattern = new SubrowPattern(ctx);

        final List<CellContext> cellContexts = ctx.cell();
        if (cellContexts != null && !cellContexts.isEmpty()) {
            boolean result = apply(cellContexts, subrowPattern);
            if (!result)
                return null; // Impossible

            return subrowPattern;
        }

        final CellsContext cellsContext = ctx.cells();
        if (cellsContext != null) {
            boolean result = apply(cellsContext, subrowPattern);
            if (!result)
                return null; // Impossible

            return subrowPattern;
        }

        return null; // Impossible
    }

    private boolean apply(List<CellContext> cellContexts, SubrowPattern pattern) {
        for (CellContext ctx : cellContexts) {
            CellPattern cellPattern = cellVisitor.visit(ctx);
            if (cellPattern == null)
                return false; // Impossible

            pattern.add(cellPattern);
        }

        final Quantifier quantifier = new Quantifier(Quantifier.Times.UNDEFINED, null);
        pattern.setQuantifier(quantifier);

        return true;
    }

    private boolean apply(CellsContext cellsContext, SubrowPattern pattern) {
        pattern.cellsContext = cellsContext;

        // Instruction order matter
        final List<CellContext> cellContexts = cellsContext.cell();
        for (CellContext ctx : cellContexts) {
            CellPattern cellPattern = cellVisitor.visit(ctx);
            if (cellPattern == null)
                return false; // Impossible

            pattern.add(cellPattern);
        }

        final CondContext condContext = cellsContext.cond();
        if (condContext != null) {
            final Condition condition = condVisitor.visit(condContext);
            if (condition == null)
                return false; // Impossible

            pattern.setCondition(condition);
        }

        final Quantifier quantifier;
        final QuantifierContext quantifierContext = cellsContext.quantifier();
        if (quantifierContext == null) {
            quantifier = new Quantifier(Quantifier.Times.UNDEFINED, null);
        } else {
            quantifier = quantifierVisitor.visit(quantifierContext);
            if (quantifier == null)
                return false; // Impossible
        }
        pattern.setQuantifier(quantifier);

        final ActionsContext actionsCtx = cellsContext.actions();
        if (actionsCtx != null) {
            List<ActionContext> actionCtxList = actionsCtx.action();
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
