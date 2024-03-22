package com.regtab.core.rtl.interpreter.pattern;

import com.regtab.core.model.Action;
import lombok.Getter;
import lombok.NonNull;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

public abstract class ActionablePattern extends BasePattern {
    public ActionablePattern(ParseTree tree) {
        super(tree);
    }

    @Getter
    private final List<Action> actions = new ArrayList<>(1);

//    public void add(@NonNull Action action) {
//        actions.add(action);
//    }

    public abstract void add(@NonNull Action action);
}
