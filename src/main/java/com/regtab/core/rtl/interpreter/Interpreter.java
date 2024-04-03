package com.regtab.core.rtl.interpreter;

import lombok.NonNull;

import com.regtab.core.model.semantics.*;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.regtab.core.rtl.parser.RTLBaseVisitor;

import static com.regtab.core.rtl.interpreter.RTLPattern.*;
import static com.regtab.core.rtl.parser.RTLParser.*;

final class Interpreter {
    static TablePattern compile(@NonNull ParseTree tree) {
        return tableVisitor.visit(tree);
    }

    private Interpreter() {
    }

    // Visitors

    private static final TableVisitor tableVisitor = new TableVisitor();
    private final static SubtableVisitor subtableVisitor = new SubtableVisitor();
    private static final RowVisitor rowVisitor = new RowVisitor();
    private static final SubrowVisitor subrowVisitor = new SubrowVisitor();
    private static final CellVisitor cellVisitor = new CellVisitor();

    private static final ElementVisitor elementVisitor = new ElementVisitor();
    private static final StructVisitor structVisitor = new StructVisitor();
    private static final ChoiceVisitor choiceVisitor = new ChoiceVisitor();

    private static final CondVisitor condVisitor = new CondVisitor();
    private static final QuantifierVisitor quantifierVisitor = new QuantifierVisitor();
    private static final ActionVisitor actionVisitor = new ActionVisitor();
    private static final ExprVisitor exprVisitor = new ExprVisitor();

    final static class TableVisitor extends RTLBaseVisitor<TablePattern> {
        @Override
        public TablePattern visitTable(TableContext ctx) {
            final List<SubtableContext> subtableCtxList = ctx.subtable();

            if (subtableCtxList == null || subtableCtxList.isEmpty())
                return null; // Impossible

            final TablePattern pattern = new TablePattern(ctx);
            for (SubtableContext subtableCtx : subtableCtxList) {
                final SubtablePattern subtablePattern = subtableVisitor.visit(subtableCtx);
                if (subtablePattern == null)
                    return null; // Impossible
                pattern.add(subtablePattern);
            }
            final List<SubtablePattern> subtablePatterns = pattern.getSubtablePatterns();
            return subtablePatterns.isEmpty() ? null : pattern;
        }
    }

    final static class SubtableVisitor extends RTLBaseVisitor<SubtablePattern> {
        @Override
        public SubtablePattern visitSubtable(SubtableContext ctx) {
            final SubtablePattern subtablePattern = new SubtablePattern(ctx);

            final List<RowContext> rowContexts = ctx.row();
            if (rowContexts != null && !rowContexts.isEmpty()) {
                final boolean result = apply(rowContexts, subtablePattern);
                if (!result)
                    return null; // Impossible

                return subtablePattern;
            }

            final RowsContext rowsContext = ctx.rows();
            if (rowsContext != null) {
                final boolean result = apply(rowsContext, subtablePattern);
                if (!result)
                    return null; // Impossible

                return subtablePattern;
            }

            return null; // Impossible
        }

        private boolean apply(List<RowContext> rowContexts, SubtablePattern pattern) {
            for (RowContext ctx : rowContexts) {
                RowPattern rowPattern = rowVisitor.visit(ctx);
                if (rowPattern == null)
                    return false; // Impossible

                pattern.add(rowPattern);
            }

            final Quantifier quantifier = new Quantifier(Quantifier.Times.UNDEFINED, null);
            pattern.setQuantifier(quantifier);

            return true;
        }

        private boolean apply(RowsContext rowsContext, SubtablePattern pattern) {
            pattern.rowsContext = rowsContext;

            // Instruction order matter
            final List<RowContext> rowContexts = rowsContext.row();
            for (RowContext ctx : rowContexts) {
                RowPattern rowPattern = rowVisitor.visit(ctx);
                if (rowPattern == null)
                    return false; // Impossible
                pattern.add(rowPattern);
            }

            final CondContext condContext = rowsContext.cond();
            if (condContext != null) {
                final Condition condition = condVisitor.visit(condContext);
                if (condition == null)
                    return false; // Impossible

                pattern.setCondition(condition);
            }

            final Quantifier quantifier;
            final QuantifierContext quantifierContext = rowsContext.quantifier();
            if (quantifierContext == null) {
                quantifier = new Quantifier(Quantifier.Times.UNDEFINED, null);
            } else {
                quantifier = quantifierVisitor.visit(quantifierContext);
                if (quantifier == null)
                    return false; // Impossible
            }
            pattern.setQuantifier(quantifier);

            final ActionsContext actionsCtx = rowsContext.actions();
            if (actionsCtx != null) {
                final List<ActionContext> actionCtxList = actionsCtx.action();
                if (actionCtxList != null) {
                    for (ActionContext actionCtx : actionCtxList) {
                        Action action = actionVisitor.visit(actionCtx);
                        if (action == null)
                            return false; // Impossible
                        pattern.add(action);
                    }
                }
            }

            return true;
        }
    }

    final static class RowVisitor extends RTLBaseVisitor<RowPattern> {
        private static final HashMap<String, SubrowsContext> store = new HashMap<>();

        @Override
        public RowPattern visitRow(RowContext ctx) {
            final RowPattern rowPattern = new RowPattern(ctx);

            final SubrowsContext subrowsContext;
            final CopyContext copyContext = ctx.copy();
            if (copyContext != null) {
                String label = copyContext.TAG().getText();
                subrowsContext = store.get(label);
                if (subrowsContext == null) {
                    final String msg = String.format("undefined label \"%s\"", label);
                    throw new RTLSyntaxException(msg, ctx);
                }
            } else {
                subrowsContext = ctx.subrows();
            }

            final LabelContext labelContext = ctx.label();
            if (labelContext != null) {
                String label = labelContext.TAG().getText();
                store.put(label, subrowsContext);
            }

            rowPattern.subrowsContext = subrowsContext;

            // Instruction order matter
            final List<SubrowContext> subrowContexts = subrowsContext.subrow();
            for (SubrowContext subrowContext : subrowContexts) {
                final SubrowPattern subrowPattern = subrowVisitor.visit(subrowContext);
                rowPattern.add(subrowPattern);
            }

            final CondContext condContext = subrowsContext.cond();
            if (condContext != null) {
                final Condition condition = condVisitor.visit(condContext);
                rowPattern.setCondition(condition);
            }

            final QuantifierContext quantifierContext = ctx.quantifier();
            final Quantifier quantifier;
            if (quantifierContext != null)
                quantifier = quantifierVisitor.visit(quantifierContext);
            else
                quantifier = new Quantifier(Quantifier.Times.UNDEFINED, null);

            rowPattern.setQuantifier(quantifier);

            final ActionsContext actionsCtx = subrowsContext.actions();
            if (actionsCtx != null) {
                List<ActionContext> actionCtxList = actionsCtx.action();
                if (actionCtxList != null) {
                    for (ActionContext actionCtx : actionCtxList) {
                        Action action = actionVisitor.visit(actionCtx);
                        if (action == null)
                            return null; // Impossible
                        rowPattern.add(action);
                    }
                }
            }

            return rowPattern;
        }

    }

    final static class SubrowVisitor extends RTLBaseVisitor<SubrowPattern> {
        @Override
        public SubrowPattern visitSubrow(SubrowContext ctx) {
            final SubrowPattern subrowPattern = new SubrowPattern(ctx);

            final List<CellContext> cellContexts = ctx.cell();
            if (cellContexts != null && !cellContexts.isEmpty()) {
                boolean result = apply(cellContexts, subrowPattern);
                if (!result)
                    return null; // Impossible

                return subrowPattern;
            }

            final CellsContext cellsContext = ctx.cells();
            if (cellsContext != null) {
                boolean result = apply(cellsContext, subrowPattern);
                if (!result)
                    return null; // Impossible

                return subrowPattern;
            }

            return null; // Impossible
        }

        private boolean apply(List<CellContext> cellContexts, SubrowPattern pattern) {
            for (CellContext ctx : cellContexts) {
                CellPattern cellPattern = cellVisitor.visit(ctx);
                if (cellPattern == null)
                    return false; // Impossible

                pattern.add(cellPattern);
            }

            final Quantifier quantifier = new Quantifier(Quantifier.Times.UNDEFINED, null);
            pattern.setQuantifier(quantifier);

            return true;
        }

        private boolean apply(CellsContext cellsContext, SubrowPattern pattern) {
            pattern.cellsContext = cellsContext;

            // Instruction order matter
            final List<CellContext> cellContexts = cellsContext.cell();
            for (CellContext ctx : cellContexts) {
                CellPattern cellPattern = cellVisitor.visit(ctx);
                if (cellPattern == null)
                    return false; // Impossible

                pattern.add(cellPattern);
            }

            final CondContext condContext = cellsContext.cond();
            if (condContext != null) {
                final Condition condition = condVisitor.visit(condContext);
                if (condition == null)
                    return false; // Impossible

                pattern.setCondition(condition);
            }

            final Quantifier quantifier;
            final QuantifierContext quantifierContext = cellsContext.quantifier();
            if (quantifierContext == null) {
                quantifier = new Quantifier(Quantifier.Times.UNDEFINED, null);
            } else {
                quantifier = quantifierVisitor.visit(quantifierContext);
                if (quantifier == null)
                    return false; // Impossible
            }
            pattern.setQuantifier(quantifier);

            final ActionsContext actionsCtx = cellsContext.actions();
            if (actionsCtx != null) {
                List<ActionContext> actionCtxList = actionsCtx.action();
                if (actionCtxList != null) {
                    for (ActionContext actionCtx : actionCtxList) {
                        Action action = actionVisitor.visit(actionCtx);
                        if (action == null)
                            return false; // Impossible
                        pattern.add(action);
                    }
                }
            }

            return true;
        }

    }

    final static class CellVisitor extends RTLBaseVisitor<CellPattern> {
        private static final HashMap<String, ElementsContext> store = new HashMap<>();

        @Override
        public CellPattern visitCell(CellContext ctx) {
            final CellPattern cellPattern = new CellPattern(ctx);

            final ElementsContext elementsContext;
            final CopyContext copyContext = ctx.copy();
            if (copyContext != null) {
                String label = copyContext.TAG().getText();
                elementsContext = store.get(label);
                if (elementsContext == null) {
                    final String msg = String.format("undefined label \"%s\"", label);
                    throw new RTLSyntaxException(msg, ctx);
                }
            } else {
                elementsContext = ctx.elements();
            }

            final LabelContext labelContext = ctx.label();
            if (labelContext != null) {
                String label = labelContext.TAG().getText();
                store.put(label, elementsContext);
            }

            final boolean result = apply(cellPattern, elementsContext);
            if (!result)
                return null; // Impossible

            final CondContext condContext = elementsContext.cond();
            if (condContext != null) {
                final Condition condition = condVisitor.visit(condContext);
                cellPattern.setCondition(condition);
            }

            final QuantifierContext quantifierContext = ctx.quantifier();
            final Quantifier quantifier;
            if (quantifierContext != null)
                quantifier = quantifierVisitor.visit(quantifierContext);
            else
                quantifier = new Quantifier(Quantifier.Times.UNDEFINED, null);

            cellPattern.setQuantifier(quantifier);

            final ActionsContext actionsCtx = elementsContext.actions();
            if (actionsCtx != null) {
                List<ActionContext> actionCtxList = actionsCtx.action();
                if (actionCtxList != null) {
                    for (ActionContext actionCtx : actionCtxList) {
                        Action action = actionVisitor.visit(actionCtx);
                        if (action == null)
                            return null; // Impossible
                        cellPattern.add(action);
                    }
                }
            }

            return cellPattern;
        }

        private boolean apply(CellPattern pattern, ElementsContext ctx) {
            final ElementContext elementContext = ctx.element();
            if (elementContext != null) {
                final ElementPattern elementPattern = elementVisitor.visit(elementContext);
                if (elementPattern == null)
                    return false; // Impossible

                pattern.setElementsPattern(elementPattern);
                return true;
            }

            final StructContext structuredContext = ctx.struct();
            if (structuredContext != null) {
                final StructPattern structPattern = structVisitor.visit(structuredContext);
                if (structPattern == null)
                    return false; // Impossible

                pattern.setElementsPattern(structPattern);
                return true;
            }

            final ChoiceContext choiceContext = ctx.choice();
            if (choiceContext != null) {
                final ChoicePattern choicePattern = choiceVisitor.visit(choiceContext);
                if (choicePattern == null)
                    return false; // Impossible

                pattern.setElementsPattern(choicePattern);
            }

            return true;
        }

    }

    final static class ElementVisitor extends RTLBaseVisitor<ElementPattern> {
        @Override
        public ElementPattern visitElement(ElementContext ctx) {
            final ElementTypeContext etCtx = ctx.elementType();

            final Element.Type elementType;
            if (etCtx.VALUE() != null)
                elementType = Element.Type.VALUE;
            else if (etCtx.ATTRIBUTE() != null)
                elementType = Element.Type.ATTRIBUTE;
            else if (etCtx.SKIPPED() != null)
                elementType = Element.Type.SKIPPED;
            else
                elementType = null; // Impossible

            final ElementPattern elementPattern = new ElementPattern(ctx);
            elementPattern.setElementType(elementType);

            final ExprContext exprContext = ctx.expr();
            if (exprContext != null) {
                final Expr expr = exprVisitor.visit(exprContext);
                if (expr == null)
                    return null; // Impossible
                elementPattern.setExpr(expr);
            }

            final TagsContext tagsCtx = ctx.tags();
            if (tagsCtx != null) {
                final List<TerminalNode> tns = tagsCtx.TAG();
                for (TerminalNode tn : tns) {
                    String tag = tn.getText();
                    elementPattern.add(tag);
                }
            }

            final ActionsContext actionsCtx = ctx.actions();
            if (actionsCtx != null) {
                final List<ActionContext> actionCtxList = actionsCtx.action();
                if (actionCtxList != null) {
                    for (ActionContext actionCtx : actionCtxList) {
                        final Action action = actionVisitor.visit(actionCtx);
                        if (action == null)
                            return null; // Impossible
                        elementPattern.add(action);
                    }
                }
            }

            return elementPattern;

        }
    }

    final static class StructVisitor extends RTLBaseVisitor<StructPattern> {
        @Override
        public StructPattern visitStruct(StructContext ctx) {
            final StructPattern structPattern = new StructPattern(ctx);

            final List<ElementContext> elementContexts = ctx.element();
            for (ElementContext elemCtx : elementContexts) {
                final ElementPattern elementPattern = elementVisitor.visit(elemCtx);
                if (elementPattern == null)
                    return null; // Impossible

                structPattern.add(elementPattern);
            }

            final StartTextContext stCtx = ctx.startText();
            if (stCtx != null) {
                final String startText = stCtx.STRING().getText();
                structPattern.setStartText(startText);
            }

            final EndTextContext etCtx = ctx.endText();
            if (etCtx != null) {
                final String endText = etCtx.STRING().getText();
                structPattern.setEndText(endText);
            }

            final List<SeparatorContext> separatorContexts = ctx.separator();
            if (separatorContexts != null && !separatorContexts.isEmpty()) {
                final List<String> separators = new ArrayList<>(separatorContexts.size());
                for (SeparatorContext separatorContext : separatorContexts) {
                    String separator = separatorContext.STRING().getText();
                    separators.add(separator);
                }
                structPattern.setSeparators(separators);
            }

            return structPattern;
        }

    }

    final static class ChoiceVisitor extends RTLBaseVisitor<ChoicePattern> {
        @Override
        public ChoicePattern visitChoice(ChoiceContext ctx) {
            final ChoicePattern choicePattern = new ChoicePattern(ctx);

            ChoiceBodyContext choiceBodyContext;

            choiceBodyContext = ctx.choiceBody(0);
            final ElementsPattern left = createVariant(choiceBodyContext);
            if (left == null)
                return null; // Impossible
            choicePattern.setLeft(left);

            choiceBodyContext = ctx.choiceBody(1);
            final ElementsPattern right = createVariant(choiceBodyContext);
            if (right == null)
                return null; // Impossible
            choicePattern.setRight(right);

            final CondContext condContext = ctx.cond();
            if (condContext != null) {
                final Condition condition = condVisitor.visit(condContext);
                if (condition == null)
                    return null; // Impossible
                choicePattern.setCondition(condition);
            }

            return choicePattern;
        }

        private ElementsPattern createVariant(ChoiceBodyContext ctx) {
            final ElementContext elementContext = ctx.element();
            if (elementContext != null) {
                return elementVisitor.visit(elementContext);
            }

            final StructContext structuredContext = ctx.struct();
            if (structuredContext != null) {
                return structVisitor.visit(structuredContext);
            }

            return null; // Impossible
        }

    }

    final static class CondVisitor extends RTLBaseVisitor<Condition> {
        @Override
        public Condition visitCond(CondContext ctx) {
            final Condition cond = new Condition();
            final List<ExprContext> exprCtxList = ctx.expr();

            for (ExprContext exprCtx : exprCtxList) {
                final Expr expr = exprVisitor.visit(exprCtx);

                if (expr == null)
                    return null; // Impossible

                final Constraint constr = new Constraint(expr);
                cond.addConstraint(constr);
            }

            return cond;
        }
    }

    final static class QuantifierVisitor extends RTLBaseVisitor<Quantifier> {
        public Quantifier visit(QuantifierContext ctx) {
            return visitQuantifier(ctx);
        }

        @Override
        public Quantifier visitQuantifier(QuantifierContext ctx) {
            Quantifier.Times times = null;
            Integer exactly = null;

            if (ctx != null) {
                ExactlyContext exactlyCtx = ctx.exactly();
                if (exactlyCtx != null) {
                    TerminalNode tn = exactlyCtx.INT();
                    times = Quantifier.Times.EXACTLY;
                    String str = tn.getText();
                    exactly = Integer.parseInt(str);
                    if (exactly < 0) {
                        return null; // Impossible
                    }
                } else {
                    if (ctx.oneOrMore() != null)
                        times = Quantifier.Times.ONE_OR_MORE;
                    if (ctx.zeroOrMore() != null)
                        times = Quantifier.Times.ZERO_OR_MORE;
                    if (ctx.zeroOrOne() != null)
                        times = Quantifier.Times.ZERO_OR_ONE;
                }
            } else {
                times = Quantifier.Times.UNDEFINED;
            }

            return new Quantifier(times, exactly);
        }
    }

    final static class ActionVisitor extends RTLBaseVisitor<Action> {
        private static final LookupVisitor lookupVisitor = new LookupVisitor();

        @Override
        public Action visitAction(ActionContext ctx) {
            final List<ActionBodyContext> actionBodyCtxList = ctx.actionBody();
            final ActionTypeContext actionTypeCtx = ctx.actionType();

            final Action.Type actType;

            if (actionTypeCtx.FACTOR() != null)
                actType = Action.Type.FACTOR;
            else if (actionTypeCtx.PREFIX() != null)
                actType = Action.Type.PREFIX;
            else if (actionTypeCtx.SUFFIX() != null)
                actType = Action.Type.SUFFIX;
            else if (actionTypeCtx.RECORD() != null)
                actType = Action.Type.RECORD;
            else if (actionTypeCtx.GROUP() != null)
                actType = Action.Type.GROUP;
            else if (actionTypeCtx.SCHEMA() != null)
                actType = Action.Type.SCHEMA;
            else
                return null; // Impossible

            final Action action = new Action(actType);

            final Configurator configurator = getConfigurator();
            if (configurator != null) {
                final String concatSeparator = configurator.getConcatSeparator();
                action.setConcatSeparator(concatSeparator);

                final String avSeparator = configurator.getAvSeparator();
                action.setAvSeparator(avSeparator);
            }

            if (actionBodyCtxList == null)
                return null; // Impossible

            for (ActionBodyContext actionBodyCtx : actionBodyCtxList) {
                final TerminalNode tn = actionBodyCtx.STRING();

                if (tn != null) {
                    final String string = tn.getText();
                    action.addString(string);
                } else {
                    final LookupContext lookupCtx = actionBodyCtx.lookup();
                    final Lookup lookup = lookupVisitor.visit(lookupCtx);
                    if (lookup == null)
                        return null; // Impossible
                    action.addLookup(lookup);
                }
            }

            return action;
        }
    }

    final static class LookupVisitor extends RTLBaseVisitor<Lookup> {
        @Override
        public Lookup visitLookup(LookupContext ctx) {
            final DirectionContext directionCtx = ctx.direction();
            final Lookup.Direction direction;

            if (directionCtx.LEFT() != null)
                direction = Lookup.Direction.LEFT;
            else if (directionCtx.RIGHT() != null)
                direction = Lookup.Direction.RIGHT;
            else if (directionCtx.UP() != null)
                direction = Lookup.Direction.UP;
            else if (directionCtx.DOWN() != null)
                direction = Lookup.Direction.DOWN;
            else if (directionCtx.INROW() != null)
                direction = Lookup.Direction.IN_ROW;
            else if (directionCtx.INCOL() != null)
                direction = Lookup.Direction.IN_COL;
            else if (directionCtx.INCELL() != null)
                direction = Lookup.Direction.IN_CELL;
            else
                return null; // Impossible

            final Lookup lookup = new Lookup(direction);

            final AllContext allCtx = ctx.all();
            if (allCtx != null)
                lookup.setAll(true);

            final WhereContext whereCtx = ctx.where();

            if (whereCtx != null) {
                final RangeContext rangeCtx = whereCtx.range();

                if (rangeCtx != null) {
                    final RowRangeContext rowRangeCtx = rangeCtx.rowRange();
                    if (rowRangeCtx != null) {
                        final Range.Desc rowRangeDesc = new Range.Desc(true);
                        final RangeBodyContext rbCtx = rowRangeCtx.rangeBody();
                        apply(rowRangeDesc, rbCtx);
                        lookup.setRowRangeDesc(rowRangeDesc);
                    }
                    final ColRangeContext colRangeCtx = rangeCtx.colRange();
                    if (colRangeCtx != null) {
                        final RangeBodyContext rbCtx = colRangeCtx.rangeBody();
                        final Range.Desc colRangeDesc = new Range.Desc(false);
                        apply(colRangeDesc, rbCtx);
                        lookup.setColRangeDesc(colRangeDesc);
                    }
                }

                final ElementIndexContext elementIndexContext = whereCtx.elementIndex();
                if (elementIndexContext != null) {
                    final TerminalNode tn = elementIndexContext.INT();
                    final int index = Integer.parseInt(tn.getText());
                    lookup.setElementIndex(index);
                }

                final TagsContext tagsCtx = whereCtx.tags();
                if (tagsCtx != null) {
                    final List<TerminalNode> tns = tagsCtx.TAG();
                    for (TerminalNode tn : tns) {
                        String tag = tn.getText();
                        lookup.addTag(tag);
                    }
                }
            }

            final CondContext condCtx = ctx.cond();
            if (condCtx != null) {
                final Condition cond = condVisitor.visit(condCtx);
                if (cond == null)
                    return null; // Impossible
                lookup.setCondition(cond);
            }

            return lookup;
        }

        private void apply(final Range.Desc rangeDesc, RangeBodyContext ctx) {
            int start;
            int end;
            boolean useRelativeStart = false;
            boolean useRelativeEnd = false;

            String literal;
            RelativeContext relativeContext;

            final TerminalNode tn = ctx.INT();
            if (tn != null) {
                literal = tn.getText();
                start = end = Integer.parseInt(literal);
                relativeContext = ctx.relative();
                if (relativeContext != null) {
                    useRelativeStart = useRelativeEnd = true;
                    final TerminalNode minus = relativeContext.MINUS();
                    if (minus != null) {
                        start = -start;
                        end = -end;
                    }
                }
            } else {
                final StartContext startCtx = ctx.start();
                literal = startCtx.INT().getText();
                start = Integer.parseInt(literal);
                relativeContext = startCtx.relative();
                if (relativeContext != null) {
                    useRelativeStart = true;
                    final TerminalNode minus = relativeContext.MINUS();
                    if (minus != null) {
                        start = -start;
                    }
                }

                final EndContext endCtx = ctx.end();
                literal = endCtx.INT().getText();
                end = Integer.parseInt(literal);
                relativeContext = startCtx.relative();
                if (relativeContext != null) {
                    useRelativeEnd = true;
                    final TerminalNode minus = relativeContext.MINUS();
                    if (minus != null) {
                        end = -end;
                    }
                }
            }

            rangeDesc.setStart(start);
            rangeDesc.setUseRelativeStart(useRelativeStart);
            rangeDesc.setEnd(end);
            rangeDesc.setUseRelativeEnd(useRelativeEnd);
        }

    }

    final static class ExprVisitor extends RTLBaseVisitor<Expr> {
        @Override
        public Expr visitIntLiteral(IntLiteralContext ctx) {
            final String str = ctx.INT().getText();
            final Integer i = Integer.valueOf(str);

            return Expr.builder().integer(i).build();
        }

        @Override
        public Expr visitProp(PropContext ctx) {
            final String id = ctx.getText();
            final Prop<?> prop = Prop.get(id);

            if (prop == null) {
                final String msg = String.format("undefined property \"%s\"", id);
                throw new RTLSyntaxException(msg, ctx);
            }

            return Expr.builder().prop(prop).build();
        }

        @Override
        public Expr visitPropExpr(PropExprContext ctx) {
            return super.visit(ctx.prop());
        }

        @Override
        public Expr visitFunc(FuncContext ctx) {
            final String id = ctx.ID().getText();
            final Func<?> func = Func.get(id);
            if (func == null) {
                final String msg = String.format("undefined function \"%s\"", id);
                throw new RTLSyntaxException(msg, ctx);
            }

            final List<ArgContext> argCtxList = ctx.arg();
            if (argCtxList != null) {
                for (ArgContext argCtx : argCtxList) {
                    if (argCtx.INT() != null) {
                        final String str = argCtx.INT().getText();
                        final Integer integer = Integer.valueOf(str);
                        func.addArg(integer);
                    } else if (argCtx.STRING() != null) {
                        final String str = argCtx.STRING().getText();
                        func.addArg(str);
                    }
                }
            }

            return Expr.builder().func(func).build();
        }

        @Override
        public Expr visitFuncExpr(FuncExprContext ctx) {
            return super.visit(ctx.func());
        }

        @Override
        public Expr visitNotExpr(NotExprContext ctx) {
            final Expr boolExpr = visit(ctx.expr());

            if (boolExpr == null)
                return null; // Impossible

            return Expr.builder().notExpr(boolExpr).build();
        }

        @Override
        public Expr visitParenExpr(ParenExprContext ctx) {
            return visit(ctx.expr());
        }

        @Override
        public Expr visitCompExpr(CompExprContext ctx) {
            final Expr left = this.visit(ctx.leftExpr);
            final Expr right = this.visit(ctx.rightExpr);

            if (left == null || right == null)
                return null; // Impossible

            if (ctx.op.EQ() != null)
                return Expr.builder().compOperator(Expr.CompOperator.EQUAL).left(left).right(right).build();

            if (ctx.op.NEQ() != null)
                return Expr.builder().compOperator(Expr.CompOperator.NOT_EQUAL).left(left).right(right).build();

            if (ctx.op.GT() != null)
                return Expr.builder().compOperator(Expr.CompOperator.GREATER).left(left).right(right).build();

            if (ctx.op.GE() != null)
                return Expr.builder().compOperator(Expr.CompOperator.GREATER_OR_EQUAL).left(left).right(right).build();

            if (ctx.op.LT() != null)
                return Expr.builder().compOperator(Expr.CompOperator.LESS).left(left).right(right).build();

            if (ctx.op.LE() != null)
                return Expr.builder().compOperator(Expr.CompOperator.LESS_OR_EQUAL).left(left).right(right).build();

            if (ctx.op.CONTAINS() != null)
                return Expr.builder().compOperator(Expr.CompOperator.CONTAINS).left(left).right(right).build();

            if (ctx.op.MATCHES() != null)
                return Expr.builder().compOperator(Expr.CompOperator.MATCHES).left(left).right(right).build();

            return null; // Impossible
        }

        @Override
        public Expr visitBinaryExpr(BinaryExprContext ctx) {
            final Expr left = this.visit(ctx.leftExpr);
            final Expr right = this.visit(ctx.rightExpr);

            if (left == null || right == null)
                return null; // Impossible

            if (ctx.op.AND() != null)
                return Expr.builder().binaryOperator(Expr.BinaryOperator.AND).left(left).right(right).build();

            if (ctx.op.OR() != null)
                return Expr.builder().binaryOperator(Expr.BinaryOperator.OR).left(left).right(right).build();

            return null; // Impossible
        }

        @Override
        public Expr visitArithmExpr(ArithmExprContext ctx) {
            final Expr left = this.visit(ctx.leftExpr);
            final Expr right = this.visit(ctx.rightExpr);

            if (left == null || right == null)
                return null; // Impossible

            if (ctx.op.PLUS() != null)
                return Expr.builder().arithmOperator(Expr.ArithmOperator.SUM).left(left).right(right).build();

            if (ctx.op.MINUS() != null)
                return Expr.builder().arithmOperator(Expr.ArithmOperator.SUB).left(left).right(right).build();

            if (ctx.op.MULT() != null)
                return Expr.builder().arithmOperator(Expr.ArithmOperator.MUL).left(left).right(right).build();

            if (ctx.op.MOD() != null)
                return Expr.builder().arithmOperator(Expr.ArithmOperator.MOD).left(left).right(right).build();

            return null; // Impossible
        }

        @Override
        public Expr visitStrExpr(StrExprContext ctx) {
            final Expr left = this.visit(ctx.leftExpr);
            final Expr right = this.visit(ctx.rightExpr);

            if (left == null || right == null)
                return null; // Impossible

            if (ctx.op.PLUS() != null)
                return Expr.builder().strOperator(Expr.StrOperator.CONCAT).left(left).right(right).build();

            return null; // Impossible
        }

        @Override
        public Expr visitBoolLiteral(BoolLiteralContext ctx) {
            if (ctx.bool().TRUE() != null)
                return Expr.builder().bool(true).build();

            if (ctx.bool().FALSE() != null)
                return Expr.builder().bool(false).build();

            return null; // Impossible
        }

        @Override
        public Expr visitStrLiteral(StrLiteralContext ctx) {
            final String str = ctx.getText();
            if (str == null)
                return null; // Impossible

            return Expr.builder().string(str).build();
        }

        @Override
        public Expr visitHexLiteral(HexLiteralContext ctx) {
            final String hex = ctx.getText().toLowerCase();
            if (!hex.matches("[a-f0-9]{6}")) {
                final String msg = String.format("incorrect hexadecimal \"%s\"", hex);
                throw new RTLSyntaxException(msg, ctx);
            }

            return Expr.builder().hex(hex).build();
        }

        @Override
        public Expr visitThisExpr(ThisExprContext ctx) {
            final Expr expr;

            final PropContext propCtx = ctx.prop();
            if (propCtx != null) {
                expr = super.visit(propCtx);
            } else {
                final FuncContext funcCtx = ctx.func();
                if (funcCtx != null)
                    expr = super.visit(funcCtx);
                else
                    return null; // Impossible
            }

            return expr.toBuilder().useCaller(true).build();
        }
    }
}
