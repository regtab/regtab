package com.regtab.core.model;

import com.regtab.core.model.recordset.Recordset;
import com.regtab.core.model.semantics.Action;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ITable {
    //private final List<IRow> rows;
    //private final List<ICol> cols;
    private final IRow[] rows;
    private final ICol[] cols;
    private final List<ICell> cells;

    public IRow[] copyRows() {
        return Arrays.copyOf(rows, rows.length);
    }

    public ICol[] copyCols() {
        return Arrays.copyOf(cols, cols.length);
    }

    public List<IRow> rowsAsList() {
        return Arrays.asList(rows);// new ArrayList<>(rows);
    }

    public List<ICol> colsAsList() {
        return Arrays.asList(cols); //new ArrayList<>(cols);
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
        rows = new IRow[numOfRows];//new ArrayList<>(numOfRows);
        for (int i = 0; i < numOfRows; i++) {
            //IRow row = new IRow(i);
            //rows.add(row);
            rows[i] = new IRow(i);
        }

        this.numOfCols = numOfCols;
        cols = new ICol[numOfCols];//new ArrayList<>(numOfCols);
        for (int i = 0; i < numOfCols; i++) {
            //ICol col = new ICol(i);
            //cols.add(col);
            cols[i] = new ICol(i);
        }

        cells = new ArrayList<>(numOfRows * numOfCols);
    }

    public ICell createCell(int r, int c, @NonNull String text) {
        IRow row = rows[r];//.get(r);
        ICol col = cols[c];//.get(c);
        ICell cell = new ICell(this, row, col, text);
        row.getCells().add(cell);
        col.getCells().add(cell);
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
