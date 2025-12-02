package com.regtab.core.rtl.interpreter;

import com.regtab.core.model.IRow;
import com.regtab.core.model.SubrowPos;
import com.regtab.core.model.SubtablePos;
import lombok.*;

import com.regtab.core.model.ICell;

import java.util.ArrayList;
import java.util.List;

import static com.regtab.core.rtl.interpreter.RTLPattern.CellPattern;

/**
 * Represents the matching result between a table and a compiled RTL pattern.
 * This class is final and its constructor is package-private to prevent instantiation from outside the package.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class TableMatch {
    @Getter
    private final List<SubtableMatch> subtableMatches = new ArrayList<>();

    /**
     * Adds a subtable match to this table match.
     *
     * @param match The subtable match to add.
     */
    void add(@NonNull TableMatch.SubtableMatch match) {
        subtableMatches.add(match);
    }

    /**
     * Applies the pattern to the table.
     *
     * @return True if the pattern was successfully applied to all subtable matches, false otherwise.
     */
    public boolean apply() {
        for (SubtableMatch match : subtableMatches) {
            boolean result = match.apply();
            if (!result)
                return false;
        }
        return true;
    }

    /**
     * Represents the matching result for a subtable within a table.
     * This class is static and final, and its constructor is package-private.
     */
    @NoArgsConstructor(access = AccessLevel.PACKAGE)
    public static final class SubtableMatch {

        private int topRowPosition = Integer.MAX_VALUE;
        private int bottomRowPosition = Integer.MIN_VALUE;

        @Getter
        private final List<RowMatch> rowMatches = new ArrayList<>();

        /**
         * Adds a row match to this subtable match.
         *
         * @param match The row match to add.
         */
        void add(@NonNull TableMatch.RowMatch match) {
            final IRow row = match.getRow();
            final int rowPosition = row.getPosition();

            topRowPosition = Math.min(topRowPosition, rowPosition);
            bottomRowPosition = Math.max(bottomRowPosition, rowPosition);

            rowMatches.add(match);
        }

        /**
         * Applies the pattern to the subtable.
         *
         * @return True if the pattern was successfully applied to all row matches, false otherwise.
         */
        public boolean apply() {
            for (RowMatch match : rowMatches) {
                final SubtablePos subtablePos = new SubtablePos(topRowPosition, bottomRowPosition);
                final boolean result = match.apply(subtablePos);
                if (!result)
                    return false;
            }
            return true;
        }
    }

    /**
     * Represents the matching result for a row within a subtable.
     * This class is static and final, and its constructor is package-private.
     */
    @RequiredArgsConstructor(access = AccessLevel.PACKAGE)
    public static final class RowMatch {
        @NonNull
        @Getter
        private final IRow row;

        @Getter
        private final List<SubrowMatch> subrowMatches = new ArrayList<>();

        /**
         * Adds a subrow match to this row match.
         *
         * @param match The subrow match to add.
         */
        void add(@NonNull TableMatch.SubrowMatch match) {
            subrowMatches.add(match);
        }

        @Setter(AccessLevel.PRIVATE)
        private SubtablePos subtablePos;

        /**
         * Applies the pattern to the row.
         *
         * @return True if the pattern was successfully applied to all subrow matches, false otherwise.
         */
        public boolean apply(@NonNull final SubtablePos subtablePos) {
            for (SubrowMatch match : subrowMatches) {
                final boolean result = match.apply(subtablePos);
                if (!result)
                    return false;
            }
            return true;
        }
    }

    /**
     * Represents the matching result for a subrow within a row.
     * This class is static and final, and its constructor is package-private.
     */
    @NoArgsConstructor(access = AccessLevel.PACKAGE)
    public static final class SubrowMatch {

        private int leftColPosition = Integer.MAX_VALUE;
        private int rightColPosition = Integer.MIN_VALUE;

        @Getter
        private final List<CellMatch> cellMatches = new ArrayList<>();

        /**
         * Adds a cell match to this subrow match.
         *
         * @param match The cell match to add.
         */
        void add(@NonNull TableMatch.CellMatch match) {
            final ICell cell = match.getCell();
            final int cellColPosition = cell.getCol().getPosition();

            leftColPosition = Math.min(leftColPosition, cellColPosition);
            rightColPosition = Math.max(rightColPosition, cellColPosition);

            cellMatches.add(match);
        }

        /**
         * Applies the pattern to the subrow.
         *
         * @return True if the pattern was successfully applied to all cell matches, false otherwise.
         */
        public boolean apply(@NonNull final SubtablePos subtablePos) {
            for (CellMatch match : cellMatches) {
                final SubrowPos subrowPos = new SubrowPos(leftColPosition, rightColPosition);
                final boolean result = match.apply(subtablePos, subrowPos);
                if (!result)
                    return false;
            }
            return true;
        }
    }

    /**
     * Represents the matching result for a cell within a subrow.
     * This class is static and final, and its constructor is package-private.
     */
    @RequiredArgsConstructor(access = AccessLevel.PACKAGE)
    public static final class CellMatch {

        @NonNull
        @Getter
        private final ICell cell;

        @NonNull
        @Getter
        private final CellPattern pattern;

        /**
         * Applies the pattern to the cell.
         *
         * @return True if the pattern was successfully applied to the cell, false otherwise.
         */
        public boolean apply(@NonNull final SubtablePos subtablePos, @NonNull final SubrowPos subrowPos) {
            final boolean result = pattern.apply(cell);

            if (result) {
                cell.setSubtablePos(subtablePos);
                cell.setSubrowPos(subrowPos);

                // Add the provenance
                cell.setPattern(pattern);
            }

            return result;
        }
    }

}
