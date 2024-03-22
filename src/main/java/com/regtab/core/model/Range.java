package com.regtab.core.model;

public record Range(int from, int to) {
    public static Range intersection(Range range1, Range range2) {
        int from = Math.max(range1.from, range2.from);
        int to = Math.min(range1.to, range2.to);
        return new Range(from, to);
    }
}
