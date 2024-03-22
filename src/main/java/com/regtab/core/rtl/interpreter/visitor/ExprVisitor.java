package com.regtab.core.rtl.interpreter.visitor;

import com.regtab.core.model.semantics.Expr;
import com.regtab.core.model.semantics.Func;
import com.regtab.core.model.semantics.Prop;

import java.util.List;

import com.regtab.core.rtl.parser.RTLBaseVisitor;
import com.regtab.core.rtl.parser.RTLParser.*;

final class ExprVisitor extends RTLBaseVisitor<Expr> {
    @Override
    public Expr visitIntLiteral(IntLiteralContext ctx) {
        String str = ctx.INT().getText();
        Integer i = Integer.valueOf(str);
        return Expr.builder().integer(i).build();
    }

    @Override
    public Expr visitProp(PropContext ctx) {
        String id = ctx.getText();
        Prop prop = Prop.get(id);
        return Expr.builder().prop(prop).build();
    }

    @Override
    public Expr visitPropExpr(PropExprContext ctx) {
        return super.visit(ctx.prop());
    }

    @Override
    public Expr visitFunc(FuncContext ctx) {
        String id = ctx.ID().getText();
        Func func = Func.get(id);

        List<ArgContext> argCtxList = ctx.arg();
        if (argCtxList != null) {
            for (ArgContext argCtx : argCtxList) {
                if (argCtx.INT() != null) {
                    String str = argCtx.INT().getText();
                    Integer integer = Integer.valueOf(str);
                    func.addArg(integer);
                } else if (argCtx.STRING() != null) {
                    String str = argCtx.INT().getText();
                    func.addArg(str);
                } else if (argCtx.prop() != null) {
                    id = argCtx.prop().ID().getText();
                    Prop prop = Prop.get(id);
                    func.addArg(prop);
                }
                // TODO реализовать поддержку аргументов func
            }
        }

        return Expr.builder().func(func).build();
    }

    @Override
    public Expr visitFuncExpr(FuncExprContext ctx) {
        return super.visit(ctx.func());
    }

    @Override
    public Expr visitNotExpr(NotExprContext ctx) {
        Expr boolExpr = this.visit(ctx.expr());
        return Expr.builder().not(true).expr(boolExpr).build();
    }

    @Override
    public Expr visitParenExpr(ParenExprContext ctx) {
        return super.visit(ctx.expr());
    }

    @Override
    public Expr visitCompExpr(CompExprContext ctx) {
        Expr left = this.visit(ctx.leftExpr);
        Expr right = this.visit(ctx.rightExpr);

        if (ctx.op.EQ() != null)
            return Expr.builder().compOperator(Expr.CompOperator.EQUAL).left(left).right(right).build();

        if (ctx.op.NEQ() != null)
            return Expr.builder().compOperator(Expr.CompOperator.NOT_EQUAL).left(left).right(right).build();

        if (ctx.op.GT() != null)
            return Expr.builder().compOperator(Expr.CompOperator.GREATER).left(left).right(right).build();

        if (ctx.op.GE() != null)
            return Expr.builder().compOperator(Expr.CompOperator.GREATER_OR_EQUAL).left(left).right(right).build();

        if (ctx.op.LT() != null)
            return Expr.builder().compOperator(Expr.CompOperator.LESS).left(left).right(right).build();

        if (ctx.op.LE() != null)
            return Expr.builder().compOperator(Expr.CompOperator.LESS_OR_EQUAL).left(left).right(right).build();

        if (ctx.op.CONTAINS() != null)
            return Expr.builder().compOperator(Expr.CompOperator.CONTAINS).left(left).right(right).build();

        if (ctx.op.MATCHES() != null)
            return Expr.builder().compOperator(Expr.CompOperator.MATCHES).left(left).right(right).build();

        return null; // Impossible
    }

    @Override
    public Expr visitBinaryExpr(BinaryExprContext ctx) {
        Expr left = this.visit(ctx.leftExpr);
        Expr right = this.visit(ctx.rightExpr);

        if (ctx.op.AND() != null)
            return Expr.builder().binaryOperator(Expr.BinaryOperator.AND).left(left).right(right).build();

        if (ctx.op.OR() != null)
            return Expr.builder().binaryOperator(Expr.BinaryOperator.OR).left(left).right(right).build();

        return null; // Impossible
    }

    @Override
    public Expr visitArithmExpr(ArithmExprContext ctx) {
        Expr left = this.visit(ctx.leftExpr);
        Expr right = this.visit(ctx.rightExpr);

        if (ctx.op.PLUS() != null)
            return Expr.builder().arithmOperator(Expr.ArithmOperator.SUM).left(left).right(right).build();

        if (ctx.op.MINUS() != null)
            return Expr.builder().arithmOperator(Expr.ArithmOperator.SUB).left(left).right(right).build();

        if (ctx.op.MULT() != null)
            return Expr.builder().arithmOperator(Expr.ArithmOperator.MUL).left(left).right(right).build();

        if (ctx.op.MOD() != null)
            return Expr.builder().arithmOperator(Expr.ArithmOperator.MOD).left(left).right(right).build();

        return null; // Impossible
    }

    @Override
    public Expr visitStrExpr(StrExprContext ctx) {
        Expr left = this.visit(ctx.leftExpr);
        Expr right = this.visit(ctx.rightExpr);

        if (ctx.op.PLUS() != null)
            return Expr.builder().strOperator(Expr.StrOperator.CONCAT).left(left).right(right).build();

        return null; // Impossible
    }

    @Override
    public Expr visitBoolLiteral(BoolLiteralContext ctx) {
        if (ctx.bool().TRUE() != null)
            return Expr.builder().bool(true).build();

        if (ctx.bool().FALSE() != null)
            return Expr.builder().bool(false).build();

        return null; // Impossible
    }

    @Override
    public Expr visitStrLiteral(StrLiteralContext ctx) {
        String str = ctx.getText();
        return Expr.builder().string(str).build();
    }

    @Override
    public Expr visitThisExpr(ThisExprContext ctx) {
        Expr boolExpr = null;

        PropContext propCtx = ctx.prop();
        if (propCtx != null) {
            boolExpr = super.visit(propCtx);
        } else {
            FuncContext funcCtx = ctx.func();
            if (funcCtx != null)
                boolExpr = super.visit(funcCtx);
        }

        return boolExpr.toBuilder().useCaller(true).build();
    }
}
