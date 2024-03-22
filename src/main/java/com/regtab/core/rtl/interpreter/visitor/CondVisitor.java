package com.regtab.core.rtl.interpreter.visitor;

import com.regtab.core.model.Condition;
import com.regtab.core.model.Constraint;
import com.regtab.core.model.Expr;
import com.regtab.core.rtl.parser.TTLBaseVisitor;
import com.regtab.core.rtl.parser.TTLParser.CondContext;
import com.regtab.core.rtl.parser.TTLParser.ConstrContext;
import com.regtab.core.rtl.parser.TTLParser.ExprContext;

import java.util.List;

final class CondVisitor extends TTLBaseVisitor<Condition>  {

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
