package com.regtab.core.rtl.interpreter.visitor;

import com.regtab.core.model.semantics.Action;
import com.regtab.core.model.semantics.Element;
import com.regtab.core.model.semantics.Expr;
import com.regtab.core.rtl.parser.RTLBaseVisitor;
import com.regtab.core.rtl.interpreter.pattern.ElementPattern;

import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

import com.regtab.core.rtl.parser.RTLParser.*;

final class ElementVisitor extends RTLBaseVisitor<ElementPattern> {
    private final ExprVisitor exprVisitor = new ExprVisitor();
    private final ActionVisitor actionVisitor = new ActionVisitor();

    @Override
    public ElementPattern visitElement(ElementContext ctx) {
        final ElementTypeContext etCtx = ctx.elementType();

        final Element.Type elementType;
        if (etCtx.VALUE() != null)
            elementType = Element.Type.VALUE;
        else if (etCtx.ATTRIBUTE() != null)
            elementType = Element.Type.ATTRIBUTE;
        else if (etCtx.SKIPPED() != null)
            elementType = Element.Type.SKIPPED;
        else
            elementType = null; // Impossible

        final ElementPattern elementPattern = new ElementPattern(ctx);
        elementPattern.setElementType(elementType);

        final ExprContext exprContext = ctx.expr();
        if (exprContext != null) {
            final Expr expr = exprVisitor.visit(exprContext);
            if (expr == null)
                return null; // Impossible
            elementPattern.setExpr(expr);
        }

        final TagsContext tagsCtx = ctx.tags();
        if (tagsCtx != null) {
            final List<TerminalNode> tns = tagsCtx.TAG();
            for (TerminalNode tn : tns) {
                String tag = tn.getText();
                elementPattern.add(tag);
            }
        }

        final ActionsContext actionsCtx = ctx.actions();
        if (actionsCtx != null) {
            final List<ActionContext> actionCtxList = actionsCtx.action();
            if (actionCtxList != null) {
                for (ActionContext actionCtx : actionCtxList) {
                    final Action action = actionVisitor.visit(actionCtx);
                    if (action == null)
                        return null; // Impossible
                    elementPattern.add(action);
                }
            }
        }

        return elementPattern;

    }
}
