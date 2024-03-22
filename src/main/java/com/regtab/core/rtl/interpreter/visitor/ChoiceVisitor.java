package com.regtab.core.rtl.interpreter.visitor;

import com.regtab.core.rtl.interpreter.pattern.ChoicePattern;
import com.regtab.core.rtl.interpreter.pattern.ElementPattern;
import com.regtab.core.rtl.interpreter.pattern.ElementsPattern;
import com.regtab.core.rtl.interpreter.pattern.StructPattern;

import com.regtab.core.rtl.parser.RTLBaseVisitor;
import com.regtab.core.rtl.parser.RTLParser.*;

final class ChoiceVisitor extends RTLBaseVisitor<ChoicePattern> {
    private static final ElementVisitor elementVisitor = new ElementVisitor();
    private static final StructVisitor structVisitor = new StructVisitor();
    private static final CondVisitor condVisitor = new CondVisitor();

    @Override
    public ChoicePattern visitChoice(ChoiceContext ctx) {
        final ChoicePattern choiceTemplate = new ChoicePattern(ctx);

        ChoiceBodyContext choiceBodyContext;
        choiceBodyContext = ctx.choiceBody(0);

        final ElementContext elementContext = choiceBodyContext.element();
        choiceTemplate.left = createVariant(choiceBodyContext);
        choiceBodyContext = ctx.choiceBody(1);
        choiceTemplate.right = createVariant(choiceBodyContext);

        final CondContext condContext = ctx.cond();
        if (condContext != null) {
            choiceTemplate.condition = condVisitor.visit(condContext);
        }

        return choiceTemplate;
    }

    private ElementsPattern createVariant(ChoiceBodyContext ctx) {
        final ElementContext elementContext = ctx.element();
        if (elementContext != null) {
            final ElementPattern elementTemplate = elementVisitor.visit(elementContext);
            if (elementTemplate == null)
                return null; // TODO test

            return elementTemplate;
        }

        final StructContext structuredContext = ctx.struct();
        if (structuredContext != null) {
            final StructPattern structTemplate = structVisitor.visit(structuredContext);
            if (structTemplate == null)
                return null; // TODO test

            return structTemplate;
        }

        return null; // Impossible
    }

}
