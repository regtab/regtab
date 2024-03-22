package com.regtab.core.rtl.interpreter;

import com.regtab.core.model.ICell;
import com.regtab.core.rtl.interpreter.pattern.CellPattern;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public final class TableMap {
    @Getter
    private List<SubtableMap> subtableMaps = new ArrayList<>();

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

    @RequiredArgsConstructor
    public static final class CellMap {
        @NonNull
        @Getter
        private final ICell cell;
        @NonNull
        @Getter
        private final CellPattern template;
        public boolean apply() {
            return template.apply(cell);
        }
    }

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

}
