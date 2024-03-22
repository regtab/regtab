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

    public ITable(int numOfRows, int numOfCols) {
        this.numOfRows = numOfRows;
        rows = new IRow[numOfRows];
        for (int i = 0; i < numOfRows; i++) {
            rows[i] = new IRow(i);
        }

        this.numOfCols = numOfCols;
        cols = new ICol[numOfCols];
        for (int i = 0; i < numOfCols; i++) {
            cols[i] = new ICol(i);
        }

        cells = new ICell[numOfRows * numOfCols];
        int count = 0;
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfCols; j++) {
                ICell cell = new ICell(this, rows[i], cols[j]);
                rows[i].getCells().add(cell);
                cols[j].getCells().add(cell);
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

        return rows[rowIndex].getCells().get(colIndex);
    }

//    public ICell createCell(final int rowIndex, final int colIndex, @NonNull String text) {
//        if (rowIndex < 0 || rowIndex >= rows.length)
//            throw new IllegalArgumentException("Row index is out of bounds");
//
//        if (rowIndex < 0 || rowIndex >= rows.length)
//            throw new IllegalArgumentException("Col index is out of bounds");
//
//        final IRow row = rows[rowIndex];
//        final ICell cell = row.getCells().get(colIndex);
//        if (text.length() > 0 && text.isBlank()) text = "";
//        cell.setText(text);
//
//        return cell;
//    }

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
