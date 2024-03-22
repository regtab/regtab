package com.regtab.core.model.recordset;

import com.regtab.core.model.semantics.Element;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;

@EqualsAndHashCode
public final class Value {
    @Getter
    private Attribute attribute;

    @Getter @Setter
    private Group group;

    @Getter
    private final String data;

    @Getter
    private Provenance provenance;

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    Value(String str) {
        data = str;
        //provenance = element; // провенанс надо брать из action
    }

    Value(Element element) {
        if (element.getType() != Element.Type.VALUE)
            throw new IllegalArgumentException("Недопустимый тип элемента");
        data = element.getData();
        provenance = element;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("attribute", attribute)
                .append("data", data)
                .append("provenance", provenance)
                .toString();
    }
}
