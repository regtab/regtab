package com.regtab.core.model;

import lombok.Getter;
import lombok.NonNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public final class Attribute {
    @Getter
    private final String name;

    @Getter
    private final Element provenance;

    Attribute(@NonNull String name, Element provenance) {
        this.name = name;
        if (provenance != null && provenance.getType() != Element.Type.ATTRIBUTE)
            throw new IllegalArgumentException("Invalid element type");
        this.provenance = provenance;
    }

    private final List<Value> values = new ArrayList<>();

    void addValue(@NonNull Value value) {
        values.add(value);
        value.setAttribute(this);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("name", name)
                .toString();
    }
}
