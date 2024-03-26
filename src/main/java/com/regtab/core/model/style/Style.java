package com.regtab.core.model.style;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

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
