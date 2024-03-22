package com.regtab.core.rtl.interpreter.visitor;

import com.regtab.core.model.semantics.Action;
import com.regtab.core.model.semantics.Element;
import com.regtab.core.model.semantics.Expr;
import com.regtab.core.rtl.parser.RTLBaseVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;
import com.regtab.core.rtl.interpreter.pattern.ElementPattern;

import java.util.List;

import com.regtab.core.rtl.parser.RTLParser.*;

final class ElementVisitor extends RTLBaseVisitor<ElementPattern> {
    private static final ExprVisitor exprVisitor = new ExprVisitor();
    private static final ActionVisitor actionVisitor = new ActionVisitor();

    @Override
    public ElementPattern visitElement(ElementContext ctx) {
        final ElementTypeContext etCtx = ctx.elementType();

        Element.Type elementType;
        if (etCtx.VALUE() != null)
            elementType = Element.Type.VALUE;
        else if (etCtx.ATTRIBUTE() != null)
            elementType = Element.Type.ATTRIBUTE;
        else if (etCtx.SKIPPED() != null)
            elementType = Element.Type.SKIPPED;
        else
            elementType = null; // Impossible

        final ElementPattern elementTemplate = new ElementPattern(ctx);
        elementTemplate.setElementType(elementType);

        final ExprContext exprContext = ctx.expr();
        if (exprContext != null) {
            final Expr expr = exprVisitor.visit(exprContext);
            if (expr == null)
                return null; // TODO test
            elementTemplate.setExpr(expr);
        }

        final TagsContext tagsCtx = ctx.tags();
        if (tagsCtx != null) {
            List<TerminalNode> tns = tagsCtx.TAG();
            for (TerminalNode tn : tns) {
                String tag = tn.getText();
                elementTemplate.add(tag);
            }
        }

        final ActionsContext actionsCtx = ctx.actions();
        if (actionsCtx != null) {
            List<ActionContext> actionCtxList = actionsCtx.action();
            if (actionCtxList != null) {
                for (ActionContext actionCtx : actionCtxList) {
                    Action action = actionVisitor.visit(actionCtx);
                    if (action == null)
                        return null; // TODO test
                    elementTemplate.add(action);
                }
            }
        }

        return elementTemplate;

    }
}
