package com.regtab.core.rtl.interpreter.pattern;

import com.regtab.core.model.ILine;
import com.regtab.core.model.semantics.Action;
import com.regtab.core.model.ICell;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.regtab.core.rtl.parser.RTLParser.*;

public final class StructPattern extends ElementsPattern {
    public StructPattern(StructContext context) {
        super(context);
    }

    @Getter
    private final List<ElementPattern> elementPatterns = new ArrayList<>();

    public void add(@NonNull ElementPattern tmpl) {
        elementPatterns.add(tmpl);
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
        for (ElementPattern elementPattern : elementPatterns) {
            elementPattern.add(action);
        }
    }

    @Override
    public boolean apply(@NonNull ICell cell) {
        final List<ILine> lines = cell.getLines();
        for (ILine line : lines) {
            boolean result = apply(line);
            if (!result)
                return false; // TODO log
        }

        return true;
    }

    public boolean apply(@NonNull ILine line) {
        final String text = line.getText();
        if (text.isBlank())
            return false; // TODO log

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

            final String subText = text.substring(start, end);

            // Если нет разделителей, то есть только один элемент
            if (separators == null) {
                if (elementPatterns.size() != 1)
                    return false; // TODO log

                final ElementPattern elementPattern = elementPatterns.getFirst();
                final String val = subText;
                result = elementPattern.apply(line, val);
                if (result == false)
                    return false; // TODO log

                return true;
            }

            // Если есть n разделителей, то есть n-1 элементов
            if (elementPatterns.size() != separators.size() + 1)
                return false; // TODO log

            start = 0;
            int shift = 0;
            for (int i = 0; i < elementPatterns.size(); i++) {
                final ElementPattern elementPattern = elementPatterns.get(i);

                if (i < elementPatterns.size() - 1) {
                    String separator = separators.get(i);
                    end = subText.indexOf(separator, start);
                    shift = separator.length();
                } else {
                    end = subText.length();
                }

                String val = subText.substring(start, end);
                result = elementPattern.apply(line, val);
                if (result == false)
                    return false; // TODO log

                start = end + shift;
            }

        return true;
    }

}
