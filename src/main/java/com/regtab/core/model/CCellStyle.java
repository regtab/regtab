package com.regtab.core.model;

import lombok.Getter;
import lombok.Setter;

public class CCellStyle {
    @Getter
    @Setter
    private CFont font;

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
    private CColor bgColor;

    @Getter
    @Setter
    private CColor fgColor;

    @Getter
    @Setter
    private int indention;

    @Getter
    @Setter
    private int rotation;
}
