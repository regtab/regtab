package com.regtab.core.rtl.interpreter.visitor;

import com.regtab.core.rtl.interpreter.pattern.ElementPattern;
import com.regtab.core.rtl.interpreter.pattern.StructPattern;
import com.regtab.core.rtl.parser.TTLBaseVisitor;
import com.regtab.core.rtl.parser.TTLParser.*;

import java.util.ArrayList;
import java.util.List;

final class StructVisitor extends TTLBaseVisitor<StructPattern> {
    private static final ElementVisitor elementVisitor = new ElementVisitor();

    @Override
    public StructPattern visitStruct(StructContext ctx) {
        final StructPattern structTemplate = new StructPattern(ctx);

        final List<ElementContext> elementContexts = ctx.line().element();
        for (ElementContext elemCtx : elementContexts) {
            final ElementPattern elementTemplate = elementVisitor.visit(elemCtx);
            if (elementTemplate == null)
                return null; // TODO test

            structTemplate.add(elementTemplate);
        }

        final StartTextContext stCtx = ctx.line().startText();
        if (stCtx != null) {
            final String startText = stCtx.STRING().getText();
            structTemplate.setStartText(startText);
        }

        final EndTextContext etCtx = ctx.line().endText();
        if (etCtx != null) {
            final String endText = etCtx.STRING().getText();
            structTemplate.setEndText(endText);
        }

        final List<SeparatorContext> separatorContexts = ctx.line().separator();
        if (separatorContexts != null && !separatorContexts.isEmpty()) {
            List<String> separators = new ArrayList<>(separatorContexts.size());
            for (SeparatorContext separatorContext: separatorContexts) {
                String separator = separatorContext.STRING().getText();
                separators.add(separator);
            }
            structTemplate.setSeparators(separators);
        }

        return structTemplate;
    }

}
