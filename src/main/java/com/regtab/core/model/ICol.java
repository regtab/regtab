package com.regtab.core.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * The ICol class represents a column in a table. It contains an array of cells and provides
 * methods to add cells, retrieve cells, and get the column size.
 */
@RequiredArgsConstructor
public final class ICol {
    @Getter
    private final int position;

    private final ICell[] cells;

    private int last;

    /**
     * Adds a cell to the column.
     *
     * @param cell The cell to add.
     * @return True if the cell was added successfully, false otherwise.
     */
    boolean add(final ICell cell) {
        if (last == cells.length)
            return false;
        cells[last] = cell;
        last++;
        return true;
    }

    /**
     * Retrieves a cell from the column by its index.
     *
     * @param index The index of the cell.
     * @return The cell at the specified index.
     */
    ICell get(int index) {
        return cells[index];
    }

    /**
     * Returns a copy of the array of cells in the column.
     *
     * @return A copy of the array of cells.
     */
    public ICell[] copyCells() {
        return Arrays.copyOf(cells, cells.length);
    }

    /**
     * Returns a list of the cells in the column.
     *
     * @return A list of the cells.
     */
    public List<ICell> cellsAsList() {
        return Arrays.asList(cells);
    }

    /**
     * Returns the size of the column.
     *
     * @return The size of the column.
     */
    public int size() {
        return cells.length;
    }

    /**
     * Constructs an instance of {@code ICol} with the specified position and size.
     *
     * @param position The position of the column.
     * @param size The size of the column.
     */
    ICol(int position, int size) {
        this.position = position;
        cells = new ICell[size];
    }

}
