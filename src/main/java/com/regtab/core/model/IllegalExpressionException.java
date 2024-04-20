package com.regtab.core.model;

import lombok.Getter;
import org.antlr.v4.runtime.tree.ParseTree;

public class IllegalExpressionException extends RuntimeException {
    @Getter
    private final Expr expr;

    @Getter
    private final Object[] operands;

    public IllegalExpressionException(Expr expr, Object ... operands) {
        this.operands = operands;
        this.expr = expr;
    }

    public String getMessage() {
        final String exprAsString = expr.getAsString();
        final String operandsAsString = String.join(", ", (String[]) operands);

        return String.format("\"%s\" could not be evaluated for operands: %s", exprAsString, operandsAsString);
    }
}