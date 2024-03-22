package com.regtab.core.model.style;

import lombok.Getter;

@Getter
public final class Border {
    private final boolean left;
    private final boolean top;
    private final boolean right;
    private final boolean bottom;

    public Border(boolean left, boolean top, boolean right, boolean bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }
}
