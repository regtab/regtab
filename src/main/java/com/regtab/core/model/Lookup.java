package com.regtab.core.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public final class Lookup {
    private Element caller;

    @Getter
    @Setter
    private boolean all;

    @Getter
    final private Direction direction;

    @Getter
    @Setter
    private
    RangeDesc rowRangeDesc;

    @Getter
    @Setter
    private
    RangeDesc colRangeDesc;

    @Getter
    @Setter
    private int index = -1; // TODO Почему index = -1, может быть сделать null

    private final List<String> tags = new ArrayList<>();

    public void addTag(String tag) {
        tags.add(tag);
    }

    List<String> tags() {
        return new ArrayList<>(tags);
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

    public Element findElement(Element caller) {
        return findElement(null, caller);
    }

    public Element findElement(Element.Type type, Element caller) {
        List<Element> elements = findElements(type, caller);
        if (elements == null) return null;
        return elements.size() > 0 ? elements.get(0) : null;
    }

    public List<Element> findElements(Element.Type type, Element caller) {
        this.caller = caller;

        final ICell cell = caller.getCell();
        final IRow row = cell.getRow();
        final ICol col = cell.getCol();

        final ITable table = cell.getTable();
        final List<IRow> rows = table.rows();
        final List<ICol> cols = table.cols();

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

        //Range rRange = getRRange();
        //Range cRange = getCRange();
        Range rRange = null;
        if (rowRangeDesc != null)
            rRange = rowRangeDesc.createRange(cell); //createRange(rowRangeDesc, caller.getCell().r());

        Range cRange = null;
        if (colRangeDesc != null)
            cRange = colRangeDesc.createRange(cell); //createRange(colRangeDesc, caller.getCell().c());

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

        List<Element> elements = new ArrayList<>();

        List<ICell> cells = switch (direction) {
            case IN_ROW -> collectCells(rows, rRange, rowRange);
            case IN_COL -> collectCells(cols, cRange, colRange);
            case LEFT -> collectCells(rows, rRange, leftRange);
            case RIGHT -> collectCells(rows, rRange, rightRange);
            case UP -> collectCells(cols, cRange, upRange);
            case DOWN -> collectCells(cols, cRange, downRange);
            case IN_CELL -> collectCells(cols, cellCRange, cellRRange);
        };

        if (cells != null) {
            if (direction != Direction.IN_CELL)
                cells.remove(cell);
            collectElements(cells, type, elements);
            if (direction == Direction.IN_CELL)
                elements.remove(caller);
        }

        return elements.isEmpty() ? null : elements;
    }

    private List<ICell> collectCells(List<? extends CellRange> cellRanges, Range range1, Range range2) {
        final int from = range1.from();
        final int to = range1.to();
        final List<? extends CellRange> subList = cellRanges.subList(from, to + 1);
        return collectCells(subList, range2);
    }

    private List<ICell> collectCells(List<? extends CellRange> cellRanges, Range range) {
        List<ICell> allCells = new ArrayList<>();
        for (CellRange cellRange : cellRanges) {
            List<ICell> cells = collectCells(cellRange, range);
            allCells.addAll(cells);
        }
        return allCells;
    }

    private List<ICell> collectCells(CellRange cellRange, Range range) {
        List<ICell> cells = cellRange.cells();

        int from = range.from();
        int to = range.to();

        List<ICell> subList = new ArrayList<>(to - from + 1);

        for (int i = from; i <= to; i++) {
            ICell cell = cells.get(i);
            subList.add(cell);
        }

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
        List<Element> elements = cell.elements();
        if (elements != null) {
            int index = getIndex();
            if (index > -1 && index < elements.size()) {
                Element elem = elements.get(index);
                collectElement(elem, type, result);
            } else {
                for (Element elem : elements) {
                    collectElement(elem, type, result);
                    if (!all && !result.isEmpty()) return;
                }
            }
        }
    }

//    private Range createRange(RangeDesc rangeDesc, int relatively) {
//        final int from, to;
//
//        if (rangeDesc.getStartRelatively() == RangeDesc.Relatively.PLUS)
//            from = relatively + rangeDesc.getStart();
//        else if (rangeDesc.getStartRelatively() == RangeDesc.Relatively.MINUS)
//            from = relatively - rangeDesc.getStart();
//        else
//            from = rangeDesc.getStart();
//
//
//        if (rangeDesc.getEndRelatively() == RangeDesc.Relatively.PLUS)
//            to = relatively + rangeDesc.getEnd();
//        else if (rangeDesc.getEndRelatively() == RangeDesc.Relatively.MINUS)
//            to = relatively - rangeDesc.getEnd();
//        else
//            to = rangeDesc.getEnd();
//
//        return new Range(from, to);
//    }

}
