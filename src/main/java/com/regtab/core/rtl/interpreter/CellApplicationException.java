package com.regtab.core.rtl.interpreter;

import com.regtab.core.model.ICell;
import lombok.Getter;

public class CellApplicationException extends Exception {
    @Getter
    private final ICell cell;

    @Getter
    private final int textShift;

    public CellApplicationException(ICell cell, int textShift) {
        this.cell = cell;
        this.textShift = textShift;
    }

    public String getMessage() {
        final String msg = "%s could not be matched starting from the position: %d";
        return String.format(msg, cell, textShift);
    }
}
