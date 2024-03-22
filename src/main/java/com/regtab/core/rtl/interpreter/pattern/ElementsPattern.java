package com.regtab.core.rtl.interpreter.pattern;

import com.regtab.core.model.ICell;
import org.antlr.v4.runtime.tree.ParseTree;

public abstract class ElementsPattern extends ActionablePattern {
    public ElementsPattern(ParseTree tree) {
        super(tree);
    }

    public abstract boolean apply(ICell cell);
}
