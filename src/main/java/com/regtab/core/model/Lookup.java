package com.regtab.core.model;

import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Lookup class is used to find components in a table based on certain criteria.
 * It can search in different directions and apply conditions to filter the results.
 */
@RequiredArgsConstructor
public final class Lookup {
    private Component caller;

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
    private Integer componentIndex;

    private final List<String> tags = new ArrayList<>();

    /**
     * Adds a tag to the lookup.
     *
     * @param tag The tag to add.
     */
    public void addTag(@NonNull String tag) {
        tags.add(tag);
    }

    @Getter
    @Setter
    private Condition condition;

    /**
     * The Direction enum represents the direction in which the lookup should proceed.
     */
    public enum Direction {
        LEFT(true),
        RIGHT(false),
        UP(true),
        DOWN(false),
        IN_ROW(false),
        IN_COL(false),
        IN_CELL(false),

        SUB_LEFT(true),
        SUB_RIGHT(false),
        SUB_UP(true),
        SUB_DOWN(false),
        IN_SUB_ROW(false),
        IN_SUB_COL(false);

        public final boolean reversed;

        Direction(boolean reversed) {
            this.reversed = reversed;
        }
    }

    /**
     * Finds the first component that matches the lookup criteria.
     *
     * @param caller The component from which the lookup is initiated.
     * @return The first matching component, or null if no match is found.
     */
    Component findFirst(Component caller) {
        return findFirst(null, caller);
    }

    /**
     * Finds the first component of a specific type that matches the lookup criteria.
     *
     * @param type The type of the component to find.
     * @param caller The component from which the lookup is initiated.
     * @return The first matching component of the specified type, or null if no match is found.
     */
    Component findFirst(Component.Type type, Component caller) {
        final List<Component> components = findComponents(type, caller);
        if (components == null) return null;
        return components.size() > 0 ? components.getFirst() : null;
    }

    /**
     * Finds all components that match the lookup criteria.
     *
     * @param type The type of the components to find.
     * @param caller The component from which the lookup is initiated.
     * @return A list of matching components, or null if no match is found.
     */
    List<Component> findAll(Component.Type type, Component caller) {
        return findComponents(type, caller);
    }

    private List<Component> findComponents(Component.Type type, Component caller) {
        final List<ICell> cells = collectCells(caller);
        List<Component> components = new ArrayList<>();

        if (cells != null) {
            collectComponents(cells, type, components);
            if (direction == Direction.IN_CELL)
                components.remove(caller);
        }

        return components.isEmpty() ? null : components;
    }

    /**
     * Private helper method to collect cells based on the lookup criteria.
     *
     * @param caller The component from which the lookup is initiated.
     * @return A list of cells that match the lookup criteria.
     */
    private List<ICell> collectCells(Component caller) {
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

        final SubtablePos subtablePos = cell.getSubtablePos();
        final SubrowPos subrowPos = cell.getSubrowPos();

        Range subRowRange = new Range(subrowPos.leftCol(), subrowPos.rightCol());
        Range subColRange = new Range(subtablePos.topRow(), subtablePos.bottomRow());
        Range subLeftRange = new Range(subrowPos.leftCol(), cell.c() - 1);
        Range subRightRange = new Range(cell.c() + 1, subrowPos.rightCol());
        Range subUpRange = new Range(subtablePos.topRow(), cell.r() - 1);
        Range subDownRange = new Range(cell.r() + 1, subtablePos.bottomRow());


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

            // TODO: необходимо учесть сдвиг cRange относительно позиций подстроки cell.subrowPosition()
            subRowRange = Range.intersection(cRange, subRowRange);
            subLeftRange = Range.intersection(cRange, subLeftRange);
            subRightRange = Range.intersection(cRange, subRightRange);
        } else {
            int pos = col.getPosition();
            cRange = new Range(pos, pos);
        }

        if (rRange != null) {
            colRange = Range.intersection(rRange, colRange);
            upRange = Range.intersection(rRange, upRange);
            downRange = Range.intersection(rRange, downRange);
            cellRRange = rRange;

            // TODO: необходимо учесть сдвиг rRange относительно позиций подтаблицы cell.subtablePosition()
            subColRange = Range.intersection(rRange, subColRange);
            subUpRange = Range.intersection(rRange, subUpRange);
            subDownRange = Range.intersection(rRange, subDownRange);
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

            case IN_SUB_ROW -> collectCells(rows, rRange, subRowRange);
            case IN_SUB_COL -> collectCells(cols, cRange, subColRange);
            case SUB_LEFT -> collectCells(rows, rRange, subLeftRange);
            case SUB_RIGHT -> collectCells(rows, rRange, subRightRange);
            case SUB_UP -> collectCells(cols, cRange, subUpRange);
            case SUB_DOWN -> collectCells(cols, cRange, subDownRange);
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

    /**
     * Private helper method to collect components from a list of cells.
     *
     * @param cells The list of cells to search for components.
     * @param type The type of the components to collect.
     * @param result The list to which the collected components will be added.
     */
    private void collectComponents(List<ICell> cells, Component.Type type, List<Component> result) {
        for (ICell c : cells) {
            collectComponents(c, type, result);
            if (!all) {
                if (!result.isEmpty())
                    return;
            }
        }
    }

    private void collectComponent(Component elem, Component.Type type, List<Component> result) {
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

    private void collectComponents(ICell cell, Component.Type type, List<Component> result) {
        final List<Component> components = cell.getComponents();
        if (components != null) {
            if (componentIndex != null && componentIndex < components.size()) {
                Component elem = components.get(componentIndex);
                if (elem != caller)
                    collectComponent(elem, type, result);
            } else {
                for (Component elem : components) {
                    if (limit != null && result.size() == limit)
                        return;

                    if (elem == caller)
                        continue;

                    collectComponent(elem, type, result);

                    if (!all && !result.isEmpty())
                        return;
                }
            }
        }
    }

}
