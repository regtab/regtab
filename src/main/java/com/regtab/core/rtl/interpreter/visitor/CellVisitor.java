package com.regtab.core.rtl.interpreter.visitor;

import com.regtab.core.model.semantics.Action;
import com.regtab.core.model.semantics.Condition;
import com.regtab.core.rtl.interpreter.pattern.CellPattern;
import com.regtab.core.rtl.interpreter.pattern.StructPattern;
import com.regtab.core.rtl.interpreter.pattern.ChoicePattern;
import com.regtab.core.rtl.interpreter.pattern.ElementPattern;
import com.regtab.core.rtl.parser.RTLBaseVisitor;
import lombok.extern.java.Log;

import java.util.HashMap;
import java.util.List;

import com.regtab.core.rtl.parser.RTLParser.*;

@Log
final class CellVisitor extends RTLBaseVisitor<CellPattern> {
    private static final ElementVisitor elementVisitor = new ElementVisitor();
    private static final StructVisitor structVisitor = new StructVisitor();
    private static final ChoiceVisitor choiceVisitor = new ChoiceVisitor();
    private static final CondVisitor condVisitor = new CondVisitor();
    private static final QuantifierVisitor quantifierVisitor = new QuantifierVisitor();
    private static final ActionVisitor actionVisitor = new ActionVisitor();

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
                final String message = String.format("No a such label [%s]", label);
                //throw new PatternException(message); //TODO подумать, как лучше сделать?
                log.warning(message);
                return null;
            }
        } else {
            elementsContext = ctx.elements();
        }

        final LabelContext labelContext = ctx.label();
        if (labelContext != null) {
            String label = labelContext.TAG().getText();
            store.put(label, elementsContext);
        }

        cellPattern.elementsContext = elementsContext;
        boolean result = apply(cellPattern, elementsContext);
        if (!result)
            return null; // TODO test

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

//        final ActionsContext actionsCtx = elementsContext.actions();
//        if (actionsCtx != null) {
//            List<ActionContext> actionCtxList = actionsCtx.action();
//            if (actionCtxList != null) {
//                for (ActionContext actionCtx : actionCtxList) {
//                    Action action = actionVisitor.visit(actionCtx);
//                    if (action == null)
//                        return null; // TODO test
//                    cellPattern.add(action);
//                }
//            }
//        }

        return cellPattern;
    }

    private boolean apply(CellPattern tmpl, ElementsContext ctx) {
        final ElementContext elementContext = ctx.element();
        if (elementContext != null) {
            final ElementPattern elementPattern = elementVisitor.visit(elementContext);
            if (elementPattern == null)
                return false; // TODO log

            tmpl.setElementsPattern(elementPattern);
            return true;
        }

        final StructContext structuredContext = ctx.struct();
        if (structuredContext != null) {
            final StructPattern structPattern = structVisitor.visit(structuredContext);
            if (structPattern == null)
                return false; // TODO log

            tmpl.setElementsPattern(structPattern);
            return true;
        }

        final ChoiceContext choiceContext = ctx.choice();
        if (choiceContext != null) {
            final ChoicePattern choicePattern = choiceVisitor.visit(choiceContext);
            if (choicePattern == null)
                return false; // TODO log

            tmpl.setElementsPattern(choicePattern);
        }

        return true;
    }

}
