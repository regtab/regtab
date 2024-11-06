package com.regtab.core.model.style;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The VAlignment class represents the vertical alignment style of a cell in a table. It provides static instances
 * for top, center, bottom, and justify alignments.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class VAlignment {
    private final boolean top;
    private final boolean center;
    private final boolean bottom;
    private final boolean justify;

    /**
     * Represents a top vertical alignment.
     */
    public static final VAlignment TOP;

    /**
     * Represents a center vertical alignment.
     */
    public static final VAlignment CENTER;

    /**
     * Represents a bottom vertical alignment.
     */
    public static final VAlignment BOTTOM;

    /**
     * Represents a justify vertical alignment.
     */
    public static final VAlignment JUSTIFY;

    static {
        TOP = new VAlignment(true, false, false, false);
        CENTER = new VAlignment(false, true, false, false);
        BOTTOM = new VAlignment(false, false, true, false);
        JUSTIFY = new VAlignment(false, false, false, true);
    }
}
