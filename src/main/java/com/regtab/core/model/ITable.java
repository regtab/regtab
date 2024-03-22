package com.regtab.core.model;

import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public final class ITable {
    private final List<IRow> rows;
    private final List<ICol> cols;
    private final List<ICell> cells;

    public List<IRow> rows() {
        return new ArrayList<>(rows);
    }

    public List<ICol> cols() {
        return new ArrayList<>(cols);
    }

    public boolean isEmpty() {
        return cells.isEmpty();
    }

    @Getter
    private final int numOfRows;

    @Getter
    private final int numOfCols;

    @Getter
    private final Recordset recordset = new Recordset();

    public ITable(int numOfRows, int numOfCols) {
        this.numOfRows = numOfRows;
        rows = new ArrayList<>(numOfRows);
        for (int i = 0; i < numOfRows; i++) {
            IRow row = new IRow(i);
            rows.add(row);
        }

        this.numOfCols = numOfCols;
        cols = new ArrayList<>(numOfCols);
        for (int i = 0; i < numOfCols; i++) {
            ICol col = new ICol(i);
            cols.add(col);
        }

        cells = new ArrayList<>(numOfRows * numOfCols);
    }

    public ICell createCell(int r, int c, @NonNull String text) {
        IRow row = rows.get(r);
        ICol col = cols.get(c);
        ICell cell = new ICell(this, row, col, text);
        row.addCell(cell);
        col.addCell(cell);
        cells.add(cell);

        return cell;
    }

    public void performActions() {
        for (ICell cell: cells)
            cell.perform(Action.Type.FACTOR);

        for (ICell cell: cells)
            cell.perform(Action.Type.CONCAT);

        for (ICell cell: cells)
            cell.perform(Action.Type.RECORD);

        for (ICell cell: cells)
            cell.perform(Action.Type.GROUP);

        for (ICell cell: cells)
            cell.perform(Action.Type.SCHEMA);

        recordset.genAttributes();
        recordset.excludeNulls();
    }
}
