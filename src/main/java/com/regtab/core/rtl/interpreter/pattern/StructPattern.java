package com.regtab.core.rtl.interpreter.pattern;

import com.regtab.core.model.semantics.Action;
import com.regtab.core.model.ICell;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import com.regtab.core.rtl.parser.RTLParser.*;

public final class StructPattern extends ElementsPattern {
    public StructPattern(StructContext context) {
        super(context);
    }

    @Getter
    private final List<ElementPattern> elementTemplates = new ArrayList<>();

    public void add(@NonNull ElementPattern tmpl) {
        elementTemplates.add(tmpl);
    }

    @Getter
    @Setter
    private String startText;

    @Getter
    @Setter
    private String endText;

    @Getter
    @Setter
    private List<String> separators;

    public void add(@NonNull Action action) {
        for (ElementPattern elementTemplate : elementTemplates) {
            elementTemplate.add(action);
        }
    }

    @Override
    public boolean apply(ICell cell) {
        String text = cell.getText();
        int start = 0;
        int end = text.length();

        boolean result;

        if (startText != null) {
            result = text.startsWith(startText);
            if (!result)
                return false; // TODO log
            start = startText.length();
        }

        if (endText != null) {
            result = text.endsWith(endText);
            if (!result)
                return false; // TODO log
            end = text.length() - endText.length();
        }

        text = text.substring(start, end);

        if (separators == null) {
            if (elementTemplates.size() != 1)
                return false; // TODO log

            ElementPattern elementTemplate = elementTemplates.getFirst();
            result = elementTemplate.apply(cell, text);
            if (result == false)
                return false; // TODO log

            return true;
        }

        if (elementTemplates.size() != separators.size() + 1)
            return false; // TODO log

        //int shift = 0;
        start = 0;
        int shift = 0;
        for (int i = 0; i < elementTemplates.size(); i++) {
            ElementPattern elementTemplate = elementTemplates.get(i);

            if (i < elementTemplates.size() - 1) {
                String separator = separators.get(i);
                end = text.indexOf(separator, start);
                shift = separator.length();
            } else {
                end = text.length();
            }

            String val = text.substring(start, end);
            result = elementTemplate.apply(cell, val);
            if (result == false)
                return false; // TODO log

            start = end + shift;
        }

        return true;
    }

}
