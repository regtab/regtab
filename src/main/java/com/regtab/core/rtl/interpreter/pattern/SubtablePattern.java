package com.regtab.core.rtl.interpreter.pattern;

import com.regtab.core.model.Action;
import com.regtab.core.model.Condition;
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

        for (RowPattern rowTemplate : rowTemplates) {
            rowTemplate.setCondition(condition);
        }
    }

    @Getter
    @Setter(AccessLevel.PACKAGE)
    private TablePattern tableTemplate;

    @Getter
    private final List<RowPattern> rowTemplates = new ArrayList<>();

    public void add(RowPattern tmpl) {
        tmpl.setSubtableTemplate(this);
        rowTemplates.add(tmpl);
    }

    public void add(@NonNull Action action) {
        getActions().add(action);

        for (RowPattern rowTemplate : rowTemplates) {
            rowTemplate.add(action);
        }
    }

}
