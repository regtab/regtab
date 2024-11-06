package com.regtab.core.rtl.interpreter;

import lombok.*;

import com.regtab.core.model.ICell;

import java.util.ArrayList;
import java.util.List;

import static com.regtab.core.rtl.interpreter.RTLPattern.CellPattern;

/**
 * Represents the mapping between a table and a compiled RTL pattern.
 * This class is final and its constructor is package-private to prevent instantiation from outside the package.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class TableMap {
    @Getter
    private final List<SubtableMap> subtableMaps = new ArrayList<>();

    /**
     * Adds a subtable map to this table map.
     *
     * @param map The subtable map to add.
     */
    void add(@NonNull SubtableMap map) {
        subtableMaps.add(map);
    }

    /**
     * Applies the pattern to the table.
     *
     * @return True if the pattern was successfully applied to all subtable maps, false otherwise.
     */
    public boolean apply() {
        for (SubtableMap map : subtableMaps) {
            boolean result = map.apply();
            if (!result)
                return false;
        }
        return true;
    }

    /**
     * Represents the mapping for a subtable within a table.
     * This class is static and final, and its constructor is package-private.
     */
    @NoArgsConstructor(access = AccessLevel.PACKAGE)
    public static final class SubtableMap {
        @Getter
        private final List<RowMap> rowMaps = new ArrayList<>();

        /**
         * Adds a row map to this subtable map.
         *
         * @param map The row map to add.
         */
        void add(@NonNull RowMap map) {
            rowMaps.add(map);
        }

        /**
         * Applies the pattern to the subtable.
         *
         * @return True if the pattern was successfully applied to all row maps, false otherwise.
         */
        public boolean apply() {
            for (RowMap map : rowMaps) {
                boolean result = map.apply();
                if (!result)
                    return false;
            }
            return true;
        }
    }

    /**
     * Represents the mapping for a row within a subtable.
     * This class is static and final, and its constructor is package-private.
     */
    @NoArgsConstructor(access = AccessLevel.PACKAGE)
    public static final class RowMap {
        @Getter
        private final List<SubrowMap> subrowMaps = new ArrayList<>();

        /**
         * Adds a subrow map to this row map.
         *
         * @param map The subrow map to add.
         */
        void add(@NonNull SubrowMap map) {
            subrowMaps.add(map);
        }

        /**
         * Applies the pattern to the row.
         *
         * @return True if the pattern was successfully applied to all subrow maps, false otherwise.
         */
        public boolean apply() {
            for (SubrowMap map : subrowMaps) {
                boolean result = map.apply();
                if (!result)
                    return false;
            }
            return true;
        }
    }

    /**
     * Represents the mapping for a subrow within a row.
     * This class is static and final, and its constructor is package-private.
     */
    @NoArgsConstructor(access = AccessLevel.PACKAGE)
    public static final class SubrowMap {
        @Getter
        private final List<CellMap> cellMaps = new ArrayList<>();

        /**
         * Adds a cell map to this subrow map.
         *
         * @param map The cell map to add.
         */
        void add(@NonNull CellMap map) {
            cellMaps.add(map);
        }

        /**
         * Applies the pattern to the subrow.
         *
         * @return True if the pattern was successfully applied to all cell maps, false otherwise.
         */
        public boolean apply() {
            for (CellMap map : cellMaps) {
                boolean result = map.apply();
                if (!result)
                    return false;
            }
            return true;
        }
    }

    /**
     * Represents the mapping for a cell within a subrow.
     * This class is static and final, and its constructor is package-private.
     */
    @RequiredArgsConstructor(access = AccessLevel.PACKAGE)
    public static final class CellMap {
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
        public boolean apply() {
            return pattern.apply(cell);
        }
    }

}
