package com.regtab.core.model.recordset;

import com.regtab.core.model.semantics.Element;
import lombok.*;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public final class Value {
    @NonNull
    @Getter
    @Setter
    private Attribute attribute;

    @NonNull
    @Getter
    private final String string;

    @Getter
    private final Element provenance;

    Value(@NonNull String string, Element provenance) {
        this.string = string;
        if (provenance != null && provenance.getType() != Element.Type.VALUE)
            throw new IllegalArgumentException("Invalid element type");
        this.provenance = provenance;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("attribute", attribute)
                .append("string", string)
                .toString();
    }

}
