package com.regtab.core.model.style;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class HAlignment {
    private final boolean left;
    private final boolean center;
    private final boolean right;
    private final boolean justify;

    public static final HAlignment LEFT;
    public static final HAlignment CENTER;
    public static final HAlignment RIGHT;
    public static final HAlignment JUSTIFY;

    static {
        LEFT = new HAlignment(true, false, false, false);
        CENTER = new HAlignment(false, true, false, false);
        RIGHT = new HAlignment(false, false, true, false);
        JUSTIFY = new HAlignment(false, false, false, true);
    }
}

