package com.regtab.core.model.style;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class VAlignment {
    private final boolean top;
    private final boolean center;
    private final boolean bottom;
    private final boolean justify;

    public static final VAlignment TOP;
    public static final VAlignment CENTER;
    public static final VAlignment BOTTOM;
    public static final VAlignment JUSTIFY;

    static {
        TOP = new VAlignment(true, false, false, false);
        CENTER = new VAlignment(false, true, false, false);
        BOTTOM = new VAlignment(false, false, true, false);
        JUSTIFY = new VAlignment(false, false, false, true);
    }
}
