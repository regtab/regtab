package com.regtab.core.rtl.interpreter.pattern;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import com.regtab.core.rtl.parser.RTLParser.*;
import lombok.NonNull;

public final class TablePattern extends BasePattern {
    public TablePattern(@NonNull TableContext context) {
        super(context);
    }

    @Getter
    private final List<SubtablePattern> subtablePatterns = new ArrayList<>(1);

    public void add(@NonNull SubtablePattern tmpl) {
        tmpl.setTablePattern(this);
        subtablePatterns.add(tmpl);
    }
}
