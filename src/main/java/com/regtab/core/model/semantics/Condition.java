package com.regtab.core.model.semantics;

import lombok.NonNull;

import com.regtab.core.model.ICell;

import java.util.ArrayList;
import java.util.List;

public final class Condition {
    private final List<Constraint> constraints = new ArrayList<>(1);

    public void addConstraint(@NonNull Constraint constraint) {
        constraints.add(constraint);
    }

    public List<Constraint> constraints() {
        return new ArrayList<>(constraints);
    }

    public boolean check(@NonNull ICell caller, @NonNull ICell candidate) {
        for (Constraint constraint : constraints) {
            if (!constraint.eval(caller, candidate))
                return false;
        }
        return true;
    }

    public Condition join(@NonNull Condition cond) {
        if (cond != null)
            this.constraints.addAll(cond.constraints());

        return this;
    }

}
