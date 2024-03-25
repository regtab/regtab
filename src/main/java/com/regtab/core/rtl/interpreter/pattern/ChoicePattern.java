package com.regtab.core.rtl.interpreter.pattern;

import com.regtab.core.model.semantics.Action;
import com.regtab.core.model.semantics.Condition;
import com.regtab.core.model.ICell;
import lombok.NonNull;

import com.regtab.core.rtl.parser.RTLParser.*;

public final class ChoicePattern extends ElementsPattern {
    public ChoicePattern(ChoiceContext context) {
        super(context);
    }

    public Condition condition;

    public ElementsPattern left;

    public ElementsPattern right;

    public void add(@NonNull Action action) {
        left.add(action);
        right.add(action);
    }

    @Override
    public boolean apply(@NonNull ICell cell) {
        final boolean result = condition.check(cell, cell); // TODO заменить на один аргумент
        return result ? left.apply(cell) : right.apply(cell);
    }

}
