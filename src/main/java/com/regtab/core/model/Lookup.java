package com.regtab.core.model;

import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public final class Lookup {
    private Element caller;

    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    private boolean all;

    @Getter
    @Setter
    private Integer limit;

    @Getter
    final private Direction direction;

    @Getter
    @Setter
    private
    Range.Desc rowRangeDesc;

    @Getter
    @Setter
    private
    Range.Desc colRangeDesc;

    @Getter
    @Setter
    private Integer elementIndex;

    private final List<String> tags = new ArrayList<>();

    public void addTag(@NonNull String tag) {
        tags.add(tag);
    }

    @Getter
    @Setter
    private Condition condition;

    public enum Direction {
        LEFT(true),
        RIGHT(false),
        UP(true),
        DOWN(false),
        IN_ROW(false),
        IN_COL(false),
        IN_CELL(false);

        public final boolean reversed;

        Direction(boolean reversed) {
            this.reversed = reversed;
        }
    }

    Element findFirst(Element caller) {
        return findFirst(null, caller);
    }

    Element findFirst(Element.Type type, Element caller) {
        final List<Element> elements = findElements(type, caller);
        if (elements == null) return null;
        return elements.size() > 0 ? elements.getFirst() : null;
    }

    List<Element> findAll(Element.Type type, Element caller) {
        return findElements(type, caller);
    }

    private List<Element> findElements(Element.Type type, Element caller) {
        final List<ICell> cells = collectCells(caller);
        List<Element> elements = new ArrayList<>();

        if (cells != null) {
            collectElements(cells, type, elements);
            if (direction == Direction.IN_CELL)
                elements.remove(caller);
        }

        return elements.isEmpty() ? null : elements;
    }

    private List<ICell> collectCells(Element caller) {
        this.caller = caller;

        final ICell cell = caller.getCell();
        final IRow row = cell.getRow();
        final ICol col = cell.getCol();

        final int rowSize = row.size();
        final int colSize = col.size();

        Range rowRange = new Range(0, rowSize - 1);
        Range colRange = new Range(0, colSize - 1);
        Range leftRange = new Range(0, cell.c() - 1);
        Range rightRange = new Range(cell.c() + 1, rowSize - 1);
        Range upRange = new Range(0, cell.r() - 1);
        Range downRange = new Range(cell.r() + 1, colSize - 1);

        Range cellRRange = new Range(cell.r(), cell.r());
        Range cellCRange = new Range(cell.c(), cell.c());

        Range rRange = null;
        if (rowRangeDesc != null)
            rRange = rowRangeDesc.createRange(cell);

        Range cRange = null;
        if (colRangeDesc != null)
            cRange = colRangeDesc.createRange(cell);

        if (cRange != null) {
            rowRange = Range.intersection(cRange, rowRange);
            leftRange = Range.intersection(cRange, leftRange);
            rightRange = Range.intersection(cRange, rightRange);
            cellCRange = cRange;
        } else {
            int pos = col.getPosition();
            cRange = new Range(pos, pos);
        }

        if (rRange != null) {
            colRange = Range.intersection(rRange, colRange);
            upRange = Range.intersection(rRange, upRange);
            downRange = Range.intersection(rRange, downRange);
            cellRRange = rRange;
        } else {
            int pos = row.getPosition();
            rRange = new Range(pos, pos);
        }

        final ITable table = cell.getTable();
        final IRow[] rows = table.copyRows();
        final ICol[] cols = table.copyCols();

        List<ICell> cells = switch (direction) {
            case IN_ROW -> collectCells(rows, rRange, rowRange);
            case IN_COL -> collectCells(cols, cRange, colRange);
            case LEFT -> collectCells(rows, rRange, leftRange);
            case RIGHT -> collectCells(rows, rRange, rightRange);
            case UP -> collectCells(cols, cRange, upRange);
            case DOWN -> collectCells(cols, cRange, downRange);
            case IN_CELL -> collectCells(rows, cellRRange, cellCRange);
        };

        if (direction != Direction.IN_CELL)
            cells.remove(cell);

        return cells.isEmpty() ? null : cells;
    }

    private List<ICell> collectCells(IRow[] rows, Range range1, Range range2) {
        final int from = range1.start();
        final int to = range1.end();
        final IRow[] sub = Arrays.copyOfRange(rows, from, to + 1);

        final List<ICell> allCells = new ArrayList<>();
        for (IRow row : sub) {
            final ICell[] cells = row.copyCells();
            List<ICell> collectedCells = collectCells(cells, range2);
            allCells.addAll(collectedCells);
        }
        return allCells;
    }

    private List<ICell> collectCells(ICol[] cols, Range range1, Range range2) {
        final int from = range1.start();
        final int to = range1.end();
        final ICol[] sub = Arrays.copyOfRange(cols, from, to + 1);

        final List<ICell> allCells = new ArrayList<>();
        for (ICol col : sub) {
            final ICell[] cells = col.copyCells();
            List<ICell> collectedCells = collectCells(cells, range2);
            allCells.addAll(collectedCells);
        }
        return allCells;
    }

    private List<ICell> collectCells(final ICell[] cells, final Range range) {
        int from = range.start();
        int to = range.end();

        List<ICell> subList = new ArrayList<>(to - from + 1);

        subList.addAll(Arrays.asList(cells).subList(from, to + 1));

        if (direction.reversed && subList.size() > 1)
            subList = subList.reversed();

        return subList.stream().
                filter(candidate -> condition == null || condition.check(caller.getCell(), candidate)).
                collect(Collectors.toList());
    }

    private void collectElements(List<ICell> cells, Element.Type type, List<Element> result) {
        for (ICell c : cells) {
            collectElements(c, type, result);
            if (!all) {
                if (!result.isEmpty())
                    return;
            }
        }
    }

    private void collectElement(Element elem, Element.Type type, List<Element> result) {
        if (elem.getType() == type || null == type) {
            if (tags.isEmpty()) {
                result.add(elem);
            } else {
                for (String tag : tags) {
                    if (elem.hasTag(tag)) {
                        result.add(elem);
                        if (!all) return;
                    }
                }
            }
        }
    }

    private void collectElements(ICell cell, Element.Type type, List<Element> result) {
        final List<Element> elements = cell.getElements();
        if (elements != null) {
            if (elementIndex != null && elementIndex < elements.size()) {
                Element elem = elements.get(elementIndex);
                if (elem != caller)
                    collectElement(elem, type, result);
            } else {
                for (Element elem : elements) {
                    if (limit != null && result.size() == limit)
                        return;

                    if (elem == caller)
                        continue;

                    collectElement(elem, type, result);

                    if (!all && !result.isEmpty())
                        return;
                }
            }
        }
    }

}
