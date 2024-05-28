package com.regtab.core.model.style;

import lombok.Getter;

/**
 * The Border class represents the border style of a cell in a table. It provides methods to check if each side of
 * the border is present.
 */
@Getter
public final class Border {
    private final boolean left;
    private final boolean top;
    private final boolean right;
    private final boolean bottom;

    /**
     * Constructs a new Border with the specified presence of each side.
     *
     * @param left   True if the left border is present, false otherwise.
     * @param top    True if the top border is present, false otherwise.
     * @param right  True if the right border is present, false otherwise.
     * @param bottom True if the bottom border is present, false otherwise.
     */
    public Border(boolean left, boolean top, boolean right, boolean bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }
}
