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

    @Getter
    private final String string;

//    @Getter
//    private Element element;

    Value(@NonNull String string) {
        this.string = string;
    }

//    Value(@NonNull Element element) {
//        if (element.getType() != Element.Type.VALUE)
//            throw new IllegalArgumentException("Invalid element type");
//        string = element.getText();
//        this.element = element;
//    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("attribute", attribute)
                .append("string", string)
                .toString();
    }

}
