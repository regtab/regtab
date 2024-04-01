package com.regtab.core.rtl.interpreter.visitor;

import com.regtab.core.model.semantics.Condition;
import com.regtab.core.model.semantics.Lookup;
import com.regtab.core.model.semantics.Range;
import com.regtab.core.rtl.parser.RTLBaseVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

import com.regtab.core.rtl.parser.RTLParser.*;

final class LookupVisitor extends RTLBaseVisitor<Lookup> {
    private static final CondVisitor condVisitor = new CondVisitor();

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
