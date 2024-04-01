package com.regtab.core.rtl.interpreter.visitor;

import com.regtab.core.rtl.interpreter.pattern.SubtablePattern;
import com.regtab.core.rtl.interpreter.pattern.TablePattern;
import com.regtab.core.rtl.parser.RTLBaseVisitor;
import com.regtab.core.rtl.parser.RTLParser.*;

import java.util.List;

final class TableVisitor extends RTLBaseVisitor<TablePattern> {
    private final static SubtableVisitor subtableVisitor = new SubtableVisitor();

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