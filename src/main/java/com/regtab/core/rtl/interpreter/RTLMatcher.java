package com.regtab.core.rtl.interpreter;

import lombok.extern.slf4j.Slf4j;
import lombok.NonNull;

import com.regtab.core.model.Condition;
import com.regtab.core.model.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static com.regtab.core.rtl.interpreter.TableMap.*;
import static com.regtab.core.rtl.interpreter.RTLPattern.*;

@Slf4j
public class RTLMatcher {
    @NonNull
    private final RTLPattern pattern;

    public RTLMatcher(@NonNull RTLPattern pattern) {
        this.pattern = pattern;
    }

    public TableMap match(@NonNull ITable table) {
        final TablePattern tablePattern = pattern.getTablePattern();
        return match(table, tablePattern);
    }

    private TableMap match(ITable table, TablePattern pattern) {
        final TableMap tableMap = new TableMap();

        final Queue<IRow> rows = new LinkedList<>(table.rowsAsList());

        final List<SubtablePattern> subtablePatterns = pattern.getSubtablePatterns();
        for (SubtablePattern subtablePattern : subtablePatterns) {
            log.debug("Next subtable pattern: {}", subtablePattern);
            log.debug("Rows ahead: {}", rows.size());

            int repetitionCount = 0;

            final Quantifier quantifier = subtablePattern.getQuantifier();
            final Quantifier.Times times = quantifier.times();

            if (times == Quantifier.Times.UNDEFINED) {
                SubtableMap map = match(rows, subtablePattern);
                if (map == null) {
                    log.debug("Subtable does not match to the pattern {}", subtablePattern);
                    return null;
                }

                tableMap.add(map);
                repetitionCount++;

                subtablePattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (times == Quantifier.Times.EXACTLY) {
                final int exactly = quantifier.exactly();
                for (int i = 0; i < exactly; i++) {
                    SubtableMap map = match(rows, subtablePattern);
                    if (map == null) {
                        log.debug("Subtable does not match to the pattern {}", subtablePattern);
                        return null;
                    }
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
                if (map == null) {
                    log.debug("Subtable does not match to the pattern {}", subtablePattern);
                    return null;
                }

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

    private SubtableMap match(Queue<IRow> rows, SubtablePattern pattern) {
        if (rows.isEmpty()) {
            log.debug("No more rows for matching");
            return null;
        }

        final SubtableMap subtableMap = new SubtableMap();

        final List<RowPattern> rowPatterns = pattern.getRowPatterns();
        for (RowPattern rowPattern : rowPatterns) {
            log.debug("Next row pattern: {}", rowPattern);

            int repetitionCount = 0;

            final Quantifier quantifier = rowPattern.getQuantifier();
            final Quantifier.Times times = quantifier.times();

            if (times == Quantifier.Times.UNDEFINED) {
                IRow row = rows.poll();
                if (row == null) {
                    log.debug("Impossible");
                    return null; // Impossible
                }

                RowMap map = match(row, rowPattern);
                if (map == null) {
                    log.debug("Row [{}] does not match to the pattern {}", row, rowPattern);
                    return null;
                }

                subtableMap.add(map);
                repetitionCount++;

                rowPattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (times == Quantifier.Times.EXACTLY) {
                final int exactly = quantifier.exactly();
                for (int i = 0; i < exactly; i++) {
                    if (rows.isEmpty()) {
                        log.debug("No more rows for matching");
                        return null;
                    }
                    IRow row = rows.peek();
                    RowMap map = match(row, rowPattern);
                    if (map == null) {
                        log.debug("Row [{}] does not match to the pattern {}", row, rowPattern);
                        return null;
                    }

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
                if (rows.isEmpty()) {
                    log.debug("No more rows for matching");
                    return null;
                }
                IRow row = rows.peek();
                RowMap map = match(row, rowPattern);
                if (map == null) {
                    log.debug("Row [{}] does not match to the pattern {}", row, rowPattern);
                    return null;
                }

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

        log.debug("Subtable has been matched to the pattern {}", pattern);
        return subtableMap;
    }

    private RowMap match(@NonNull IRow row, RowPattern pattern) {
        log.debug("Next row: {}", row);

        final RowMap rowMap = new RowMap();

        final Queue<ICell> cells = new LinkedList<>(row.cellsAsList());
        final List<SubrowPattern> subrowPatterns = pattern.getSubrowPatterns();
        for (SubrowPattern subrowPattern : subrowPatterns) {
            log.debug("Next subrow pattern: {}", subrowPattern);
            log.debug("Cells ahead: {}", cells.size());

            int repetitionCount = 0;

            final Quantifier quantifier = subrowPattern.getQuantifier();
            final Quantifier.Times times = quantifier.times();

            if (times == Quantifier.Times.UNDEFINED) {
                SubrowMap map = match(cells, subrowPattern);
                if (map == null) {
                    log.debug("Row does not match to the pattern {}", pattern);
                    return null;
                }
                rowMap.add(map);
                repetitionCount++;

                subrowPattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (times == Quantifier.Times.EXACTLY) {
                final int exactly = quantifier.exactly();
                for (int i = 0; i < exactly; i++) {
                    SubrowMap map = match(cells, subrowPattern);
                    if (map == null) {
                        log.debug("Row does not match to the pattern {}", pattern);
                        return null;
                    }
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
                if (map == null) {
                    log.debug("Row does not match to the pattern {}", pattern);
                    return null;
                }

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

        if (!cells.isEmpty()) {
            log.debug("No more cells for matching");
            return null;
        }

        log.debug("Row has been matched to the pattern {}", pattern);

        return rowMap;
    }

    private SubrowMap match(Queue<ICell> cells, SubrowPattern pattern) {
        if (cells.isEmpty()) {
            log.debug("No more cells for matching");
            return null;
        }

        final SubrowMap subrowMap = new SubrowMap();

        final List<CellPattern> cellPatterns = pattern.getCellPatterns();
        for (CellPattern cellPattern : cellPatterns) {
            log.debug("Next cell pattern: {}", cellPattern);

            int repetitionCount = 0;

            final Quantifier quantifier = cellPattern.getQuantifier();
            final Quantifier.Times times = quantifier.times();

            if (times == Quantifier.Times.UNDEFINED) {
                ICell cell = cells.poll();
                if (cell == null) {
                    log.debug("Impossible");
                    return null; // Impossible
                }

                CellMap map = match(cell, cellPattern);
                if (map == null) {
                    final String subrowAsString = Arrays.toString(cells.toArray());
                    log.debug("Cells {} does not match to the pattern {}", subrowAsString, pattern);
                    return null;
                }
                subrowMap.add(map);
                repetitionCount++;

                cellPattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (times == Quantifier.Times.EXACTLY) {
                final int exactly = quantifier.exactly();
                for (int i = 0; i < exactly; i++) {
                    if (cells.isEmpty()) {
                        log.debug("Cells {} does not match to the pattern {}", "[]", pattern);
                        return null;
                    }
                    ICell cell = cells.peek();
                    CellMap map = match(cell, cellPattern);
                    if (map == null) {
                        final String subrowAsString = Arrays.toString(cells.toArray());
                        log.debug("Cells {} does not match to the pattern {}", subrowAsString, pattern);
                        return null;
                    }
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
                if (cells.isEmpty()) {
                    log.debug("Cells {} does not match to the pattern {}", "[]", pattern);
                    return null;
                }
                ICell cell = cells.peek();
                CellMap map = match(cell, cellPattern);
                if (map == null) {
                    final String subrowAsString = Arrays.toString(cells.toArray());
                    log.debug("Cells {} does not match to the pattern {}", subrowAsString, pattern);
                    return null;
                }

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

        log.debug("Subrow have been matched to the pattern {}", pattern);

        return subrowMap;
    }

    private CellMap match(@NonNull ICell cell, CellPattern pattern) {
        final Condition condition = pattern.getCondition();
        if (condition != null) {
            final boolean result = condition.check(cell);
            if (!result) {
                log.debug("Cell [{}] does not match to the pattern {}", cell, pattern);
                return null;
            }
        }

        log.debug("Cell [{}] has been matched to the pattern {}", cell, pattern);
        return new CellMap(cell, pattern);
    }

}
