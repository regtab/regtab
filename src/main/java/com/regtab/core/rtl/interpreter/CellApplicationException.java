package com.regtab.core.rtl.interpreter;

import com.regtab.core.model.ICell;
import lombok.Getter;

public class CellApplicationException extends Exception {
    @Getter
    private final ICell cell;

    @Getter
    private final RTLPattern.BasePattern pattern;


    public CellApplicationException(ICell cell, RTLPattern.BasePattern pattern) {
        this.cell = cell;
        this.pattern = pattern;
    }

    public String getMessage() {
        final String msg = "%s could not be matched to %s";
        return String.format(msg, pattern, cell);
    }
}
