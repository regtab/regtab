package com.regtab.core.model.recordset;

import com.regtab.core.model.semantics.Element;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public final class Attribute {
    @Getter
    private final String name;

    private final List<Value> values = new ArrayList<>();

    @Getter
    private Element element;

    public void addValue(@NonNull Value value) {
        values.add(value);
        value.setAttribute(this);
    }

    Attribute(@NonNull Element element) {
        if (element.getType() != Element.Type.ATTRIBUTE)
            throw new IllegalArgumentException("Недопустимый тип элемента");
        name = element.getText();
        this.element = element;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("name", name)
                .toString();
    }
}
