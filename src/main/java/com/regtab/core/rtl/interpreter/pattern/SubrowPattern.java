package com.regtab.core.rtl.interpreter.pattern;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.Getter;
import lombok.Setter;

import com.regtab.core.model.semantics.Action;
import com.regtab.core.model.semantics.Condition;
import com.regtab.core.rtl.parser.RTLParser.*;

import java.util.ArrayList;
import java.util.List;

public final class SubrowPattern extends RepeatablePattern {
    public SubrowPattern(@NonNull SubrowContext context) {
        super(context);
    }

    public CellsContext cellsContext;

    @Override
    public void setCondition(@NonNull Condition condition) {
        super.setCondition(condition);

        for (CellPattern cellPattern : cellPatterns) {
            cellPattern.setCondition(condition);
        }
    }

    @Getter
    @Setter(AccessLevel.PACKAGE)
    private RowPattern rowPattern;

    @Getter
    private final List<CellPattern> cellPatterns = new ArrayList<>();

    public void add(@NonNull CellPattern pattern) {
        pattern.setSubrowPattern(this);
        cellPatterns.add(pattern);
    }

    public void add(@NonNull Action action) {
        getActions().add(action);

        for (CellPattern cellPattern : cellPatterns) {
            cellPattern.add(action);
        }
    }
}
