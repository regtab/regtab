package com.regtab.core.rtl.interpreter.visitor;

import com.regtab.core.rtl.interpreter.pattern.ElementPattern;
import com.regtab.core.rtl.interpreter.pattern.StructPattern;

import java.util.ArrayList;
import java.util.List;

import com.regtab.core.rtl.parser.RTLBaseVisitor;
import com.regtab.core.rtl.parser.RTLParser.*;

final class StructVisitor extends RTLBaseVisitor<StructPattern> {
    private static final ElementVisitor elementVisitor = new ElementVisitor();

    @Override
    public StructPattern visitStruct(StructContext ctx) {
        final StructPattern structPattern = new StructPattern(ctx);

        final List<ElementContext> elementContexts = ctx.line().element();
        for (ElementContext elemCtx : elementContexts) {
            final ElementPattern elementPattern = elementVisitor.visit(elemCtx);
            if (elementPattern == null)
                return null; // TODO test

            structPattern.add(elementPattern);
        }

        final StartTextContext stCtx = ctx.line().startText();
        if (stCtx != null) {
            final String startText = stCtx.STRING().getText();
            structPattern.setStartText(startText);
        }

        final EndTextContext etCtx = ctx.line().endText();
        if (etCtx != null) {
            final String endText = etCtx.STRING().getText();
            structPattern.setEndText(endText);
        }

        final List<SeparatorContext> separatorContexts = ctx.line().separator();
        if (separatorContexts != null && !separatorContexts.isEmpty()) {
            List<String> separators = new ArrayList<>(separatorContexts.size());
            for (SeparatorContext separatorContext: separatorContexts) {
                String separator = separatorContext.STRING().getText();
                separators.add(separator);
            }
            structPattern.setSeparators(separators);
        }

        return structPattern;
    }

}
