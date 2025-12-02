package com.regtab.core.rtl.interpreter;

import com.regtab.core.model.*;
import com.regtab.core.model.Action;
import lombok.extern.slf4j.Slf4j;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import com.regtab.core.model.Component;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.*;

import com.regtab.core.rtl.parser.*;

import static com.regtab.core.rtl.interpreter.Quantifier.Times.*;
import static com.regtab.core.rtl.parser.RTLParser.*;

/**
 * The `RTLPattern` class represents a compiled RegTab Language (RTL) pattern.
 * It uses ANTLR to parse RTL expressions and constructs a hierarchical pattern structure
 * that can be used to match and apply actions to tables.
 */
public class RTLPattern {
    @Getter(AccessLevel.PACKAGE)
    private final TablePattern tablePattern;

    private RTLPattern(TablePattern tablePattern) {
        this.tablePattern = tablePattern;
    }

    /**
     * Compiles an RTL string into a `RTLPattern` object.
     *
     * @param rtl The RTL string to compile.
     * @return The compiled `RTLPattern` object, or null if the RTL string is blank.
     */
    public static RTLPattern compile(@NonNull String rtl) {
        if (rtl.isBlank())
            return null;

        final RTLLexer lexer = new RTLLexer(CharStreams.fromString(rtl));
        lexer.removeErrorListeners();
        lexer.addErrorListener(RTLErrorListener.INSTANCE);
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final RTLParser parser = new RTLParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(RTLErrorListener.INSTANCE);
        final ParseTree tree = parser.table();

        final TablePattern tablePattern = Interpreter.compile(tree);

        return tablePattern == null ? null : new RTLPattern(tablePattern);
    }

    /**
     * Creates a `RTLMatcher` object for matching against tables.
     *
     * @return The `RTLMatcher` object.
     */
    public RTLMatcher matcher() {
        return new RTLMatcher(this);
    }

    /**
     * Static utility method for matching a pattern to a table and returning the result as a `TableMap`.
     *
     * @param rtl   The RTL string to match.
     * @param table The table to match against.
     * @return The `TableMap` representing the match result, or null if the pattern could not be compiled.
     */
    public static TableMatch match(@NonNull String rtl, @NonNull ITable table) {
        final RTLPattern t = compile(rtl);
        if (t == null)
            return null;
        final RTLMatcher m = t.matcher();

        return m.match(table);
    }

    /**
     * Static utility method for applying a pattern to a table.
     *
     * @param rtl   The RTL string to apply.
     * @param table The table to apply the pattern to.
     * @return True if the pattern was applied successfully, false otherwise.
     */
    public static boolean apply(@NonNull String rtl, @NonNull ITable table) {
        final TableMatch match = match(rtl, table);
        if (match == null)
            return false;

        return match.apply();
    }

    /**
     * The default configurator instance for the interpreter.
     */
    @Getter
    private static final Configurator configurator = Configurator.DEFAULT_CONFIGURATOR;

    /**
     * Returns the configurator for the interpreter.
     *
     * @return The configurator.
     */
    public static Configurator config() {
        return configurator;
    }

    /**
     * The abstract base class for all pattern classes.
     */
    public abstract static class BasePattern {
        @Getter
        private final ParseTree tree;

        @Getter
        private final Long id;

        private static Long count = 0L;

        BasePattern(ParseTree tree) {
            this.tree = tree;
            id = count ++;
        }

        /**
         * Returns a string representation of the pattern.
         *
         * @return The string representation.
         */
        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                    .append("ttl", tree.getText())
                    .toString();
        }
    }

    /**
     * The abstract class for patterns that can have actions associated with them.
     */
    public abstract static class ActionablePattern extends BasePattern {
        ActionablePattern(ParseTree tree) {
            super(tree);
        }

        @Getter
        private final List<Action> actions = new ArrayList<>(1);

        abstract void add(@NonNull Action action);

    }

    /**
     * The abstract class for patterns that can be repeated according to a quantifier and condition.
     */
    public abstract static class RepeatablePattern extends ActionablePattern {
        RepeatablePattern(ParseTree tree) {
            super(tree);
        }

        @Getter
        private Condition condition;

        void setCondition(@NonNull Condition condition) {
            if (this.condition == null)
                this.condition = condition;
            else
                this.condition.join(condition);
        }

        @Getter
        @Setter
        private Quantifier quantifier;

        @Getter
        @Setter
        private Integer repetitionCount;
    }

    public static final class SettingParams {
        private Map<Setting, String> params = new HashMap<>();
        public void add(String name, String value) {
            Setting setting = Setting.get(name);
            if (setting != null) {
                params.put(setting, value);
            }
        }

        public Optional<Boolean> getBoolean(String settingName) {
            final Setting<?> setting = Setting.get(settingName);
            if (setting == null)
                return Optional.empty();

            final String value = params.get(setting);
            if (value == null)
                return Optional.empty();

            return Optional.of(Boolean.parseBoolean(value));
        }

        public Optional<Integer> getInt(String settingName) {
            final Setting<?> setting = Setting.get(settingName);
            if (setting == null)
                return Optional.empty();

            final String value = params.get(setting);
            if (value == null)
                return Optional.empty();

            return Optional.of(Integer.parseInt(value));
        }

        public Optional<String> getString(String settingName) {
            final Setting<?> setting = Setting.get(settingName);
            if (setting == null)
                return Optional.empty();

            final String value = params.get(setting);

            return Optional.ofNullable(value);
        }

    }

    /**
     * Represents the root of the pattern structure for a table.
     */
    public static final class TablePattern extends BasePattern {
        TablePattern(@NonNull TableContext context) {
            super(context);
        }

        @Setter
        @Getter
        private SettingParams settingParams;

        @Getter
        private final List<SubtablePattern> subtablePatterns = new ArrayList<>(1);

        void add(@NonNull RTLPattern.SubtablePattern pattern) {
            pattern.setTablePattern(this);
            subtablePatterns.add(pattern);
        }

    }

    /**
     * Represents a pattern for a subtable within a table.
     */
    public static final class SubtablePattern extends RepeatablePattern {
        SubtablePattern(@NonNull SubtableContext context) {
            super(context);
        }

        RowsContext rowsContext;

        @Override
        void setCondition(@NonNull Condition condition) {
            super.setCondition(condition);

            for (RowPattern rowPattern : rowPatterns) {
                rowPattern.setCondition(condition);
            }
        }

        @Getter
        @Setter(AccessLevel.PACKAGE)
        private TablePattern tablePattern;

        @Getter
        private final List<RowPattern> rowPatterns = new ArrayList<>();

        void add(@NonNull RTLPattern.RowPattern pattern) {
            pattern.setSubtablePattern(this);
            rowPatterns.add(pattern);
        }

        @Override
        void add(@NonNull Action action) {
            getActions().add(action);

            for (RowPattern rowPattern : rowPatterns) {
                rowPattern.add(action);
            }
        }

    }

    /**
     * Represents a pattern for a row within a subtable.
     */
    public static final class RowPattern extends RepeatablePattern {
        SubrowsContext subrowsContext;

        RowPattern(@NonNull RowContext context) {
            super(context);
        }

        @Override
        void setCondition(@NonNull Condition condition) {
            super.setCondition(condition);

            for (SubrowPattern subrowPattern : subrowPatterns) {
                subrowPattern.setCondition(condition);
            }
        }

        @Getter
        @Setter(AccessLevel.PACKAGE)
        private SubtablePattern subtablePattern;

        @Getter
        private final List<SubrowPattern> subrowPatterns = new ArrayList<>(1);

        void add(@NonNull SubrowPattern pattern) {
            pattern.setRowPattern(this);
            subrowPatterns.add(pattern);
        }

        @Override
        void add(@NonNull Action action) {
            getActions().add(action);

            for (SubrowPattern subrowPattern : subrowPatterns) {
                subrowPattern.add(action);
            }
        }

    }

    /**
     * Represents a pattern for a subrow within a row.
     */
    public static final class SubrowPattern extends RepeatablePattern {
        SubrowPattern(@NonNull SubrowContext context) {
            super(context);
        }

        CellsContext cellsContext;

        @Override
        void setCondition(@NonNull Condition condition) {
            super.setCondition(condition);

            for (CellPattern cellPattern : cellPatterns) {
                cellPattern.setCondition(condition);
            }
        }

        @Getter
        @Setter(AccessLevel.PACKAGE)
        private RowPattern rowPattern;

        @Getter
        private final List<CellPattern> cellPatterns = new ArrayList<>();

        void add(@NonNull RTLPattern.CellPattern pattern) {
            pattern.setSubrowPattern(this);
            cellPatterns.add(pattern);
        }

        @Override
        void add(@NonNull Action action) {
            getActions().add(action);

            for (CellPattern cellPattern : cellPatterns) {
                cellPattern.add(action);
            }
        }
    }

    /**
     * Represents a pattern for a cell within a subrow.
     */
    public static final class CellPattern extends RepeatablePattern {
        CellPattern(CellContext context) {
            super(context);
        }

        @Getter
        @Setter
        private ComponentsPattern componentsPattern;

        @Override
        void setCondition(@NonNull Condition condition) {
            super.setCondition(condition);
        }

        @Getter
        @Setter(AccessLevel.PACKAGE)
        private SubrowPattern subrowPattern;

        @Override
        void add(@NonNull Action action) {
            getActions().add(action);
            if (componentsPattern != null)
                componentsPattern.add(action);
        }

        boolean apply(@NonNull ICell cell) {
            return componentsPattern.apply(cell);
        }
    }


    /**
     * Represents a pattern for a set of components.
     */
    public abstract static class ComponentsPattern extends ActionablePattern {
        ComponentsPattern(ParseTree tree) {
            super(tree);
        }

        abstract boolean apply(ICell cell);
    }

    /**
     * Represents a pattern for a component.
     */
    @Slf4j
    public static final class ComponentPattern extends ComponentsPattern {
        ComponentPattern(@NonNull ComponentContext context) {
            super(context);
        }

        @Getter
        @Setter
        private Component.Type componentType;

        @Getter
        @Setter
        private Expr expr;

        @Getter
        private final List<String> tags = new ArrayList<>();

        void add(@NonNull String tag) {
            tags.add(tag);
        }

        @Override
        void add(@NonNull Action action) {
            getActions().add(action);
        }

        @Override
        boolean apply(@NonNull ICell cell) {
            final String val = cell.getText();
            return apply(cell, val);
        }

        boolean apply(@NonNull ICell cell, @NonNull String val) {
            if (expr != null) {
                final Object result = expr.eval(cell);
                if (result == null) {
                    log.debug("Pattern {} could not be applied to the cell {}", this, cell);
                    return false;
                }
                val = result.toString();
            }

            final Component component = cell.createComponent(componentType, val);

            for (String tag : tags)
                component.addTag(tag);

            final List<Action> actions = getActions();
            for (Action action : actions)
                component.addAction(action);

            // Add the provenance
            component.setPattern(this);

            return true;
        }

    }

    /**
     * Represents a pattern for a choice.
     */
    public static final class ChoicePattern extends ComponentsPattern {
        ChoicePattern(ChoiceContext context) {
            super(context);
        }

        @NonNull
        @Getter
        @Setter
        private Condition condition;

        @NonNull
        @Getter
        @Setter
        private ComponentsPattern left;

        @NonNull
        @Getter
        @Setter
        private ComponentsPattern right;

        @Override
        void add(@NonNull Action action) {
            left.add(action);
            right.add(action);
        }

        @Override
        boolean apply(@NonNull ICell cell) {
            final boolean result = condition.check(cell);
            return result ? left.apply(cell) : right.apply(cell);
        }

    }

    /**
     * Represents a pattern for a struct.
     */
    @Slf4j
    public static final class StructPattern extends ComponentsPattern {
        StructPattern(@NonNull StructContext context) {
            super(context);
        }

        @Getter
        private final List<ComponentPattern> componentPatterns = new ArrayList<>();

        void add(@NonNull ComponentPattern pattern) {
            componentPatterns.add(pattern);
        }

        @Getter
        @Setter
        private String startText;

        @Getter
        @Setter
        private String endText;

        @Getter
        @Setter
        private List<String> separators;

        @Override
        void add(@NonNull Action action) {
            for (ComponentPattern componentPattern : componentPatterns) {
                componentPattern.add(action);
            }
        }

        @Override
        boolean apply(@NonNull ICell cell) {
            final String text = cell.getText();

            int start = 0;
            int end = text.length();

            boolean result;

            if (startText != null) {
                result = text.startsWith(startText);
                if (!result) {
                    log.debug("Pattern {} could not be applied to the cell {}", this, cell);
                    return false;
                }
                start = startText.length();
            }

            if (endText != null) {
                result = text.endsWith(endText);
                if (!result) {
                    log.debug("Pattern {} could not be applied to the cell {}", this, cell);
                    return false;
                }
                end = text.length() - endText.length();
            }

            final String subText = text.substring(start, end);

            // Если нет разделителей, то есть только один элемент
            if (separators == null) {
                if (componentPatterns.size() != 1) {
                    log.debug("Pattern {} could not be applied to the cell {}", this, cell);
                    return false;
                }
                final ComponentPattern componentPattern = componentPatterns.getFirst();
                result = componentPattern.apply(cell, subText);
                if (!result) {
                    log.debug("Pattern {} could not be applied to the cell {}", this, cell);
                    return false;
                }

                return true;
            }

            // Если есть n разделителей, то есть n-1 элементов
            if (componentPatterns.size() != separators.size() + 1) {
                log.debug("Pattern {} could not be applied to the cell {}", this, cell);
                return false;
            }

            start = 0;
            int shift = 0;
            for (int i = 0; i < componentPatterns.size(); i++) {
                final ComponentPattern componentPattern = componentPatterns.get(i);

                if (i < componentPatterns.size() - 1) {
                    final String unescapedSeparator = separators.get(i);
                    end = subText.indexOf(unescapedSeparator, start);
                    if (end == -1) {
                        final String separator = StringEscapeUtils.escapeJava(unescapedSeparator);
                        final String msg = String.format("Invalid separator: \"%s\" in \"%s\"", separator, cell);
                        throw new IllegalStateException(msg);
                    }
                    shift = unescapedSeparator.length();
                } else {
                    end = subText.length();
                }

                String val = subText.substring(start, end);
                result = componentPattern.apply(cell, val);
                if (!result) {
                    log.debug("Pattern {} could not be applied to the cell {}", this, cell);
                    return false;
                }

                start = end + shift;
            }

            return true;
        }

    }

    @Slf4j
    public static final class StructxPattern extends ComponentsPattern {
        StructxPattern(@NonNull StructxContext context) {
            super(context);
        }

        @Getter
        private final List<SubstructxPattern> substructxPatterns = new LinkedList<>();

        private final List<SubstructxPattern> tempSubstructxPatterns = new LinkedList<>();

        void add(@NonNull SubstructxPattern pattern) {
            if (pattern.startText != null) {
                if (!tempSubstructxPatterns.isEmpty()) {
                    Quantifier quantifier = pattern.getQuantifier();
                    Quantifier.Times times = quantifier.times();

                    if (times == ONE_OR_MORE || times == UNDEFINED
                            || (times == EXACTLY && (quantifier.exactly() > 0))) {
                        for (SubstructxPattern tempSubstructxPattern : tempSubstructxPatterns) {
                            tempSubstructxPattern.closeEndSeparator = pattern.startText;
                        }
                        tempSubstructxPatterns.clear();
                    } else {
                        for (SubstructxPattern tempSubstructxPattern : tempSubstructxPatterns) {
                            tempSubstructxPattern.openEndSeparators.add(pattern.startText);
                        }
                    }
                }
            }

            if (pattern.endText == null) {
                tempSubstructxPatterns.add(pattern);
            }

            substructxPatterns.add(pattern);
        }

        @Override
        void add(@NonNull Action action) {
            for (SubstructxPattern substructxPattern : substructxPatterns) {
                substructxPattern.add(action);
            }
        }

        @Override
        boolean apply(@NonNull ICell cell) {
            int textShift = 0;
            for (SubstructxPattern substructxPattern : substructxPatterns) {
                textShift = substructxPattern.apply(cell, textShift);
            }

            return true;
        }

    }

    @Slf4j
    public static final class SubstructxPattern extends RepeatablePattern {
        SubstructxPattern(@NonNull SubstructxContext context) {
            super(context);
        }

        @Getter
        private final List<ComponentPattern> componentPatterns = new ArrayList<>();

        void add(@NonNull ComponentPattern pattern) {
            componentPatterns.add(pattern);
        }

        @Getter
        @Setter
        private String startText;

        @Getter
        @Setter
        private String endText;

        @Getter
        @Setter
        private boolean looped;

        @Getter
        @Setter
        private String closeEndSeparator;

        @Getter
        @Setter
        private List<String> openEndSeparators = new LinkedList<>();

        @Getter
        @Setter
        private List<String> separators = new LinkedList<>();

        @Override
        void add(@NonNull Action action) {
            for (ComponentPattern componentPattern : componentPatterns) {
                componentPattern.add(action);
            }
        }

        private ICell currentCell;

        int apply(@NonNull ICell cell, int textShift) {
            currentCell = cell;

            final Quantifier.Times times = getQuantifier().times();

            if (times == Quantifier.Times.EXACTLY) {
                // Repeat exactly n times
                int count = getRepetitionCount();
                try {
                    while (count > 0) {
                        textShift = attemptPatternApplication(textShift);
                        count--;
                    }
                } catch (PatternApplicationException e) {
                    log.debug("Pattern {} could not be applied to the cell {}", this, cell);
                    final String msg = String.format("Pattern %s could not be applied to the cell %s", this, cell);
                    throw new RuntimeException(msg);
                }
                return textShift;
            } else if (times == Quantifier.Times.UNDEFINED) {
                // Repeat exactly one time
                try {
                    return attemptPatternApplication(textShift);
                } catch (PatternApplicationException e) {
                    log.debug("Pattern {} could not be applied to the cell {}", this, cell);
                    final String msg = String.format("Pattern %s could not be applied to the cell %s", this, cell);
                    throw new RuntimeException(msg);
                }
            } else if (times == ONE_OR_MORE) {
                // Repeat at least one time
                try {
                    attemptPatternApplication(textShift);
                } catch (PatternApplicationException e) {
                    log.debug("Pattern {} could not be applied to the cell {}", this, cell);
                    final String msg = String.format("Pattern %s could not be applied to the cell %s", this, cell);
                    throw new RuntimeException(msg);
                }
                // Repeat more times if possible
                try {
                    while (true) {
                        textShift = attemptPatternApplication(textShift);
                    }
                } catch (PatternApplicationException e) {
                    return textShift;
                }
            } else if (times == Quantifier.Times.ZERO_OR_MORE) {
                // Repeat zero or more times
                try {
                    while (true) {
                        textShift = attemptPatternApplication(textShift);
                    }
                } catch (PatternApplicationException e) {
                    return textShift;
                }
            } else if (times == Quantifier.Times.ZERO_OR_ONE) {
                // Repeat zero or one time
                try {
                    textShift = attemptPatternApplication(textShift);
                } catch (PatternApplicationException e) {
                    return textShift;
                }
            }

            throw new RuntimeException("Impossible");
        }

        private int findStartPos(String remainder) throws PatternApplicationException {
            if (startText != null) {
                boolean result = remainder.startsWith(startText);

                if (!result) {
                    throw new PatternApplicationException(currentCell, this);
                }

                return startText.length();
            }

            return 0;
        }

        private int findEndPos(String remainder) throws PatternApplicationException {
            int position;
            int startTextLength = startText == null ? 0 : startText.length();

            if (endText != null) {
                position = remainder.indexOf(endText, startTextLength);

                if (position < 1) {
                    throw new PatternApplicationException(currentCell, this);
                }

                return position + startTextLength;
            }

            if (looped && startText != null) {
                position = remainder.indexOf(startText, startTextLength);

                if (position > 0) {
                    return position + startTextLength;
                }
            }

            if (!openEndSeparators.isEmpty()) {
                for (String openEndSeparator : openEndSeparators) {
                    position = remainder.indexOf(openEndSeparator, startTextLength);

                    if (position > 0) {
                        return position + startTextLength;
                    }
                }
            }

            if (closeEndSeparator != null) {
                position = remainder.indexOf(closeEndSeparator, startTextLength);

                if (position < 1) {
                    throw new IllegalStateException("Invalid close end separator");
                }

                return position + startTextLength;
            }

            return remainder.length();
        }

        private int attemptPatternApplication(int textShift) throws PatternApplicationException {
            final String text = currentCell.getText();
            final int length = text.length();

            if (length - textShift <= 0) {
                throw new PatternApplicationException(currentCell, this);
            }

            final String remainder = text.substring(textShift, length);

            int start = findStartPos(remainder);
            int end = findEndPos(remainder);

            final String extractedText = remainder.substring(start, end);

            // Когда нет разделителей, тогда есть только один компонент
            if (separators == null) {
                if (componentPatterns.size() != 1) {
                    log.debug("Pattern {} could not be applied to the cell {}", this, currentCell);
                    throw new PatternApplicationException(currentCell, this);
                }
                final ComponentPattern componentPattern = componentPatterns.getFirst();
                boolean result = componentPattern.apply(currentCell, extractedText);
                if (!result) {
                    log.debug("Pattern {} could not be applied to the cell {}", this, currentCell);
                    throw new PatternApplicationException(currentCell, this);
                }

                // Вычислить и вернуть новый сдвиг
                return end + (endText == null ? 0 : endText.length());
            }

            // Когда есть n разделителей, тогда есть n-1 компонентов
            if (componentPatterns.size() != separators.size() + 1) {
                throw new PatternApplicationException(currentCell, this);
            }

            start = 0;
            int shift = 0;
            for (int i = 0; i < componentPatterns.size(); i++) {
                final ComponentPattern componentPattern = componentPatterns.get(i);

                if (i < componentPatterns.size() - 1) {
                    final String unescapedSeparator = separators.get(i);
                    end = extractedText.indexOf(unescapedSeparator, start);
                    if (end == -1) {
                        final String separator = StringEscapeUtils.escapeJava(unescapedSeparator);
                        final String msg = String.format("Invalid separator: \"%s\" in \"%s\"", separator, currentCell);
                        throw new IllegalStateException(msg);
                    }
                    shift = unescapedSeparator.length();
                } else {
                    end = extractedText.length();
                }

                String val = extractedText.substring(start, end);
                boolean result = componentPattern.apply(currentCell, val);
                if (!result) {
                    throw new PatternApplicationException(currentCell, this);
                }

                start = end + shift;
            }

            // Вычислить и вернуть новый сдвиг
            return textShift + end + (endText == null ? 0 : endText.length());
        }

    }

}