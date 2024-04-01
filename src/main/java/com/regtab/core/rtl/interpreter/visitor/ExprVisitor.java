package com.regtab.core.rtl.interpreter.visitor;

import com.regtab.core.model.semantics.Expr;
import com.regtab.core.model.semantics.Func;
import com.regtab.core.model.semantics.Prop;

import java.util.List;

import com.regtab.core.rtl.RTLSyntaxException;
import com.regtab.core.rtl.parser.RTLBaseVisitor;
import com.regtab.core.rtl.parser.RTLParser.*;

final class ExprVisitor extends RTLBaseVisitor<Expr> {
    @Override
    public Expr visitIntLiteral(IntLiteralContext ctx) {
        final String str = ctx.INT().getText();
        final Integer i = Integer.valueOf(str);

        return Expr.builder().integer(i).build();
    }

    @Override
    public Expr visitProp(PropContext ctx) {
        final String id = ctx.getText();
        final Prop<?> prop = Prop.get(id);

        if (prop == null) {
            final String msg = String.format("undefined property \"%s\"", id);
            throw new RTLSyntaxException(msg, ctx);
        }

        return Expr.builder().prop(prop).build();
    }

    @Override
    public Expr visitPropExpr(PropExprContext ctx) {
        return super.visit(ctx.prop());
    }

    @Override
    public Expr visitFunc(FuncContext ctx) {
        final String id = ctx.ID().getText();
        final Func<?> func = Func.get(id);
        if (func == null) {
            final String msg = String.format("undefined function \"%s\"", id);
            throw new RTLSyntaxException(msg, ctx);
        }

        final List<ArgContext> argCtxList = ctx.arg();
        if (argCtxList != null) {
            for (ArgContext argCtx : argCtxList) {
                if (argCtx.INT() != null) {
                    final String str = argCtx.INT().getText();
                    final Integer integer = Integer.valueOf(str);
                    func.addArg(integer);
                } else if (argCtx.STRING() != null) {
                    final String str = argCtx.STRING().getText();
                    func.addArg(str);
                }
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
        final Expr boolExpr = visit(ctx.expr());

        if (boolExpr == null)
            return null; // Impossible

        return Expr.builder().notExpr(boolExpr).build();
    }

    @Override
    public Expr visitParenExpr(ParenExprContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Expr visitCompExpr(CompExprContext ctx) {
        final Expr left = this.visit(ctx.leftExpr);
        final Expr right = this.visit(ctx.rightExpr);

        if (left == null || right == null)
            return null; // Impossible

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
        final Expr left = this.visit(ctx.leftExpr);
        final Expr right = this.visit(ctx.rightExpr);

        if (left == null || right == null)
            return null; // Impossible

        if (ctx.op.AND() != null)
            return Expr.builder().binaryOperator(Expr.BinaryOperator.AND).left(left).right(right).build();

        if (ctx.op.OR() != null)
            return Expr.builder().binaryOperator(Expr.BinaryOperator.OR).left(left).right(right).build();

        return null; // Impossible
    }

    @Override
    public Expr visitArithmExpr(ArithmExprContext ctx) {
        final Expr left = this.visit(ctx.leftExpr);
        final Expr right = this.visit(ctx.rightExpr);

        if (left == null || right == null)
            return null; // Impossible

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
        final Expr left = this.visit(ctx.leftExpr);
        final Expr right = this.visit(ctx.rightExpr);

        if (left == null || right == null)
            return null; // Impossible

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
        final String str = ctx.getText();
        if (str == null)
            return null; // Impossible

        return Expr.builder().string(str).build();
    }

    @Override
    public Expr visitHexLiteral(HexLiteralContext ctx) {
        final String hex = ctx.getText().toLowerCase();
        if (!hex.matches("[a-f0-9]{6}")) {
            final String msg = String.format("incorrect hexadecimal \"%s\"", hex);
            throw new RTLSyntaxException(msg, ctx);
        }

        return Expr.builder().hex(hex).build();
    }

    @Override
    public Expr visitThisExpr(ThisExprContext ctx) {
        final Expr expr;

        final PropContext propCtx = ctx.prop();
        if (propCtx != null) {
            expr = super.visit(propCtx);
        } else {
            final FuncContext funcCtx = ctx.func();
            if (funcCtx != null)
                expr = super.visit(funcCtx);
            else
                return null; // Impossible
        }

        return expr.toBuilder().useCaller(true).build();
    }
}
