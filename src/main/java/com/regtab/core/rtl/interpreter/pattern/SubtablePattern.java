package com.regtab.core.rtl.interpreter.pattern;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import com.regtab.core.model.semantics.Action;
import com.regtab.core.model.semantics.Condition;
import com.regtab.core.rtl.parser.RTLParser.*;

import java.util.ArrayList;
import java.util.List;

public final class SubtablePattern extends RepeatablePattern {
    public SubtablePattern(@NonNull SubtableContext context) {
        super(context);
    }

    public RowsContext rowsContext;

    @Override
    public void setCondition(@NonNull Condition condition) {
        super.setCondition(condition);

        for (RowPattern rowPattern : rowPatterns) {
            rowPattern.setCondition(condition);
        }
    }

    @Getter
    @Setter(AccessLevel.PACKAGE)
    private TablePattern tablePattern;

    @Getter
    private final List<RowPattern> rowPatterns = new ArrayList<>();

    public void add(@NonNull RowPattern pattern) {
        pattern.setSubtablePattern(this);
        rowPatterns.add(pattern);
    }

    public void add(@NonNull Action action) {
        getActions().add(action);

        for (RowPattern rowPattern : rowPatterns) {
            rowPattern.add(action);
        }
    }

}
