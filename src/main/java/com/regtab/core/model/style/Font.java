package com.regtab.core.model.style;

import lombok.Getter;
import lombok.Setter;

/**
 * The Font class represents the font style of a cell in a table. It provides methods to get and set the font name,
 * size, and various font styles such as bold, italic, strikeout, underline, and double underline.
 */
public final class Font {
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
