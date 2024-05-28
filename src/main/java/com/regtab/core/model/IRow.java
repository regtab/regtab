package com.regtab.core.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Arrays;
import java.util.List;

/**
 * The IRow class represents a row in a table. It contains an array of cells and provides
 * methods to add cells, retrieve cells, and get the row size.
 */
@RequiredArgsConstructor
public final class IRow {
    @Getter
    private final int position;

    private final ICell[] cells;

    private int last;

    /**
     * Adds a cell to the row.
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
     * Retrieves a cell from the row by its index.
     *
     * @param index The index of the cell.
     * @return The cell at the specified index.
     */
    ICell get(int index) {
        return cells[index];
    }

    /**
     * Returns a copy of the array of cells in the row.
     *
     * @return A copy of the array of cells.
     */
    public ICell[] copyCells() {
        return Arrays.copyOf(cells, cells.length);
    }

    /**
     * Returns a list of the cells in the row.
     *
     * @return A list of the cells.
     */
    public List<ICell> cellsAsList() {
        return Arrays.asList(cells);
    }

    /**
     * Returns the size of the row.
     *
     * @return The size of the row.
     */
    public int size() {
        return cells.length;
    }

    /**
     * Constructs an instance of {@code IRow} with the specified position and size.
     *
     * @param position The position of the row.
     * @param size The size of the row.
     */
    IRow(int position, int size) {
        this.position = position;
        cells = new ICell[size];
    }

    /**
     * Returns a string representation of the row.
     *
     * @return A string representation of the row.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("position", position)
                .append("cells", Arrays.toString(cells))
                .toString();
    }
}
