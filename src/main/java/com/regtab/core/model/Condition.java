package com.regtab.core.model;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * The Condition class represents a set of constraints that can be evaluated against a cell.
 */
public final class Condition {
    private final List<Constraint> constraints = new ArrayList<>(1);

    /**
     * Adds a constraint to the condition.
     *
     * @param constraint The constraint to be added.
     */
    public void addConstraint(@NonNull Constraint constraint) {
        constraints.add(constraint);
    }

    /**
     * Returns a copy of the list of constraints.
     *
     * @return A copy of the list of constraints.
     */
    public List<Constraint> constraints() {
        return new ArrayList<>(constraints);
    }

    /**
     * Checks if the condition is satisfied by a candidate cell, given a caller cell.
     *
     * @param caller The caller cell.
     * @param candidate The candidate cell.
     * @return True if the condition is satisfied, false otherwise.
     */
    public boolean check(@NonNull ICell caller, @NonNull ICell candidate) {
        for (Constraint constraint : constraints) {
            if (!constraint.eval(caller, candidate))
                return false;
        }
        return true;
    }

    /**
     * Checks if the condition is satisfied by a caller cell.
     *
     * @param caller The caller cell.
     * @return True if the condition is satisfied, false otherwise.
     */
    public boolean check(@NonNull ICell caller) {
        for (Constraint constraint : constraints) {
            if (!constraint.eval(caller))
                return false;
        }
        return true;
    }

    /**
     * Joins another condition to this one.
     *
     * @param cond The condition to be joined.
     * @return This condition.
     */
    public Condition join(@NonNull Condition cond) {
        if (cond != null)
            this.constraints.addAll(cond.constraints());

        return this;
    }

}
