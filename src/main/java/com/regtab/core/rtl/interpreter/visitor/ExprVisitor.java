package com.regtab.core.rtl.interpreter.visitor;

import com.regtab.core.model.semantics.Expr;
import com.regtab.core.model.semantics.Func;
import com.regtab.core.model.semantics.Prop;

import java.lang.reflect.Type;
import java.util.List;

import com.regtab.core.rtl.parser.RTLBaseVisitor;
import com.regtab.core.rtl.parser.RTLParser.*;

final class ExprVisitor extends RTLBaseVisitor<Expr> {
    @Override
    public Expr<Integer> visitIntLiteral(IntLiteralContext ctx) {
        final String str = ctx.INT().getText();
        final Integer i = Integer.valueOf(str);

        return Expr.<Integer>builder().integer(i).build();
    }

    @Override
    public Expr<Object> visitProp(PropContext ctx) {
        final String id = ctx.getText();
        final Prop<Object> prop = Prop.get(id);

        if (prop == null)
            return null; // TODO log (такого свойства нет)

        return Expr.builder().prop(prop).build();
    }

    @Override
    public Expr<Object> visitPropExpr(PropExprContext ctx) {
        return super.visit(ctx.prop());
    }

    @Override
    public Expr<Object> visitFunc(FuncContext ctx) {
        String id = ctx.ID().getText();
        final Func<Object> func = Func.get(id);
        if (func == null)
            return null; // TODO log (такой функции нет)

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
                } else if (argCtx.prop() != null) {
                    id = argCtx.prop().ID().getText();
                    final Prop<?> prop = Prop.get(id);
                    if (prop == null)
                        return null; // TODO log (такого свойства нет)
                    func.addArg(prop);
                }
                // TODO реализовать поддержку аргументов func
            }
        }

        return Expr.builder().func(func).build();
    }

    @Override
    public Expr<Object> visitFuncExpr(FuncExprContext ctx) {
        return super.visit(ctx.func());
    }

    @Override
    public Expr<Boolean> visitNotExpr(NotExprContext ctx) {
        final Expr<Boolean> boolExpr = visit(ctx.expr());

        if (boolExpr == null)
            return null; // TODO log

        return Expr.<Boolean>builder().notExpr(boolExpr).build();
    }

    @Override
    public Expr<Object> visitParenExpr(ParenExprContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Expr<Boolean> visitCompExpr(CompExprContext ctx) {
        final Expr<Object> left = this.visit(ctx.leftExpr);
        final Expr<Object> right = this.visit(ctx.rightExpr);

        if (left == null || right == null)
            return null; // TODO log

        if (ctx.op.EQ() != null)
            return Expr.<Boolean>builder().compOperator(Expr.CompOperator.EQUAL).left(left).right(right).build();

        if (ctx.op.NEQ() != null)
            return Expr.<Boolean>builder().compOperator(Expr.CompOperator.NOT_EQUAL).left(left).right(right).build();

        if (ctx.op.GT() != null)
            return Expr.<Boolean>builder().compOperator(Expr.CompOperator.GREATER).left(left).right(right).build();

        if (ctx.op.GE() != null)
            return Expr.<Boolean>builder().compOperator(Expr.CompOperator.GREATER_OR_EQUAL).left(left).right(right).build();

        if (ctx.op.LT() != null)
            return Expr.<Boolean>builder().compOperator(Expr.CompOperator.LESS).left(left).right(right).build();

        if (ctx.op.LE() != null)
            return Expr.<Boolean>builder().compOperator(Expr.CompOperator.LESS_OR_EQUAL).left(left).right(right).build();

        if (ctx.op.CONTAINS() != null)
            return Expr.<Boolean>builder().compOperator(Expr.CompOperator.CONTAINS).left(left).right(right).build();

        if (ctx.op.MATCHES() != null)
            return Expr.<Boolean>builder().compOperator(Expr.CompOperator.MATCHES).left(left).right(right).build();

        return null; // Impossible
    }

    @Override
    public Expr<Boolean> visitBinaryExpr(BinaryExprContext ctx) {
        final Expr<Object> left = this.visit(ctx.leftExpr);
        final Expr<Object> right = this.visit(ctx.rightExpr);

        if (left == null || right == null)
            return null; // TODO log

        if (ctx.op.AND() != null)
            return Expr.<Boolean>builder().binaryOperator(Expr.BinaryOperator.AND).left(left).right(right).build();

        if (ctx.op.OR() != null)
            return Expr.<Boolean>builder().binaryOperator(Expr.BinaryOperator.OR).left(left).right(right).build();

        return null; // Impossible
    }

    @Override
    public Expr<Integer> visitArithmExpr(ArithmExprContext ctx) {
        final Expr<Integer> left = this.visit(ctx.leftExpr);
        final Expr<Integer> right = this.visit(ctx.rightExpr);

        if (left == null || right == null)
            return null; // TODO log

        if (ctx.op.PLUS() != null)
            return Expr.<Integer>builder().arithmOperator(Expr.ArithmOperator.SUM).left(left).right(right).build();

        if (ctx.op.MINUS() != null)
            return Expr.<Integer>builder().arithmOperator(Expr.ArithmOperator.SUB).left(left).right(right).build();

        if (ctx.op.MULT() != null)
            return Expr.<Integer>builder().arithmOperator(Expr.ArithmOperator.MUL).left(left).right(right).build();

        if (ctx.op.MOD() != null)
            return Expr.<Integer>builder().arithmOperator(Expr.ArithmOperator.MOD).left(left).right(right).build();

        return null; // Impossible
    }

    @Override
    public Expr<String> visitStrExpr(StrExprContext ctx) {
        final Expr<Object> left = this.visit(ctx.leftExpr);
        final Expr<Object> right = this.visit(ctx.rightExpr);

        if (left == null || right == null)
            return null; // TODO log

        if (ctx.op.PLUS() != null)
            return Expr.<String>builder().strOperator(Expr.StrOperator.CONCAT).left(left).right(right).build();

        return null; // Impossible
    }

    @Override
    public Expr<Boolean> visitBoolLiteral(BoolLiteralContext ctx) {
        if (ctx.bool().TRUE() != null)
            return Expr.<Boolean>builder().bool(true).build();

        if (ctx.bool().FALSE() != null)
            return Expr.<Boolean>builder().bool(false).build();

        return null; // Impossible
    }

    @Override
    public Expr<String> visitStrLiteral(StrLiteralContext ctx) {
        final String str = ctx.getText();

        return Expr.<String>builder().string(str).build();
    }

    @Override
    public Expr<String> visitHexLiteral(HexLiteralContext ctx) {
        final String hex = ctx.getText().toLowerCase();
        if (!hex.matches("[a-f0-9]{6}"))
            return null; // TODO log

        return Expr.<String>builder().hex(hex).build();
    }

    @Override
    public Expr<Object> visitThisExpr(ThisExprContext ctx) {
        final Expr<Object> expr;

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
