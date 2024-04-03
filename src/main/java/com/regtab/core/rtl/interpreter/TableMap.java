package com.regtab.core.rtl.interpreter;

import lombok.*;

import com.regtab.core.model.ICell;

import java.util.ArrayList;
import java.util.List;

import static com.regtab.core.rtl.interpreter.RTLPattern.CellPattern;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class TableMap {
    @Getter
    private final List<SubtableMap> subtableMaps = new ArrayList<>();

    void add(@NonNull SubtableMap map) {
        subtableMaps.add(map);
    }

    public boolean apply() {
        for (SubtableMap map : subtableMaps) {
            boolean result = map.apply();
            if (!result)
                return false;
        }
        return true;
    }

    @NoArgsConstructor(access = AccessLevel.PACKAGE)
    public static final class SubtableMap {
        @Getter
        private final List<RowMap> rowMaps = new ArrayList<>();

        void add(@NonNull RowMap map) {
            rowMaps.add(map);
        }

        public boolean apply() {
            for (RowMap map : rowMaps) {
                boolean result = map.apply();
                if (!result)
                    return false;
            }
            return true;
        }
    }

    @NoArgsConstructor(access = AccessLevel.PACKAGE)
    public static final class RowMap {
        @Getter
        private final List<SubrowMap> subrowMaps = new ArrayList<>();

        void add(@NonNull SubrowMap map) {
            subrowMaps.add(map);
        }

        public boolean apply() {
            for (SubrowMap map : subrowMaps) {
                boolean result = map.apply();
                if (!result)
                    return false;
            }
            return true;
        }
    }

    @NoArgsConstructor(access = AccessLevel.PACKAGE)
    public static final class SubrowMap {
        @Getter
        private final List<CellMap> cellMaps = new ArrayList<>();

        void add(@NonNull CellMap map) {
            cellMaps.add(map);
        }

        public boolean apply() {
            for (CellMap map : cellMaps) {
                boolean result = map.apply();
                if (!result)
                    return false;
            }
            return true;
        }
    }

    @RequiredArgsConstructor(access = AccessLevel.PACKAGE)
    public static final class CellMap {
        @NonNull
        @Getter
        private final ICell cell;

        @NonNull
        @Getter
        private final CellPattern pattern;

        public boolean apply() {
            return pattern.apply(cell);
        }
    }

}
