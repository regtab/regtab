package com.regtab.core.model;

import com.regtab.core.model.recordset.Recordset;
import com.regtab.core.model.semantics.Action;
import lombok.Getter;

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

    @Getter
    private final Recordset recordset = new Recordset();

    public ITable(final int numOfRows, final int numOfCols) {
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

    private boolean add(final ICell cell) {
        if (last == cells.length)
            return false;
        cells[last] = cell;
        last++;
        return true;
    }

    public void complete() {
        for (int i = 0; i < rows.length; i++) {
            for (int j = 0; j < cols.length; j++) {
                final ICell cell = rows[i].get(j);
                if (cell == null)
                    createCell(i, j, "", false);
            }
        }
    }

    public ICell createCell(final int rowIndex, final int colIndex, final String text, final boolean multiline) {
        if (rowIndex < 0 || rowIndex >= rows.length)
            throw new IllegalArgumentException("Row index is out of bounds");

        if (colIndex < 0 || colIndex >= cols.length)
            throw new IllegalArgumentException("Col index is out of bounds");

        final IRow row = rows[rowIndex];
        final ICol col = cols[colIndex];
        final ICell cell = new ICell(this, row, col, text, multiline);

        row.add(cell);
        col.add(cell);
        add(cell);

        return cell;
    }

    public Recordset performActions() {
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

        return recordset;
    }
}
