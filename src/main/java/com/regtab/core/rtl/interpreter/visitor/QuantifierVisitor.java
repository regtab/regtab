package com.regtab.core.rtl.interpreter.visitor;

import com.regtab.core.rtl.parser.RTLBaseVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;
import com.regtab.core.rtl.parser.RTLParser.*;

final class QuantifierVisitor extends RTLBaseVisitor<Quantifier> {
    public Quantifier visit(QuantifierContext ctx) {
        return visitQuantifier(ctx);
    }

    @Override
    public Quantifier visitQuantifier(QuantifierContext ctx) {
        Quantifier.Times times = null;
        Integer exactly = null;

        if (ctx != null) {
            ExactlyContext exactlyCtx = ctx.exactly();
            if (exactlyCtx != null) {
                TerminalNode tn = exactlyCtx.INT();
                times = Quantifier.Times.EXACTLY;
                String str = tn.getText();
                exactly = Integer.parseInt(str);
                if (exactly < 0) {
                    return null; // Impossible
                }
            } else {
                if (ctx.oneOrMore() != null)
                    times = Quantifier.Times.ONE_OR_MORE;
                if (ctx.zeroOrMore() != null)
                    times = Quantifier.Times.ZERO_OR_MORE;
                if (ctx.zeroOrOne() != null)
                    times = Quantifier.Times.ZERO_OR_ONE;
            }
        } else {
            times = Quantifier.Times.UNDEFINED;
        }

        return new Quantifier(times, exactly);
    }
}
