package com.regtab.core.rtl.interpreter.pattern;

import com.regtab.core.model.semantics.Action;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

abstract class ActionablePattern extends BasePattern {
    public ActionablePattern(ParseTree tree) {
        super(tree);
    }

    @Getter
    private final List<Action> actions = new ArrayList<>(1);

    public abstract void add(@NonNull Action action);

}
