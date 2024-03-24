package com.regtab.core.rtl.interpreter.pattern;

import com.regtab.core.model.semantics.Action;
import com.regtab.core.model.semantics.Condition;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import com.regtab.core.rtl.parser.RTLParser.*;

public final class SubtablePattern extends RepeatablePattern {
    public SubtablePattern(SubtableContext context) {
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

    public void add(RowPattern tmpl) {
        tmpl.setSubtablePattern(this);
        rowPatterns.add(tmpl);
    }

    public void add(@NonNull Action action) {
        getActions().add(action);

        for (RowPattern rowPattern : rowPatterns) {
            rowPattern.add(action);
        }
    }

}
