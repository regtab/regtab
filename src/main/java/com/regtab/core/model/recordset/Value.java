package com.regtab.core.model.recordset;

import com.regtab.core.model.semantics.Element;
import com.regtab.core.model.semantics.Provenance;
import lombok.*;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@EqualsAndHashCode
public final class Value {
    @Getter
    private Attribute attribute;

    @Getter
    @Setter(AccessLevel.PACKAGE)
    private Group group;

    @Getter
    private final String string;

    @Getter
    private Provenance provenance;

    public void setAttribute(@NonNull Attribute attribute) {
        this.attribute = attribute;
    }

    Value(@NonNull String string) {
        this.string = string;
        //provenance = element; // провенанс надо брать из action
    }

    Value(@NonNull Element element) {
        if (element.getType() != Element.Type.VALUE)
            throw new IllegalArgumentException("Недопустимый тип элемента");
        string = element.getText();
        provenance = element;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("attribute", attribute)
                .append("data", string)
                .append("provenance", provenance)
                .toString();
    }
}
