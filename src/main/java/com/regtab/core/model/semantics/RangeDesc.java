package com.regtab.core.model.semantics;

import com.regtab.core.model.ICell;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public final class RangeDesc {
    @Getter
    private final Type type;

    @Getter
    @Setter
    private int start;

    @Getter
    @Setter
    private boolean useRelativeStart;

    @Getter
    @Setter
    private int end;

    @Getter
    @Setter
    private boolean useRelativeEnd;

    public Range createRange(ICell cell) {
        final int position = type == Type.ROW ? cell.r() : cell.c();
        return createRange(position);
    }

    private Range createRange(int position) {
        final int start = useRelativeStart ? position + this.start : this.start;
        final int end = useRelativeEnd ? position + this.end : this.end;
        return new Range(start, end);
    }

    public enum Type {
        ROW, COL
    }

}
