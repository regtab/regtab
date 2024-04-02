package com.regtab.core.rtl.interpreter.pattern;

import lombok.Getter;
import lombok.NonNull;

import com.regtab.core.model.semantics.Action;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

abstract class ActionablePattern extends BasePattern {
    ActionablePattern(ParseTree tree) {
        super(tree);
    }

    @Getter
    private final List<Action> actions = new ArrayList<>(1);

    public abstract void add(@NonNull Action action);

}
