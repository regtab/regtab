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
        Condition cond = new Condition();
        List<ConstrContext> constrCtxList = ctx.constr();

        for (ConstrContext constrCtx : constrCtxList) {
            ExprContext exprCtx = constrCtx.expr();
            Expr expr = exprVisitor.visit(exprCtx);

            if (expr == null)
                return null; // Impossible

            Constraint constr = new Constraint(expr);
            cond.addConstraint(constr);
        }

        return cond;
    }
}
