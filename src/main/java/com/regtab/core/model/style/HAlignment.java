package com.regtab.core.model.style;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The HAlignment class represents the horizontal alignment style of a cell in a table. It provides static instances
 * for left, center, right, and justify alignments.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class HAlignment {
    private final boolean left;
    private final boolean center;
    private final boolean right;
    private final boolean justify;

    /**
     * Represents a left horizontal alignment.
     */
    public static final HAlignment LEFT;

    /**
     * Represents a center horizontal alignment.
     */
    public static final HAlignment CENTER;

    /**
     * Represents a right horizontal alignment.
     */
    public static final HAlignment RIGHT;

    /**
     * Represents a justify horizontal alignment.
     */
    public static final HAlignment JUSTIFY;

    static {
        LEFT = new HAlignment(true, false, false, false);
        CENTER = new HAlignment(false, true, false, false);
        RIGHT = new HAlignment(false, false, true, false);
        JUSTIFY = new HAlignment(false, false, false, true);
    }
}

