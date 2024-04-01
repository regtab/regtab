package com.regtab.core.rtl.interpreter.visitor;

import com.regtab.core.rtl.interpreter.pattern.ChoicePattern;
import com.regtab.core.rtl.interpreter.pattern.ElementPattern;
import com.regtab.core.rtl.interpreter.pattern.ElementsPattern;
import com.regtab.core.rtl.interpreter.pattern.StructPattern;
import com.regtab.core.rtl.parser.RTLBaseVisitor;
import com.regtab.core.rtl.parser.RTLParser.*;

final class ChoiceVisitor extends RTLBaseVisitor<ChoicePattern> {
    private final ElementVisitor elementVisitor = new ElementVisitor();
    private final StructVisitor structVisitor = new StructVisitor();
    private final CondVisitor condVisitor = new CondVisitor();

    @Override
    public ChoicePattern visitChoice(ChoiceContext ctx) {
        final ChoicePattern choicePattern = new ChoicePattern(ctx);

        ChoiceBodyContext choiceBodyContext;
        choiceBodyContext = ctx.choiceBody(0);

        choicePattern.left = createVariant(choiceBodyContext);
        choiceBodyContext = ctx.choiceBody(1);
        choicePattern.right = createVariant(choiceBodyContext);

        final CondContext condContext = ctx.cond();
        if (condContext != null) {
            choicePattern.condition = condVisitor.visit(condContext);
        }

        return choicePattern;
    }

    private ElementsPattern createVariant(ChoiceBodyContext ctx) {
        final ElementContext elementContext = ctx.element();
        if (elementContext != null) {
            final ElementPattern elementPattern = elementVisitor.visit(elementContext);
            if (elementPattern == null)
                return null; // Impossible

            return elementPattern;
        }

        final StructContext structuredContext = ctx.struct();
        if (structuredContext != null) {
            final StructPattern structPattern = structVisitor.visit(structuredContext);
            if (structPattern == null)
                return null; // Impossible

            return structPattern;
        }

        return null; // Impossible
    }

}
