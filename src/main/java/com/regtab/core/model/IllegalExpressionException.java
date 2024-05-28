package com.regtab.core.model;

import lombok.Getter;

/**
 * The IllegalExpressionException is a runtime exception that is thrown when an expression cannot be evaluated.
 * It contains the expression that could not be evaluated and the operands that were used.
 */
public class IllegalExpressionException extends RuntimeException {
    @Getter
    private final Expr expr;

    @Getter
    private final Object[] operands;

    /**
     * Constructs an IllegalExpressionException with the specified expression and operands.
     *
     * @param expr The expression that could not be evaluated.
     * @param operands The operands that were used when trying to evaluate the expression.
     */
    public IllegalExpressionException(Expr expr, Object ... operands) {
        this.operands = operands;
        this.expr = expr;
    }

    /**
     * Returns a message describing the exception.
     *
     * @return A message describing the exception, including the expression that could not be evaluated
     *         and the operands that were used.
     */
    public String getMessage() {
        final String exprAsString = expr.getAsString();
        final String operandsAsString = String.join(", ", (String[]) operands);

        return String.format("\"%s\" could not be evaluated for operands: %s", exprAsString, operandsAsString);
    }
}