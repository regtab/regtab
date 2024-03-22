package com.regtab.core.rtl.interpreter.visitor;

import com.regtab.core.model.Condition;
import com.regtab.core.model.Lookup;
import com.regtab.core.model.RangeDesc;
import com.regtab.core.rtl.parser.RTLBaseVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

import com.regtab.core.rtl.parser.RTLParser.*;

final class LookupVisitor extends RTLBaseVisitor<Lookup> {
    private static final CondVisitor condVisitor = new CondVisitor();

    @Override
    public Lookup visitLookup(LookupContext ctx) {
        DirectionContext directionCtx = ctx.direction();
        Lookup.Direction direction = null;

        if (directionCtx.LEFT() != null) direction = Lookup.Direction.LEFT;
        if (directionCtx.RIGHT() != null) direction = Lookup.Direction.RIGHT;
        if (directionCtx.UP() != null) direction = Lookup.Direction.UP;
        if (directionCtx.DOWN() != null) direction = Lookup.Direction.DOWN;
        if (directionCtx.INROW() != null) direction = Lookup.Direction.IN_ROW;
        if (directionCtx.INCOL() != null) direction = Lookup.Direction.IN_COL;
        if (directionCtx.INCELL() != null) direction = Lookup.Direction.IN_CELL;

        final Lookup lookup = new Lookup(direction);

        AllContext allCtx = ctx.all();
        if (allCtx != null)
            lookup.setAll(true);

        WhereContext whereCtx = ctx.where();

        if (whereCtx != null) {
            RangeContext rangeCtx = whereCtx.range();

            if (rangeCtx != null) {
                RowRangeContext rowRangeCtx = rangeCtx.rowRange();
                if (rowRangeCtx != null) {
                    final RangeDesc rowRangeDesc = new RangeDesc(RangeDesc.Type.ROW);
                    RangeBodyContext rbCtx = rowRangeCtx.rangeBody();
                    apply(rowRangeDesc, rbCtx);
                    lookup.setRowRangeDesc(rowRangeDesc);
                }
                ColRangeContext colRangeCtx = rangeCtx.colRange();
                if (colRangeCtx != null) {
                    RangeBodyContext rbCtx = colRangeCtx.rangeBody();
                    final RangeDesc colRangeDesc = new RangeDesc(RangeDesc.Type.COL);
                    apply(colRangeDesc, rbCtx);
                    lookup.setColRangeDesc(colRangeDesc);
                }
            }

            IndexContext idxCtx = whereCtx.index();
            if (idxCtx != null) {
                TerminalNode tn = idxCtx.INT();
                int index = Integer.parseInt(tn.getText());
                lookup.setIndex(index);
            }

            TagsContext tagsCtx = whereCtx.tags();
            if (tagsCtx != null) {
                List<TerminalNode> tns = tagsCtx.TAG();
                for (TerminalNode tn : tns) {
                    String tag = tn.getText();
                    lookup.addTag(tag);
                }
            }
        }
        //logger.info("Set search area {} to lookup {}", searchArea, lookup);

        CondContext condCtx = ctx.cond();
        if (condCtx != null) {
            Condition cond = condVisitor.visit(condCtx);
            if (cond == null) return null;
            lookup.setCondition(cond);
        }

        return lookup;
    }

    private void apply(RangeDesc rangeDesc, RangeBodyContext ctx) {
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
