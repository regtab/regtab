package com.regtab.core.rtl.interpreter;

import lombok.extern.slf4j.Slf4j;
import lombok.NonNull;

import com.regtab.core.model.Condition;
import com.regtab.core.model.*;

import java.util.*;

import static com.regtab.core.rtl.interpreter.TableMatch.*;
import static com.regtab.core.rtl.interpreter.RTLPattern.*;

/**
 * The `RTLMatcher` class is responsible for matching a given table against a compiled RTL pattern.
 * It uses the `TableMatch` and `RTLPattern` classes to represent the matching result and pattern respectively.
 * The matching process is performed by the `match` method, which takes an `ITable` and a `RTLPattern` as input.
 */
@Slf4j
public class RTLMatcher {
    @NonNull
    private final RTLPattern pattern;

    /**
     * Constructs a new `RTLMatcher` with the specified RTL pattern.
     *
     * @param pattern The RTL pattern to be used for matching.
     */
    public RTLMatcher(@NonNull RTLPattern pattern) {
        this.pattern = pattern;
    }

    /**
     * Matches the given table against the compiled RTL pattern.
     *
     * @param table The table to be matched.
     * @return A `TableMatch` representing the matching result between the table and the pattern, 
     * or null if no match is found.
     */
    public TableMatch match(@NonNull ITable table) {
        table.clear();
        final TablePattern tablePattern = pattern.getTablePattern();

        // Настройка ITM
        SettingParams settingParams = tablePattern.getSettingParams();
        if (settingParams != null) {
            Optional<Integer> basicFieldIndexOpt = settingParams.getInt("@basicFieldIndex");
            if (basicFieldIndexOpt.isPresent())
                table.setBasicFiledIndex(basicFieldIndexOpt.get());
            Optional<Boolean> splitComponentsOpt = settingParams.getBoolean("@splitComponents");
            if (splitComponentsOpt.isPresent())
                table.setSplitComponents(splitComponentsOpt.get());
            Optional<String> compSeparatorOpt = settingParams.getString("@compSeparator");
            if (compSeparatorOpt.isPresent())
                table.setCompSeparator(compSeparatorOpt.get());
            Optional<Boolean> alignedNamedAttrsOpt = settingParams.getBoolean("@alignedNamedAttrs");
            if (alignedNamedAttrsOpt.isPresent())
                table.setAlignedNamedAttrs(alignedNamedAttrsOpt.get());
            Optional<Boolean> normalizedSpacesOpt = settingParams.getBoolean("@normalizedSpaces");
            if (normalizedSpacesOpt.isPresent())
                table.setNormalizedSpaces(normalizedSpacesOpt.get());
        }

        return match(table, tablePattern);
    }

    // Private helper methods for matching subtable, row, subrow, and cell patterns.
    // Each method returns a corresponding map (SubtableMatch, RowMatch, SubrowMatch, CellMatch)
    // if the match is successful, or null if the match fails.

    private TableMatch match(ITable table, TablePattern pattern) {
        final TableMatch tableMatch = new TableMatch();

        final Queue<IRow> rows = new LinkedList<>(table.rowsAsList());

        final List<SubtablePattern> subtablePatterns = pattern.getSubtablePatterns();
        for (SubtablePattern subtablePattern : subtablePatterns) {
            log.debug("Next subtable pattern: {}", subtablePattern);
            log.debug("Rows ahead: {}", rows.size());

            int repetitionCount = 0;

            final Quantifier quantifier = subtablePattern.getQuantifier();
            final Quantifier.Times times = quantifier.times();

            if (times == Quantifier.Times.UNDEFINED) {
                SubtableMatch match = match(rows, subtablePattern);
                if (match == null) {
                    log.debug("Subtable does not match to the pattern {}", subtablePattern);
                    return null;
                }

                tableMatch.add(match);
                repetitionCount++;

                subtablePattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (times == Quantifier.Times.EXACTLY) {
                final int exactly = quantifier.exactly();
                for (int i = 0; i < exactly; i++) {
                    SubtableMatch match = match(rows, subtablePattern);
                    if (match == null) {
                        log.debug("Subtable does not match to the pattern {}", subtablePattern);
                        return null;
                    }
                    tableMatch.add(match);
                    repetitionCount++;
                }

                subtablePattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (times == Quantifier.Times.ZERO_OR_ONE) {
                SubtableMatch match = match(rows, subtablePattern);
                if (match != null) {
                    tableMatch.add(match);
                    repetitionCount++;
                }

                subtablePattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (times == Quantifier.Times.ZERO_OR_MORE) {
                while (true) {
                    SubtableMatch match = match(rows, subtablePattern);
                    if (match == null)
                        break;

                    tableMatch.add(match);
                    repetitionCount++;
                }

                subtablePattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (quantifier.times() == Quantifier.Times.ONE_OR_MORE) {
                SubtableMatch match = match(rows, subtablePattern);
                if (match == null) {
                    log.debug("Subtable does not match to the pattern {}", subtablePattern);
                    return null;
                }

                tableMatch.add(match);
                repetitionCount++;

                while (true) {
                    match = match(rows, subtablePattern);
                    if (match == null)
                        break;

                    tableMatch.add(match);
                    repetitionCount++;
                }

                subtablePattern.setRepetitionCount(repetitionCount);
            }
        }

        return tableMatch;
    }

    private SubtableMatch match(Queue<IRow> rows, SubtablePattern pattern) {
        if (rows.isEmpty()) {
            log.debug("No more rows for matching");
            return null;
        }

        final SubtableMatch subtableMatch = new SubtableMatch();

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

                RowMatch match = match(row, rowPattern);
                if (match == null) {
                    log.debug("Row [{}] does not match to the pattern {}", row, rowPattern);
                    return null;
                }

                subtableMatch.add(match);
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
                    RowMatch match = match(row, rowPattern);
                    if (match == null) {
                        log.debug("Row [{}] does not match to the pattern {}", row, rowPattern);
                        return null;
                    }

                    rows.poll();
                    subtableMatch.add(match);
                    repetitionCount++;
                }

                rowPattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (times == Quantifier.Times.ZERO_OR_ONE) {
                if (!rows.isEmpty()) {
                    final IRow row = rows.peek();
                    RowMatch match = match(row, rowPattern);
                    if (match != null) {
                        rows.poll();
                        subtableMatch.add(match);
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
                    RowMatch match = match(row, rowPattern);
                    if (match == null)
                        break;

                    rows.poll();
                    subtableMatch.add(match);
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
                RowMatch match = match(row, rowPattern);
                if (match == null) {
                    log.debug("Row [{}] does not match to the pattern {}", row, rowPattern);
                    return null;
                }

                rows.poll();
                subtableMatch.add(match);
                repetitionCount++;

                while (true) {
                    if (rows.isEmpty())
                        break;
                    row = rows.peek();
                    match = match(row, rowPattern);
                    if (match == null)
                        break;

                    subtableMatch.add(match);
                    rows.poll();
                    repetitionCount++;
                }

                rowPattern.setRepetitionCount(repetitionCount);
            }
        }

        log.debug("Subtable has been matched to the pattern {}", pattern);
        return subtableMatch;
    }

    private RowMatch match(@NonNull IRow row, RowPattern pattern) {
        log.debug("Next row: {}", row);

        final RowMatch rowMatch = new RowMatch(row);

        final Queue<ICell> cells = new LinkedList<>(row.cellsAsList());
        final List<SubrowPattern> subrowPatterns = pattern.getSubrowPatterns();
        for (SubrowPattern subrowPattern : subrowPatterns) {
            log.debug("Next subrow pattern: {}", subrowPattern);
            log.debug("Cells ahead: {}", cells.size());

            int repetitionCount = 0;

            final Quantifier quantifier = subrowPattern.getQuantifier();
            final Quantifier.Times times = quantifier.times();

            if (times == Quantifier.Times.UNDEFINED) {
                SubrowMatch match = match(cells, subrowPattern);
                if (match == null) {
                    log.debug("Row does not match to the pattern {}", pattern);
                    return null;
                }
                rowMatch.add(match);
                repetitionCount++;

                subrowPattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (times == Quantifier.Times.EXACTLY) {
                final int exactly = quantifier.exactly();
                for (int i = 0; i < exactly; i++) {
                    SubrowMatch match = match(cells, subrowPattern);
                    if (match == null) {
                        log.debug("Row does not match to the pattern {}", pattern);
                        return null;
                    }
                    rowMatch.add(match);
                    repetitionCount++;
                }

                subrowPattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (times == Quantifier.Times.ZERO_OR_ONE) {
                SubrowMatch match = match(cells, subrowPattern);
                if (match != null) {
                    rowMatch.add(match);
                    repetitionCount++;
                }

                subrowPattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (times == Quantifier.Times.ZERO_OR_MORE) {
                while (true) {
                    SubrowMatch match = match(cells, subrowPattern);
                    if (match == null)
                        break;

                    rowMatch.add(match);
                    repetitionCount++;
                }

                subrowPattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (quantifier.times() == Quantifier.Times.ONE_OR_MORE) {
                SubrowMatch match = match(cells, subrowPattern);
                if (match == null) {
                    log.debug("Row does not match to the pattern {}", pattern);
                    return null;
                }

                rowMatch.add(match);
                repetitionCount++;

                while (true) {
                    match = match(cells, subrowPattern);
                    if (match == null)
                        break;

                    rowMatch.add(match);
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

        return rowMatch;
    }

    private SubrowMatch match(Queue<ICell> cells, SubrowPattern pattern) {
        if (cells.isEmpty()) {
            log.debug("No more cells for matching");
            return null;
        }

        final SubrowMatch subrowMatch = new SubrowMatch();

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

                CellMatch match = match(cell, cellPattern);
                if (match == null) {
                    final String subrowAsString = Arrays.toString(cells.toArray());
                    log.debug("Cells {} does not match to the pattern {}", subrowAsString, pattern);
                    return null;
                }
                subrowMatch.add(match);
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
                    CellMatch match = match(cell, cellPattern);
                    if (match == null) {
                        final String subrowAsString = Arrays.toString(cells.toArray());
                        log.debug("Cells {} does not match to the pattern {}", subrowAsString, pattern);
                        return null;
                    }
                    cells.poll();
                    subrowMatch.add(match);
                    repetitionCount++;
                }

                cellPattern.setRepetitionCount(repetitionCount);
                continue;
            }

            if (times == Quantifier.Times.ZERO_OR_ONE) {
                if (cells.isEmpty())
                    continue;
                final ICell cell = cells.peek();
                CellMatch match = match(cell, cellPattern);
                if (match != null) {
                    cells.poll();
                    subrowMatch.add(match);
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
                    CellMatch match = match(cell, cellPattern);
                    if (match == null)
                        break;
                    cells.poll();
                    subrowMatch.add(match);
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
                CellMatch match = match(cell, cellPattern);
                if (match == null) {
                    final String subrowAsString = Arrays.toString(cells.toArray());
                    log.debug("Cells {} does not match to the pattern {}", subrowAsString, pattern);
                    return null;
                }

                cells.poll();
                subrowMatch.add(match);
                repetitionCount++;

                while (true) {
                    if (cells.isEmpty())
                        break;
                    cell = cells.peek();
                    match = match(cell, cellPattern);
                    if (match == null)
                        break;

                    cells.poll();
                    subrowMatch.add(match);
                    repetitionCount++;
                }

                cellPattern.setRepetitionCount(repetitionCount);
            }
        }

        log.debug("Subrow have been matched to the pattern {}", pattern);

        return subrowMatch;
    }

    private CellMatch match(@NonNull ICell cell, CellPattern pattern) {
        final Condition condition = pattern.getCondition();
        if (condition != null) {
            final boolean result = condition.check(cell);
            if (!result) {
                log.debug("Cell [{}] does not match to the pattern {}", cell, pattern);
                return null;
            }
        }

        log.debug("Cell [{}] has been matched to the pattern {}", cell, pattern);
        return new CellMatch(cell, pattern);
    }

}
