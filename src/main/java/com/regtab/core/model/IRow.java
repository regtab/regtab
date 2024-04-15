package com.regtab.core.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public final class IRow {
    @Getter
    private final int position;

    private final ICell[] cells;

    private int last;

    boolean add(final ICell cell) {
        if (last == cells.length)
            return false;
        cells[last] = cell;
        last++;
        return true;
    }

    ICell get(int index) {
        return cells[index];
    }

    public ICell[] copyCells() {
        return Arrays.copyOf(cells, cells.length);
    }

    public List<ICell> cellsAsList() {
        return Arrays.asList(cells);
    }

    public int size() {
        return cells.length;
    }

    IRow(int position, int size) {
        this.position = position;
        cells = new ICell[size];
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("position", position)
                .append("cells", Arrays.toString(cells))
                .toString();
    }
}
