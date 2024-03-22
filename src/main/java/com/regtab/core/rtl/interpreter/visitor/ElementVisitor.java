package com.regtab.core.rtl.interpreter.visitor;

import com.regtab.core.model.Action;
import com.regtab.core.model.Element;
import com.regtab.core.model.Expr;
import org.antlr.v4.runtime.tree.TerminalNode;
import com.regtab.core.rtl.interpreter.pattern.ElementPattern;
import com.regtab.core.rtl.parser.TTLBaseVisitor;
import com.regtab.core.rtl.parser.TTLParser.*;

import java.util.List;

final class ElementVisitor extends TTLBaseVisitor<ElementPattern> {
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
