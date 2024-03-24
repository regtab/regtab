package com.regtab.core.model;

import com.regtab.core.model.format.HtmlTag;
import com.regtab.core.model.format.SSDatatype;
import com.regtab.core.model.semantics.Action;
import com.regtab.core.model.semantics.Element;
import com.regtab.core.model.style.Style;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public final class ICell {
    @NonNull
    @Getter
    private final ITable table;

    @NonNull
    @Getter
    private final IRow row;

    @NonNull
    @Getter
    private final ICol col;

    @NonNull
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

    void perform(Action.Type type) {
        for (Element element : elements)
            element.perform(type);
    }

    public Element createElement(@NonNull Element.Type type, @NonNull String data) {
        Element element = new Element(this, type, data.trim());
        elements.add(element);

        return element;
    }

    public Element createElement(@NonNull Element.Type type) {
        Element element = new Element(this, type, text);
        elements.add(element);

        return element;
    }

    ICell(ITable table, IRow row, ICol col, String text) {
        this.table = table;
        this.row = row;
        this.col = col;
        this.text = text;
    }

//    private int getIndent(String text) {
//        if (!text.isBlank()) {
//            for (int i = 0; i < text.length(); i++) {
//                char c = text.charAt(i);
//                if (c != 32) return i;
//            }
//        }
//        return 0;
//    }

//    public Element createElement(@NonNull Element.Type type, @NonNull String data) {
//        Element element = new Element(this, type, data.trim());
//        elements.add(element);
//
//        return element;
//    }
//
//    public Element createElement(@NonNull Element.Type type) {
//        Element element = new Element(this, type, text);
//        elements.add(element);
//
//        return element;
//    }

    @Getter
    @Setter
    private Style style;

    @Getter
    @Setter
    private int indent; // TODO убрать в line

    @Getter
    @Setter
    private boolean blank;

    @Getter
    @Setter
    private boolean merged;

    @Getter
    @Setter
    private boolean nested;

    @Getter
    @Setter
    private boolean spanning;

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
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                //.append("r", r())
                //.append("c", c())
                .append("text", text)
                .toString();
    }

}
