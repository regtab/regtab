package com.regtab.core.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public final class ICol {
    @Getter
    private final int position;

    final ICell[] cells;

    public ICell[] copyCells() {
        return Arrays.copyOf(cells, cells.length);
    }

    public List<ICell> cellsAsList() {
        return Arrays.asList(cells);
    }

    public int size() {
        return cells.length;
    }

    ICol(final int position, final int size) {
        this.position = position;
        cells = new ICell[size];
    }

}
