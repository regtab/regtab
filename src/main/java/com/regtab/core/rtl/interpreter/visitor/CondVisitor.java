package com.regtab.core.rtl.interpreter.visitor;

import com.regtab.core.model.semantics.Condition;
import com.regtab.core.model.semantics.Constraint;
import com.regtab.core.model.semantics.Expr;

import java.util.List;

import com.regtab.core.rtl.parser.RTLBaseVisitor;
import com.regtab.core.rtl.parser.RTLParser.*;

final class CondVisitor extends RTLBaseVisitor<Condition> {

    private static ExprVisitor exprVisitor = new ExprVisitor();

    @Override
    public Condition visitCond(CondContext ctx) {
        final Condition cond = new Condition();
        final List<ExprContext> exprCtxList = ctx.expr();

        for (ExprContext exprCtx : exprCtxList) {
            final Expr expr = exprVisitor.visit(exprCtx);

            if (expr == null)
                return null; // Impossible

            final Constraint constr = new Constraint(expr);
            cond.addConstraint(constr);
        }

        return cond;
    }
}
