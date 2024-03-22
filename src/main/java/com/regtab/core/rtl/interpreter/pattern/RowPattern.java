package com.regtab.core.rtl.interpreter.pattern;

import com.regtab.core.model.semantics.Action;
import com.regtab.core.model.semantics.Condition;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import com.regtab.core.rtl.parser.RTLParser.*;

import java.util.ArrayList;
import java.util.List;

public final class RowPattern extends RepeatablePattern {

    public SubrowsContext subrowsContext;
    public RowPattern(RowContext context) {
        super(context);
    }

    @Override
    public void setCondition(@NonNull Condition condition) {
        super.setCondition(condition);

        for (SubrowPattern subrowTemplate : subrowTemplates) {
            subrowTemplate.setCondition(condition);
        }
    }

    @Getter
    @Setter(AccessLevel.PACKAGE)
    private SubtablePattern subtableTemplate;

    @Getter
    private final List<SubrowPattern> subrowTemplates = new ArrayList<>(1);

    public void add(SubrowPattern tmpl) {
        tmpl.setRowTemplate(this);
        subrowTemplates.add(tmpl);
    }

    public void add(@NonNull Action action) {
        getActions().add(action);

        for (SubrowPattern subrowTemplate : subrowTemplates) {
            subrowTemplate.add(action);
        }
    }

}
