package com.regtab.core.rtl.interpreter.visitor;

import com.regtab.core.model.Action;
import com.regtab.core.model.Lookup;
import org.antlr.v4.runtime.tree.TerminalNode;
import com.regtab.core.rtl.parser.TTLBaseVisitor;
import com.regtab.core.rtl.parser.TTLParser.ActionBodyContext;
import com.regtab.core.rtl.parser.TTLParser.ActionContext;
import com.regtab.core.rtl.parser.TTLParser.ActionTypeContext;
import com.regtab.core.rtl.parser.TTLParser.LookupContext;

import java.util.List;

final class ActionVisitor extends TTLBaseVisitor<Action>  {
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
                String str = tn.getText();
                Action.Body body = new Action.Body(null, str);
                action.addBody(body);
            } else {
                LookupContext lookupCtx = actionBodyCtx.lookup();
                Lookup lookup = lookupVisitor.visit(lookupCtx);
                if (lookup == null)
                    return null;
                Action.Body body = new Action.Body(lookup, null);
                action.addBody(body);
            }
        }

        return action;
    }
}
