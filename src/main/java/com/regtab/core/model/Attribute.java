package com.regtab.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@EqualsAndHashCode
public final class Attribute {
    @Getter
    private final String name;

    private final List<Value> values = new ArrayList<>();

    private Provenance provenance;

    public void addValue(Value value) {
        values.add(value);
        value.setAttribute(this);
    }

    Attribute(@NonNull Element element) {
        if (element.getType() != Element.Type.ATTRIBUTE)
            throw new IllegalArgumentException("Недопустимый тип элемента");
        name = element.getData();
        provenance = element;
    }

    Attribute(@NonNull String name, @NonNull  Group group) {
        this(name);
        group.fillAttribute(this);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("name", name)
                //.append("values", values)
                .append("provenance", provenance)
                .toString();
    }
}
