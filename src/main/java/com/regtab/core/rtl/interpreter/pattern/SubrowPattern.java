package com.regtab.core.rtl.interpreter.pattern;

import com.regtab.core.model.semantics.Action;
import com.regtab.core.model.semantics.Condition;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import com.regtab.core.rtl.parser.RTLParser.*;

public final class SubrowPattern extends RepeatablePattern {
    public SubrowPattern(SubrowContext context) {
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

    public void add(CellPattern tmpl) {
        tmpl.setSubrowPattern(this);
        cellPatterns.add(tmpl);
    }

    public void add(@NonNull Action action) {
        getActions().add(action);

        for (CellPattern cellPattern : cellPatterns) {
            cellPattern.add(action);
        }
    }
}
