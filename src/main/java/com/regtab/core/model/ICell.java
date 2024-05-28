package com.regtab.core.model;

import com.regtab.core.model.format.HtmlTag;
import com.regtab.core.model.format.SSDatatype;
import com.regtab.core.model.style.Style;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * The ICell class represents a cell in a table. It contains the text content of the cell,
 * as well as other properties such as style, indentation, and formatting information.
 */
public final class ICell {
    @Getter
    private final ITable table;

    @Getter
    private final IRow row;

    @Getter
    private final ICol col;

    @Getter
    private final String text;

    /**
     * Attempts to parse the text content of the cell as an integer.
     *
     * @return The parsed integer, or null if the text cannot be parsed as an integer.
     */
    public Integer asInteger() {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Attempts to parse the text content of the cell as a double.
     *
     * @return The parsed double, or null if the text cannot be parsed as a double.
     */
    public Double asDouble() {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Returns the row position of the cell.
     *
     * @return The row position.
     */
    public int r() {
        return row.getPosition();
    }

    /**
     * Returns the column position of the cell.
     *
     * @return The column position.
     */
    public int c() {
        return col.getPosition();
    }

    @Getter(AccessLevel.PACKAGE)
    private final List<Element> elements = new ArrayList<>();

    /**
     * Returns a list of elements associated with the cell.
     *
     * @return The list of elements, or null if the cell has no elements.
     */
    public List<Element> elements() {
        return elements.isEmpty() ? null : new ArrayList<>(elements);
    }

    /**
     * Performs an action on all elements associated with the cell.
     *
     * @param type The type of action to perform.
     * @param recordset The recordset to use for the action.
     */
    void perform(@NonNull Action.Type type, @NonNull Recordset recordset) {
        for (Element element : elements)
            element.perform(type, recordset);
    }

    /**
     * Creates a new element associated with the cell.
     *
     * @param type The type of the element.
     * @param text The text content of the element.
     * @return The created element.
     */
    public Element createElement(@NonNull Element.Type type, @NonNull String text) {
        Element element = new Element(this, type, text.trim());
        elements.add(element);

        return element;
    }

    /**
     * Constructs an instance of ICell with the specified table, row, column, and text.
     *
     * @param table The table that this cell belongs to.
     * @param row The row that this cell belongs to.
     * @param col The column that this cell belongs to.
     * @param text The text content of this cell.
     * @throws NullPointerException if any of the parameters are null.
     */
    ICell(@NonNull ITable table, @NonNull IRow row, @NonNull ICol col, @NonNull String text) {
        this.table = table;
        this.row = row;
        this.col = col;
        this.text = text;
    }

    @Getter
    @Setter
    private Style style;

    @Getter
    @Setter
    private int indent;

    @Getter
    @Setter
    private boolean blank;

    @Getter
    @Setter
    private boolean merged;

    @Getter
    @Setter
    private boolean hidden;

    @Getter
    @Setter
    private boolean bordered;

    @Getter
    @Setter
    private HtmlTag htmlTag;

    @Getter
    @Setter
    private SSDatatype datatype;

    /**
     * Returns a string representation of the cell.
     *
     * @return A string representation of the cell.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                //.append("r", r())
                //.append("c", c())
                .append("text", text)
                .toString();
    }

    /**
     * Clears all elements associated with the cell.
     */
    void clear() {
        elements.clear();
    }

}
