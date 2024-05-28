package com.regtab.core.model.style;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

/**
 * The Style class represents the style of a cell in a table. It provides methods to get and set the font, horizontal
 * alignment, vertical alignment, border, background color, foreground color, indention, and rotation.
 */
public class Style {
    @Getter
    @Setter
    private Font font;

    @Getter
    @Setter
    private HAlignment hAlignment;

    @Getter
    @Setter
    private VAlignment vAlignment;

    @Getter
    @Setter
    private Border border;

    @Getter
    @Setter
    private Color bgColor;

    @Getter
    @Setter
    private Color fgColor;

    @Getter
    @Setter
    private int indention;

    @Getter
    @Setter
    private int rotation;
}
