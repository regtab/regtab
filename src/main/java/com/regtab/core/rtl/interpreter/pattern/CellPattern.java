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
    public ElementsContext elementsContext;
    public CellPattern(CellContext context) {
        super(context);
    }

    @NonNull
    @Getter
    @Setter
    private ElementsPattern elementsTemplate; //TODO перевести в final

    @Override
    public void setCondition(@NonNull Condition condition) {
        super.setCondition(condition);
    }

    @Getter
    @Setter(AccessLevel.PACKAGE)
    private SubrowPattern subrowTemplate;

    public void add(@NonNull Action action) {
        getActions().add(action);
        if (elementsTemplate != null) //TODO убрать, после перевода elementsTemplate в final
            elementsTemplate.add(action);
    }

    public boolean apply(@NonNull ICell cell) {
        return elementsTemplate.apply(cell);
    }
}
