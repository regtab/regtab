package com.regtab.core.rtl.interpreter.pattern;

import com.regtab.core.model.Condition;
import lombok.NonNull;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.tree.ParseTree;
import com.regtab.core.rtl.interpreter.visitor.Quantifier;

public abstract class RepeatablePattern extends ActionablePattern {
    public RepeatablePattern(ParseTree tree) {
        super(tree);
    }

    @Getter
    private Condition condition;

    void setCondition(@NonNull Condition condition) {
        if (this.condition == null)
            this.condition = condition;
        else
            this.condition.join(condition);
    }

    @Getter
    @Setter
    private Quantifier quantifier;

    @Getter
    @Setter
    private Integer repetitionCount;
}
