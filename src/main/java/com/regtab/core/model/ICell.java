package com.regtab.core.model;

import com.regtab.core.model.format.HtmlTag;
import com.regtab.core.model.format.SSDatatype;
import com.regtab.core.model.semantics.Action;
import com.regtab.core.model.semantics.Element;
import com.regtab.core.model.style.Style;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

    //private ILine[] lines = new ILine[]{new ILine("")};
    @Getter
    private final List<ILine> lines = new ArrayList<>(1);

    private void setText(String text, boolean multiline) {
        lines.clear();
        if (text == null || text.isBlank()) {
            this.text = "";
            //this.indent = 0;
            lines.add(new ILine(this, ""));
            //this.blank = true;
        } else {
            if (multiline) {
                this.text = text;
                final Scanner scanner = new Scanner(text);
                do {
                   final String lineText = scanner.nextLine();
                   if (lineText.isBlank())
                       continue;
                   lines.add(new ILine(this, lineText));
                } while (scanner.hasNext());

                if (lines.isEmpty())
                    throw new IllegalArgumentException("No lines");
            } else {
                this.text = text;
                lines.add(new ILine(this, text));
                //this.indent = getIndent(text);
            }
            //this.blank = false;
        }
    }

//    public void setText(String text) {
//        if (text == null || text.isBlank()) {
//            this.text = "";
//            this.blank = true;
//            this.indent = 0;
//        } else {
//            this.text = text;
//            this.blank = false;
//            this.indent = getIndent(text);
//        }
//    }

//    private void setLines(String... lines) {
//        if (lines == null || lines.length == 0) {
//            this.lines = new ILine[]{new ILine("")};
//        } else if (lines.length == 1) {
//            this.lines = new ILine[]{new ILine(lines[0])};
//        } else {
//            this.lines = new ILine[lines.length];
//            for (int i = 0; i < lines.length; i++) {
//                String line = lines[i];
//                this.lines[i] = new ILine(line);
//            }
//        }
//    }

//    private ILine createLine(String text) {
//        if (text == null || text.isBlank())
//            text = "";
//
//        final ILine line = new ILine(this, text);
//        lines.add(line);
//
//        return line;
//    }

//    @Getter
//    private final List<Element> elements = new ArrayList<>();
//
//    public List<Element> elements() {
//        return elements.isEmpty() ? null : new ArrayList<>(elements);
//    }

//    public void addElement(Element element) {
//        elements.add(element);
//    }

    void perform(Action.Type type) {

        for (ILine line : lines) {
            final List<Element> elements = line.getElements();
            for (Element element : elements)
                element.perform(type);
        }
    }

    ICell(ITable table, IRow row, ICol col, String text, boolean multiline) {
        this.table = table;
        this.row = row;
        this.col = col;
        setText(text, multiline);
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
