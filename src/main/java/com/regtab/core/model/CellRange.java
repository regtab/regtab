package com.regtab.core.model;

import lombok.Getter;
import lombok.NonNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class CellRange {
    @Getter
    private final int position;

    private final List<ICell> cells = new ArrayList<>();

    public void addCell(@NonNull ICell cell) {
        cells.add(cell);
    }

    public List<ICell> cells() {
        return new ArrayList<>(cells);
    }

    public int size() {
        return cells.size();
    }

    public boolean isEmpty() {
        return cells.isEmpty();
    }

    CellRange(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        String str = Arrays.toString(cells.toArray());

        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("position", position)
                .append("cells", str)
                .toString();
    }
}
