package com.regtab.core.model;

import com.regtab.core.model.recordset.Recordset;
import com.regtab.core.model.semantics.Action;
import lombok.Getter;
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

    @Getter
    private final int numOfRows;

    @Getter
    private final int numOfCols;

    @Getter
    private final Recordset recordset = new Recordset();

    public ITable(final int numOfRows, final int numOfCols) {
        this.numOfRows = numOfRows;
        rows = new IRow[numOfRows];
        for (int i = 0; i < numOfRows; i++) {
            rows[i] = new IRow(i, numOfCols);
        }

        this.numOfCols = numOfCols;
        cols = new ICol[numOfCols];
        for (int i = 0; i < numOfCols; i++) {
            cols[i] = new ICol(i, numOfRows);
        }

        cells = new ICell[numOfRows * numOfCols];
        int count = 0;
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfCols; j++) {
                ICell cell = new ICell(this, rows[i], cols[j]);
                rows[i].cells[j] = cell;
                cols[j].cells[i] = cell;
                cells[count] = cell;
                count++;
            }
        }
    }

    public ICell findCell(final int rowIndex, final int colIndex) {
        if (rowIndex < 0 || rowIndex >= rows.length)
            return null; // Row index is out of bounds

        if (rowIndex < 0 || rowIndex >= rows.length)
            return null; // Col index is out of bounds

        return rows[rowIndex].cells[colIndex];
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
