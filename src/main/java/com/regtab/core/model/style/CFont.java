package com.regtab.core.model.style;

import lombok.Getter;
import lombok.Setter;

public final class CFont {
    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private int size;

    @Getter
    @Setter
    private boolean bold;

    @Getter
    @Setter
    private boolean italic;

    @Getter
    @Setter
    private boolean strikeout;

    @Getter
    @Setter
    private boolean underline;

    @Getter
    @Setter
    private boolean doubleUnderline;
}
