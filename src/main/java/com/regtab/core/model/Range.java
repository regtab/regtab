package com.regtab.core.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

public record Range(int start, int end) {
    public static Range intersection(Range range1, Range range2) {
        final int start = Math.max(range1.start, range2.start);
        final int end = Math.min(range1.end, range2.end);
        return new Range(start, end);
    }

    @RequiredArgsConstructor
    public static final class Desc {
        @Getter
        private final boolean rowwise;

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

        Range createRange(@NonNull ICell cell) {
            final int position = rowwise ? cell.r() : cell.c();
            return createRange(position);
        }

        private Range createRange(int position) {
            final int start = useRelativeStart ? position + this.start : this.start;
            final int end = useRelativeEnd ? position + this.end : this.end;
            return new Range(start, end);
        }
    }
}
