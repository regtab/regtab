package com.regtab.core.rtl.interpreter.visitor;

import com.regtab.core.model.semantics.Action;
import com.regtab.core.model.semantics.Lookup;
import com.regtab.core.rtl.parser.RTLBaseVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.regtab.core.rtl.parser.RTLParser.*;

import java.util.List;

final class ActionVisitor extends RTLBaseVisitor<Action> {
    private static final LookupVisitor lookupVisitor = new LookupVisitor();

    @Override
    public Action visitAction(ActionContext ctx) {
        List<ActionBodyContext> actionBodyCtxList = ctx.actionBody();
        ActionTypeContext actionTypeCtx = ctx.actionType();

        Action.Type actType;

        if (actionTypeCtx.FACTOR() != null)
            actType = Action.Type.FACTOR;
        else if (actionTypeCtx.CONCAT() != null)
            actType = Action.Type.CONCAT;
        else if (actionTypeCtx.RECORD() != null)
            actType = Action.Type.RECORD;
        else if (actionTypeCtx.GROUP() != null)
            actType = Action.Type.GROUP;
        else if (actionTypeCtx.SCHEMA() != null)
            actType = Action.Type.SCHEMA;
        else
            throw new IllegalStateException("Тип действия неопределен");

        Action action = new Action(actType);

        if (actionBodyCtxList == null)
            return null; // Impossible

        for (ActionBodyContext actionBodyCtx : actionBodyCtxList) {
            TerminalNode tn = actionBodyCtx.STRING();

            if (tn != null) {
                final String string = tn.getText();
                action.addString(string);
            } else {
                LookupContext lookupCtx = actionBodyCtx.lookup();
                Lookup lookup = lookupVisitor.visit(lookupCtx);
                if (lookup == null)
                    return null;
                action.addLookup(lookup);
            }
        }

        return action;
    }
}
