package com.regtab.core.model;

import com.regtab.core.model.semantics.Action;
import com.regtab.core.model.semantics.Element;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public final class ILine {
    @Getter
    private final ICell cell;

    @Getter
    private final String text;

    public ILine(@NonNull ICell cell, String text) {
        this.cell = cell;
        if (text == null || text.isBlank()) {
            this.text = "";
        } else {
            this.text = text;
        }
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
}
