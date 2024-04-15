package com.regtab.core.rtl.interpreter;

import lombok.extern.slf4j.Slf4j;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import com.regtab.core.model.ICell;
import com.regtab.core.model.Action;
import com.regtab.core.model.Condition;
import com.regtab.core.model.Element;
import com.regtab.core.model.Expr;
import com.regtab.core.model.ITable;

import com.regtab.core.rtl.parser.RTLLexer;
import com.regtab.core.rtl.parser.RTLParser;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

import static com.regtab.core.rtl.parser.RTLParser.*;

public class RTLPattern {
    @Getter(AccessLevel.PACKAGE)
    private final TablePattern tablePattern;

    private RTLPattern(TablePattern tablePattern) {
        this.tablePattern = tablePattern;
    }

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

    public RTLMatcher matcher() {
        return new RTLMatcher(this);
    }

    public static TableMap match(@NonNull String rtl, @NonNull ITable table) {
        final RTLPattern t = compile(rtl);
        if  (t == null)
            return null;
        final RTLMatcher m = t.matcher();

        return m.match(table);
    }

    public static boolean apply(@NonNull String rtl, @NonNull ITable table) {
        final TableMap map = match(rtl, table);
        if  (map == null)
            return false;

        return map.apply();
    }

    @Getter
    private static final Configurator configurator = Configurator.DEFAULT_CONFIGURATOR;

    public static Configurator config() {
        return configurator;
    }

    abstract static class BasePattern {
        @Getter
        private final ParseTree tree;

        BasePattern(ParseTree tree) {
            this.tree = tree;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                    .append("ttl", tree.getText())
                    .toString();
        }
    }

    abstract static class ActionablePattern extends BasePattern {
        ActionablePattern(ParseTree tree) {
            super(tree);
        }

        @Getter
        private final List<Action> actions = new ArrayList<>(1);

        abstract void add(@NonNull Action action);

    }

    abstract static class RepeatablePattern extends ActionablePattern {
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

    static final class TablePattern extends BasePattern {
        TablePattern(@NonNull TableContext context) {
            super(context);
        }

        @Getter
        private final List<SubtablePattern> subtablePatterns = new ArrayList<>(1);

        void add(@NonNull RTLPattern.SubtablePattern pattern) {
            pattern.setTablePattern(this);
            subtablePatterns.add(pattern);
        }

    }

    static final class SubtablePattern extends RepeatablePattern {
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

        void add(@NonNull Action action) {
            getActions().add(action);

            for (RowPattern rowPattern : rowPatterns) {
                rowPattern.add(action);
            }
        }

    }

    static final class RowPattern extends RepeatablePattern {
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

        void add(@NonNull Action action) {
            getActions().add(action);

            for (SubrowPattern subrowPattern : subrowPatterns) {
                subrowPattern.add(action);
            }
        }

    }

    static final class SubrowPattern extends RepeatablePattern {
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

        void add(@NonNull Action action) {
            getActions().add(action);

            for (CellPattern cellPattern : cellPatterns) {
                cellPattern.add(action);
            }
        }
    }

    static final class CellPattern extends RepeatablePattern {
        CellPattern(CellContext context) {
            super(context);
        }

        @Getter
        @Setter
        private ElementsPattern elementsPattern;

        @Override
        void setCondition(@NonNull Condition condition) {
            super.setCondition(condition);
        }

        @Getter
        @Setter(AccessLevel.PACKAGE)
        private SubrowPattern subrowPattern;

        void add(@NonNull Action action) {
            getActions().add(action);
            if (elementsPattern != null)
                elementsPattern.add(action);
        }

        boolean apply(@NonNull ICell cell) {
            return elementsPattern.apply(cell);
        }
    }

    abstract static class ElementsPattern extends ActionablePattern {
        ElementsPattern(ParseTree tree) {
            super(tree);
        }

        abstract boolean apply(ICell cell);
    }

    @Slf4j
    static final class ElementPattern extends ElementsPattern {
        ElementPattern(@NonNull ElementContext context) {
            super(context);
        }

        @Getter
        @Setter
        private Element.Type elementType;

        @Getter
        @Setter
        private Expr expr;

        @Getter
        private final List<String> tags = new ArrayList<>();

        void add(@NonNull String tag) {
            tags.add(tag);
        }

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

            final Element element = cell.createElement(elementType, val);

            for (String tag : tags)
                element.addTag(tag);

            final List<Action> actions = getActions();
            for (Action action : actions)
                element.addAction(action);

            return true;
        }

    }

    static final class ChoicePattern extends ElementsPattern {
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
        private ElementsPattern left;

        @NonNull
        @Getter
        @Setter
        private ElementsPattern right;

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

    @Slf4j
    static final class StructPattern extends ElementsPattern {
        StructPattern(@NonNull StructContext context) {
            super(context);
        }

        @Getter
        private final List<ElementPattern> elementPatterns = new ArrayList<>();

        void add(@NonNull RTLPattern.ElementPattern pattern) {
            elementPatterns.add(pattern);
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

        void add(@NonNull Action action) {
            for (ElementPattern elementPattern : elementPatterns) {
                elementPattern.add(action);
            }
        }

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
                if (elementPatterns.size() != 1) {
                    log.debug("Pattern {} could not be applied to the cell {}", this, cell);
                    return false;
                }
                final ElementPattern elementPattern = elementPatterns.getFirst();
                result = elementPattern.apply(cell, subText);
                if (!result) {
                    log.debug("Pattern {} could not be applied to the cell {}", this, cell);
                    return false;
                }

                return true;
            }

            // Если есть n разделителей, то есть n-1 элементов
            if (elementPatterns.size() != separators.size() + 1) {
                log.debug("Pattern {} could not be applied to the cell {}", this, cell);
                return false;
            }

            start = 0;
            int shift = 0;
            for (int i = 0; i < elementPatterns.size(); i++) {
                final ElementPattern elementPattern = elementPatterns.get(i);

                if (i < elementPatterns.size() - 1) {
                    String separator = separators.get(i);
                    end = subText.indexOf(separator, start);
                    shift = separator.length();
                } else {
                    end = subText.length();
                }

                String val = subText.substring(start, end);
                result = elementPattern.apply(cell, val);
                if (!result) {
                    log.debug("Pattern {} could not be applied to the cell {}", this, cell);
                    return false;
                }

                start = end + shift;
            }

            return true;
        }

    }

}