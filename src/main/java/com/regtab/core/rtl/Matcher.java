package com.regtab.core.rtl;

import com.regtab.core.model.semantics.Condition;
import lombok.NonNull;
import lombok.extern.java.Log;

import com.regtab.core.model.*;
import com.regtab.core.rtl.interpreter.pattern.*;
import com.regtab.core.rtl.interpreter.visitor.Quantifier;

import static com.regtab.core.rtl.TableMap.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Log
public class Matcher {
    @NonNull
    private final Pattern pattern;

    public Matcher(@NonNull Pattern pattern) {
        this.pattern = pattern;
    }

    //private TableMap tableMap = new TableMap()

    public TableMap match(@NonNull ITable table) {
        final TablePattern tablePattern = pattern.getTablePattern();
        return match(table, tablePattern);
    }

    private TableMap match(ITable table, TablePattern tmpl) {
        final TableMap tableMap = new TableMap();

        final Queue<IRow> rows = new LinkedList<>(table.rowsAsList());

        final List<SubtablePattern> subtablePatterns = tmpl.getSubtablePatterns();
        for (SubtablePattern subtablePattern : subtablePatterns) {
            int repetitionCount = 0;

            final Quantifier quantifier = subtablePattern.getQuantifier();
            final Quantifier.Times times = quantifier.times();

            if (times == Quantifier.Times.UNDEFINED) {
                SubtableMap map = match(rows, subtablePattern);
                if (map == null)
                    return null;

                tableMap.add(map);
                repetitionCount++;

                subtablePattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (times == Quantifier.Times.EXACTLY) {
                final int exactly = quantifier.exactly();
                for (int i = 0; i < exactly; i++) {
                    SubtableMap map = match(rows, subtablePattern);
                    if (map == null)
                        return null;

                    tableMap.add(map);
                    repetitionCount++;
                }

                subtablePattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (times == Quantifier.Times.ZERO_OR_ONE) {
                SubtableMap map = match(rows, subtablePattern);
                if (map != null) {
                    tableMap.add(map);
                    repetitionCount++;
                }

                subtablePattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (times == Quantifier.Times.ZERO_OR_MORE) {
                while (true) {
                    SubtableMap map = match(rows, subtablePattern);
                    if (map == null)
                        break;

                    tableMap.add(map);
                    repetitionCount++;
                }

                subtablePattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (quantifier.times() == Quantifier.Times.ONE_OR_MORE) {
                SubtableMap map = match(rows, subtablePattern);
                if (map == null)
                    return null;

                tableMap.add(map);
                repetitionCount++;

                while (true) {
                    map = match(rows, subtablePattern);
                    if (map == null)
                        break;

                    tableMap.add(map);
                    repetitionCount++;
                }

                subtablePattern.setRepetitionCount(repetitionCount);
            }
        }

        return tableMap;
    }

    private SubtableMap match(Queue<IRow> rows, SubtablePattern tmpl) {
        if (rows.isEmpty())
            return null;

        final SubtableMap subtableMap = new SubtableMap();

        final List<RowPattern> rowPatterns = tmpl.getRowPatterns();
        for (RowPattern rowPattern : rowPatterns) {
            int repetitionCount = 0;

            final Quantifier quantifier = rowPattern.getQuantifier();
            final Quantifier.Times times = quantifier.times();

            if (times == Quantifier.Times.UNDEFINED) {
                IRow row = rows.poll();
                if (row == null)
                    return null; // Impossible

                RowMap map = match(row, rowPattern);
                if (map == null)
                    return null;

                subtableMap.add(map);
                repetitionCount++;

                rowPattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (times == Quantifier.Times.EXACTLY) {
                final int exactly = quantifier.exactly();
                for (int i = 0; i < exactly; i++) {
                    if (rows.isEmpty())
                        return null;
                    IRow row = rows.peek();
                    RowMap map = match(row, rowPattern);
                    if (map == null)
                        return null;

                    rows.poll();
                    subtableMap.add(map);
                    repetitionCount++;
                }

                rowPattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (times == Quantifier.Times.ZERO_OR_ONE) {
                if (!rows.isEmpty()) {
                    final IRow row = rows.peek();
                    RowMap map = match(row, rowPattern);
                    if (map != null) {
                        rows.poll();
                        subtableMap.add(map);
                        repetitionCount++;
                    }
                }

                rowPattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (times == Quantifier.Times.ZERO_OR_MORE) {
                while (true) {
                    if (rows.isEmpty())
                        break;
                    IRow row = rows.peek();
                    RowMap map = match(row, rowPattern);
                    if (map == null)
                        break;

                    rows.poll();
                    subtableMap.add(map);
                    repetitionCount++;
                }

                rowPattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (quantifier.times() == Quantifier.Times.ONE_OR_MORE) {
                if (rows.isEmpty())
                    return null;

                IRow row = rows.peek();
                RowMap map = match(row, rowPattern);
                if (map == null)
                    return null;

                rows.poll();
                subtableMap.add(map);
                repetitionCount++;

                while (true) {
                    if (rows.isEmpty())
                        break;
                    row = rows.peek();
                    map = match(row, rowPattern);
                    if (map == null)
                        break;

                    subtableMap.add(map);
                    rows.poll();
                    repetitionCount++;
                }

                rowPattern.setRepetitionCount(repetitionCount);
            }
        }

        return subtableMap;
    }

    private RowMap match(@NonNull IRow row, RowPattern tmpl) {
        final RowMap rowMap = new RowMap();

        final Queue<ICell> cells = new LinkedList<>(row.cellsAsList());
        final List<SubrowPattern> subrowPatterns = tmpl.getSubrowPatterns();
        for (SubrowPattern subrowPattern : subrowPatterns) {
            int repetitionCount = 0;

            final Quantifier quantifier = subrowPattern.getQuantifier();
            final Quantifier.Times times = quantifier.times();

            if (times == Quantifier.Times.UNDEFINED) {
                SubrowMap map = match(cells, subrowPattern);
                if (map == null)
                    return null;

                rowMap.add(map);
                repetitionCount++;

                subrowPattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (times == Quantifier.Times.EXACTLY) {
                final int exactly = quantifier.exactly();
                for (int i = 0; i < exactly; i++) {
                    SubrowMap map = match(cells, subrowPattern);
                    if (map == null)
                        return null;

                    rowMap.add(map);
                    repetitionCount++;
                }

                subrowPattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (times == Quantifier.Times.ZERO_OR_ONE) {
                SubrowMap map = match(cells, subrowPattern);
                if (map != null) {
                    rowMap.add(map);
                    repetitionCount++;
                }

                subrowPattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (times == Quantifier.Times.ZERO_OR_MORE) {
                while (true) {
                    SubrowMap map = match(cells, subrowPattern);
                    if (map == null)
                        break;

                    rowMap.add(map);
                    repetitionCount++;
                }

                subrowPattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (quantifier.times() == Quantifier.Times.ONE_OR_MORE) {
                SubrowMap map = match(cells, subrowPattern);
                if (map == null)
                    return null;

                rowMap.add(map);
                repetitionCount++;

                while (true) {
                    map = match(cells, subrowPattern);
                    if (map == null)
                        break;

                    rowMap.add(map);
                    repetitionCount++;
                }

                subrowPattern.setRepetitionCount(repetitionCount);
            }
        }

        if (!cells.isEmpty())
            return null;

        return rowMap;
    }

    private SubrowMap match(Queue<ICell> cells, SubrowPattern tmpl) {
        if (cells.isEmpty())
            return null;

        final SubrowMap subrowMap = new SubrowMap();

        final List<CellPattern> cellPatterns = tmpl.getCellPatterns();
        for (CellPattern cellPattern : cellPatterns) {
            int repetitionCount = 0;

            final Quantifier quantifier = cellPattern.getQuantifier();
            final Quantifier.Times times = quantifier.times();

            if (times == Quantifier.Times.UNDEFINED) {
                ICell cell = cells.poll();
                if (cell == null)
                    return null; // Impossible

                CellMap map = match(cell, cellPattern);
                if (map == null)
                    return null;

                subrowMap.add(map);
                repetitionCount++;

                cellPattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (times == Quantifier.Times.EXACTLY) {
                final int exactly = quantifier.exactly();
                for (int i = 0; i < exactly; i++) {
                    if (cells.isEmpty())
                        return null;
                    ICell cell = cells.peek();
                    CellMap map = match(cell, cellPattern);
                    if (map == null)
                        return null;

                    cells.poll();
                    subrowMap.add(map);
                    repetitionCount++;
                }

                cellPattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (times == Quantifier.Times.ZERO_OR_ONE) {
                if (cells.isEmpty())
                    continue;
                final ICell cell = cells.peek();
                CellMap map = match(cell, cellPattern);
                if (map != null) {
                    cells.poll();
                    subrowMap.add(map);
                    repetitionCount = 1;
                }

                cellPattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (times == Quantifier.Times.ZERO_OR_MORE) {
                while (true) {
                    if (cells.isEmpty())
                        break;
                    ICell cell = cells.peek();
                    CellMap map = match(cell, cellPattern);
                    if (map == null)
                        break;
                    cells.poll();
                    subrowMap.add(map);
                    repetitionCount++;
                }

                cellPattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (quantifier.times() == Quantifier.Times.ONE_OR_MORE) {
                if (cells.isEmpty())
                    return null;

                ICell cell = cells.peek();
                CellMap map = match(cell, cellPattern);
                if (map == null)
                    return null;

                cells.poll();
                subrowMap.add(map);
                repetitionCount++;

                while (true) {
                    if (cells.isEmpty())
                        break;
                    cell = cells.peek();
                    map = match(cell, cellPattern);
                    if (map == null)
                        break;

                    cells.poll();
                    subrowMap.add(map);
                    repetitionCount++;
                }

                cellPattern.setRepetitionCount(repetitionCount);
            }
        }

        return subrowMap;
    }

    private CellMap match(@NonNull ICell cell, CellPattern tmpl) {
        final Condition condition = tmpl.getCondition();
        if (condition != null) {
            final boolean result = condition.check(cell, cell); //TODO переделать на один аргумент
            if (!result) {
                // final String msg = String.format("cell {%s} does not match to pattern %s", cell, tmpl);
                // log.warning(msg);
                return null; // TODO log
            }
        }

        return new CellMap(cell, tmpl);
    }

}
