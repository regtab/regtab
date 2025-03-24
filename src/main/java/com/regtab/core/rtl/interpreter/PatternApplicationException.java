package com.regtab.core.rtl.interpreter;

import com.regtab.core.model.ICell;
import lombok.Getter;

public class PatternApplicationException extends Exception {
    @Getter
    private final ICell cell;

    @Getter
    private final RTLPattern.BasePattern pattern;


    public PatternApplicationException(ICell cell, RTLPattern.BasePattern pattern) {
        this.cell = cell;
        this.pattern = pattern;
    }

    public String getMessage() {
        final String msg = "Pattern %s could not be applied to the cell %s";
        return String.format(msg, pattern, cell);
    }
}
