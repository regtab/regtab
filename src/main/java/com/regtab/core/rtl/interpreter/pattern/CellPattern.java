package com.regtab.core.rtl.interpreter.pattern;

import com.regtab.core.model.semantics.Action;
import com.regtab.core.model.semantics.Condition;
import com.regtab.core.model.ICell;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import com.regtab.core.rtl.parser.RTLParser.*;

public final class CellPattern extends RepeatablePattern {
    public CellPattern(CellContext context) {
        super(context);
    }

    @Getter
    @Setter
    private ElementsPattern elementsPattern;

    @Override
    public void setCondition(@NonNull Condition condition) {
        super.setCondition(condition);
    }

    @Getter
    @Setter(AccessLevel.PACKAGE)
    private SubrowPattern subrowPattern;

    public void add(@NonNull Action action) {
        getActions().add(action);
        if (elementsPattern != null)
            elementsPattern.add(action);
    }

    public boolean apply(@NonNull ICell cell) {
        return elementsPattern.apply(cell);
    }
}
