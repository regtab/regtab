package com.regtab.core.model.semantics;

import com.regtab.core.model.ICell;
import lombok.*;

@RequiredArgsConstructor
public final class Constraint {
    private final Expr boolExpr;

    public boolean eval(@NonNull ICell caller, @NonNull ICell candidate) {
        final Object result = boolExpr.eval(caller, candidate);
        if (result == null)
            throw new IllegalStateException("Unable to evaluate expression");

        if (result instanceof Boolean)
            return (Boolean) result;
        else
            throw new IllegalStateException("Expression is not a boolean");
    }

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
