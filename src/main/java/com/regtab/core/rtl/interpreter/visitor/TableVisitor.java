package com.regtab.core.rtl.interpreter.visitor;

import com.regtab.core.rtl.interpreter.pattern.SubtablePattern;
import com.regtab.core.rtl.interpreter.pattern.TablePattern;
import com.regtab.core.rtl.parser.TTLBaseVisitor;
import com.regtab.core.rtl.parser.TTLParser.*;

import java.util.List;

final class TableVisitor extends TTLBaseVisitor<TablePattern> {
    private static final SubtableVisitor subtableVisitor = new SubtableVisitor();

    @Override
    public TablePattern visitTable(TableContext ctx) {
        final List<SubtableContext> subtableCtxList = ctx.subtable();

        if (subtableCtxList == null || subtableCtxList.isEmpty())
            return null; // Impossible

        final TablePattern tmpl = new TablePattern(ctx);
        for (SubtableContext subtableCtx : subtableCtxList) {
            final SubtablePattern subtableTemplate = subtableVisitor.visit(subtableCtx);
            if (subtableTemplate == null)
                return null;
            tmpl.add(subtableTemplate);
        }
        final List<SubtablePattern> subtableTemplates = tmpl.getSubtableTemplates();
        return subtableTemplates.isEmpty() ? null : tmpl;
    }
}