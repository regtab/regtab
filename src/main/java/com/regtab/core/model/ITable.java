package com.regtab.core.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

/**
 * This class represents an Interpretable Table Model. It contains rows, columns, and cells.
 * It provides methods to manipulate and extract data from the table.
 */
public final class ITable {
    private final IRow[] rows;
    private final ICol[] cols;
    private final ICell[] cells;

    public int rowsSize() {
        return rows.length;
    }

    public int colsSize() {
        return cols.length;
    }

    /**
     * Copies the rows of the table.
     *
     * @return A copy of the rows of the table.
     */
    public IRow[] copyRows() {
        return Arrays.copyOf(rows, rows.length);
    }

    /**
     * Copies the columns of the table.
     *
     * @return A copy of the columns of the table.
     */
    public ICol[] copyCols() {
        return Arrays.copyOf(cols, cols.length);
    }

    /**
     * Returns the rows of the table as a list.
     *
     * @return The rows of the table as a list.
     */
    public List<IRow> rowsAsList() {
        return Arrays.asList(rows);
    }

    /**
     * Returns the columns of the table as a list.
     *
     * @return The columns of the table as a list.
     */
    public List<ICol> colsAsList() {
        return Arrays.asList(cols);
    }

    /**
     * Checks if the table is empty.
     *
     * @return True if the table is empty, false otherwise.
     */
    public boolean isEmpty() {
        return cells.length == 0;
    }

    /**
     * Constructs an ITable with the specified number of rows and columns.
     *
     * @param numOfRows The number of rows in the table.
     * @param numOfCols The number of columns in the table.
     * @throws IllegalArgumentException If the number of rows or columns is less than 1.
     */
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

    /**
     * Adds a cell to the table.
     *
     * @param cell The cell to add.
     * @return True if the cell was added, false otherwise.
     */
    private boolean add(@NonNull ICell cell) {
        if (last == cells.length)
            return false;
        cells[last] = cell;
        last++;
        return true;
    }

    /**
     * Creates a cell at the specified row and column index with the given text.
     *
     * @param rowIndex The index of the row where the cell will be created.
     * @param colIndex The index of the column where the cell will be created.
     * @param text The text to be set in the cell.
     * @return The created cell.
     * @throws IllegalArgumentException If the row or column index is out of bounds.
     */
    public ICell createCell(int rowIndex, int colIndex, CellPos cellPos, @NonNull String text) {
        if (rowIndex < 0 || rowIndex >= rows.length)
            throw new IllegalArgumentException("Row index is out of bounds");

        if (colIndex < 0 || colIndex >= cols.length)
            throw new IllegalArgumentException("Col index is out of bounds");

        final IRow row = rows[rowIndex];
        final ICol col = cols[colIndex];
        final ICell cell = new ICell(this, row, col, cellPos, text);

        row.add(cell);
        col.add(cell);
        add(cell);

        return cell;
    }

    @Getter
    @Setter
    private boolean splitComponents = false;

    @NonNull
    @Getter
    @Setter
    private String compSeparator = "";

    @Getter
    @Setter
    private int basicFiledIndex = 0;

    @Getter
    @Setter
    private boolean alignedNamedAttrs = false;

    @Getter
    @Setter
    private boolean normalizedSpaces = true;

    /**
     * Extracts data from the table and returns it as a Recordset.
     *
     * @return The extracted data as a Recordset.
     */
    public Recordset extract() {
        final Recordset recordset = new Recordset(splitComponents, basicFiledIndex);

        for (ICell cell: cells)
            cell.perform(Action.Type.FACTOR, recordset);

        for (ICell cell: cells)
            cell.perform(Action.Type.PREFIX, recordset);

        for (ICell cell: cells)
            cell.perform(Action.Type.SUFFIX, recordset);

        for (ICell cell: cells)
            cell.perform(Action.Type.RECORD, recordset);

        for (ICell cell: cells)
            cell.perform(Action.Type.JOIN, recordset);

        for (ICell cell: cells)
            cell.perform(Action.Type.SCHEMA, recordset);

        if (alignedNamedAttrs) {
            recordset.align();
        }
        recordset.complete();

        return recordset;
    }

    /**
     * Clears all cells in the table.
     */
    public void clear() {
        for (ICell cell: cells)
            cell.clear();
    }

}
