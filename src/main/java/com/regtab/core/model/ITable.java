package com.regtab.core.model;

import lombok.NonNull;

import java.util.Arrays;
import java.util.List;

public final class ITable {
    private final IRow[] rows;
    private final ICol[] cols;
    private final ICell[] cells;

    public IRow[] copyRows() {
        return Arrays.copyOf(rows, rows.length);
    }

    public ICol[] copyCols() {
        return Arrays.copyOf(cols, cols.length);
    }

    public List<IRow> rowsAsList() {
        return Arrays.asList(rows);
    }

    public List<ICol> colsAsList() {
        return Arrays.asList(cols);
    }

    public boolean isEmpty() {
        return cells.length == 0 ? true : false;
    }

    public ITable(int numOfRows, int numOfCols) {
        if (numOfRows < 1)
            throw new IllegalArgumentException("Number of rows is less 1");

        if (numOfCols < 1)
            throw new IllegalArgumentException("Number of columns is less 1");

        rows = new IRow[numOfRows];
        for (int i = 0; i < numOfRows; i++) {
            rows[i] = new IRow(i, numOfCols);
        }

        cols = new ICol[numOfCols];
        for (int i = 0; i < numOfCols; i++) {
            cols[i] = new ICol(i, numOfRows);
        }

        cells = new ICell[numOfRows * numOfCols];
    }

    private int last;

    private boolean add(@NonNull ICell cell) {
        if (last == cells.length)
            return false;
        cells[last] = cell;
        last++;
        return true;
    }

    public ICell createCell(int rowIndex, int colIndex, @NonNull String text) {
        if (rowIndex < 0 || rowIndex >= rows.length)
            throw new IllegalArgumentException("Row index is out of bounds");

        if (colIndex < 0 || colIndex >= cols.length)
            throw new IllegalArgumentException("Col index is out of bounds");

        final IRow row = rows[rowIndex];
        final ICol col = cols[colIndex];
        final ICell cell = new ICell(this, row, col, text);

        row.add(cell);
        col.add(cell);
        add(cell);

        return cell;
    }

    public Recordset extract() {
        final Recordset recordset = new Recordset();

        for (ICell cell: cells)
            cell.perform(Action.Type.FACTOR, recordset);

        for (ICell cell: cells)
            cell.perform(Action.Type.PREFIX, recordset);

        for (ICell cell: cells)
            cell.perform(Action.Type.SUFFIX, recordset);



        for (ICell cell: cells)
            cell.perform(Action.Type.RECORD, recordset);

        for (ICell cell: cells)
            cell.perform(Action.Type.SCHEMA, recordset);

        recordset.excludeNulls();

        return recordset;
    }

}
