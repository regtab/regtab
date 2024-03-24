package com.regtab.core.rtl.interpreter.visitor;

import com.regtab.core.rtl.interpreter.pattern.SubtablePattern;
import com.regtab.core.rtl.interpreter.pattern.TablePattern;

import java.util.List;

import com.regtab.core.rtl.parser.RTLBaseVisitor;
import com.regtab.core.rtl.parser.RTLParser.*;

final class TableVisitor extends RTLBaseVisitor<TablePattern> {
    private static final SubtableVisitor subtableVisitor = new SubtableVisitor();

    @Override
    public TablePattern visitTable(TableContext ctx) {
        final List<SubtableContext> subtableCtxList = ctx.subtable();

        if (subtableCtxList == null || subtableCtxList.isEmpty())
            return null; // Impossible

        final TablePattern tmpl = new TablePattern(ctx);
        for (SubtableContext subtableCtx : subtableCtxList) {
            final SubtablePattern subtablePattern = subtableVisitor.visit(subtableCtx);
            if (subtablePattern == null)
                return null;
            tmpl.add(subtablePattern);
        }
        final List<SubtablePattern> subtablePatterns = tmpl.getSubtablePatterns();
        return subtablePatterns.isEmpty() ? null : tmpl;
    }
}