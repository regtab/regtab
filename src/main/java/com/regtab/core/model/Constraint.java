package com.regtab.core.model;

import lombok.*;

/**
 * The Constraint class represents a boolean expression that can be evaluated against a cell.
 */
@RequiredArgsConstructor
public final class Constraint {
    private final Expr boolExpr;

    /**
     * Evaluates the constraint against a candidate cell, given a caller cell.
     *
     * @param caller The caller cell.
     * @param candidate The candidate cell.
     * @return The result of the evaluation.
     * @throws IllegalStateException if the evaluation fails or the result is not a boolean.
     */
    public boolean eval(@NonNull ICell caller, @NonNull ICell candidate) {
        final Object result = boolExpr.eval(caller, candidate);
        if (result == null)
            throw new IllegalStateException("Unable to evaluate expression");

        if (result instanceof Boolean)
            return (Boolean) result;
        else
            throw new IllegalStateException("Expression is not a boolean");
    }

    /**
     * Evaluates the constraint against a caller cell.
     *
     * @param caller The caller cell.
     * @return The result of the evaluation.
     * @throws IllegalStateException if the evaluation fails or the result is not a boolean.
     */
    public boolean eval(@NonNull ICell caller) {
        final Object result = boolExpr.eval(caller);
        if (result == null)
            throw new IllegalStateException("Unable to evaluate expression");

        if (result instanceof Boolean)
            return (Boolean) result;
        else
            throw new IllegalStateException("Expression is not a boolean");
    }

}
