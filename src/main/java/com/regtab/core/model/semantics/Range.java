package com.regtab.core.model.semantics;

public record Range(int start, int end) {
    public static Range intersection(Range range1, Range range2) {
        final int start = Math.max(range1.start, range2.start);
        final int end = Math.min(range1.end, range2.end);
        return new Range(start, end);
    }
}
