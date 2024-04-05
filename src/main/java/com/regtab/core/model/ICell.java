package com.regtab.core.model;

import com.regtab.core.model.format.HtmlTag;
import com.regtab.core.model.format.SSDatatype;
import com.regtab.core.model.style.Style;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public final class ICell {
    @Getter
    private final ITable table;

    @Getter
    private final IRow row;

    @Getter
    private final ICol col;

    @Getter
    private String text;

    public int r() {
        return row.getPosition();
    }

    public int c() {
        return col.getPosition();
    }

    @Getter
    private final List<Element> elements = new ArrayList<>();

    public List<Element> elements() {
        return elements.isEmpty() ? null : new ArrayList<>(elements);
    }

    void perform(@NonNull Action.Type type, @NonNull Recordset recordset) {
        for (Element element : elements)
            element.perform(type, recordset);
    }

    public Element createElement(@NonNull Element.Type type, @NonNull String text) {
        Element element = new Element(this, type, text.trim());
        elements.add(element);

        return element;
    }

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
    private boolean bordered;

    @Getter
    @Setter
    private HtmlTag htmlTag;

    @Getter
    @Setter
    private SSDatatype datatype;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                //.append("r", r())
                //.append("c", c())
                .append("text", text)
                .toString();
    }

}
