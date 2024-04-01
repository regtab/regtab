package com.regtab.core.rtl.interpreter.visitor;

import com.regtab.core.model.semantics.Action;
import com.regtab.core.model.semantics.Lookup;
import com.regtab.core.rtl.Configurator;
import com.regtab.core.rtl.RTLPattern;
import com.regtab.core.rtl.parser.RTLBaseVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.regtab.core.rtl.parser.RTLParser.*;

import java.util.List;

final class ActionVisitor extends RTLBaseVisitor<Action> {
    private static final LookupVisitor lookupVisitor = new LookupVisitor();

    @Override
    public Action visitAction(ActionContext ctx) {
        final List<ActionBodyContext> actionBodyCtxList = ctx.actionBody();
        final ActionTypeContext actionTypeCtx = ctx.actionType();

        final Action.Type actType;

        if (actionTypeCtx.FACTOR() != null)
            actType = Action.Type.FACTOR;
        else if (actionTypeCtx.PREFIX() != null)
            actType = Action.Type.PREFIX;
        else if (actionTypeCtx.SUFFIX() != null)
            actType = Action.Type.SUFFIX;
        else if (actionTypeCtx.RECORD() != null)
            actType = Action.Type.RECORD;
        else if (actionTypeCtx.GROUP() != null)
            actType = Action.Type.GROUP;
        else if (actionTypeCtx.SCHEMA() != null)
            actType = Action.Type.SCHEMA;
        else
            return null; // Impossible

        final Action action = new Action(actType);

        final Configurator configurator = RTLPattern.getConfigurator();
        if (configurator != null) {
            final String concatSeparator = configurator.getConcatSeparator();
            if (concatSeparator != null)
                action.setConcatSeparator(concatSeparator);

            final String avSeparator = configurator.getAvSeparator();
            if (avSeparator != null)
                action.setAvSeparator(avSeparator);
        }

        if (actionBodyCtxList == null)
            return null; // Impossible

        for (ActionBodyContext actionBodyCtx : actionBodyCtxList) {
            final TerminalNode tn = actionBodyCtx.STRING();

            if (tn != null) {
                final String string = tn.getText();
                action.addString(string);
            } else {
                final LookupContext lookupCtx = actionBodyCtx.lookup();
                final Lookup lookup = lookupVisitor.visit(lookupCtx);
                if (lookup == null)
                    return null; // Impossible
                action.addLookup(lookup);
            }
        }

        return action;
    }
}
