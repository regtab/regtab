package com.regtab.core.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * The Range class represents a range of integers. It provides methods to create an intersection of two ranges
 * and a nested Desc class to describe a range based on a cell's position.
 */
public record Range(int start, int end) {
    /**
     * Calculates the intersection of two ranges.
     *
     * @param range1 The first range.
     * @param range2 The second range.
     * @return The intersection of the two ranges.
     */
    public static Range intersection(Range range1, Range range2) {
        final int start = Math.max(range1.start, range2.start);
        final int end = Math.min(range1.end, range2.end);
        return new Range(start, end);
    }

    /**
     * The Desc class describes a range based on a cell's position. It is used to create a range relative to a cell.
     */
    @RequiredArgsConstructor
    public static final class Desc {
        @Getter
        private final boolean rowwise;

        @Getter
        @Setter
        private Integer start;

        @Getter
        @Setter
        private boolean useRelativeStart;

        @Getter
        @Setter
        private Integer end;

        @Getter
        @Setter
        private boolean useRelativeEnd;

        @Getter
        @Setter
        private boolean useMinStart;

        @Getter
        @Setter
        private boolean useMaxEnd;

        /**
         * Creates a range based on the cell's position.
         *
         * @param cell The cell from which to create the range.
         * @return The created range.
         */
        Range createRange(@NonNull ICell cell) {

            final int start;
            final int end;

            if (useMinStart) {
                start = 0; // TODO необходимо учесть позиции подтаблицы/подстроки
            } else if (useRelativeStart) {
                final int position = rowwise ? cell.r() : cell.c();
                start = position + this.start;
            }
            else {
                start = this.start;
            }

            if (useMaxEnd) {
                final int size = rowwise ? cell.getTable().rowsSize() : cell.getTable().colsSize();
                end = size - 1; // TODO необходимо учесть позиции подтаблицы/подстроки
            } else if (useRelativeEnd) {
                final int position = rowwise ? cell.r() : cell.c();
                end = position + this.end;
            } else {
                end = this.end;
            }

            return new Range(start, end);
        }

//        private Range createRange(int position) {
//            final int start;
//            final int end;
//
//            if (useMinStart) {
//                start = 0;
//            }
//
//            if (useMaxEnd) {
//                start = ;
//            }
//
//            start = useMinStart ? 0 : this.start;
//            end = useMaxEnd ?  : this.end;
//
//            start = useRelativeStart ? position + this.start : this.start;
//            end = useRelativeEnd ? position + this.end : this.end;
//            return new Range(start, end);
//        }
    }
}
