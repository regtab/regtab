package com.regtab.core.rtl.interpreter.pattern;

import com.regtab.core.model.Action;
import com.regtab.core.model.Element;
import com.regtab.core.model.Expr;
import com.regtab.core.model.ICell;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import com.regtab.core.rtl.parser.RTLParser.*;

public final class ElementPattern extends ElementsPattern {
    public ElementPattern(ElementContext context) {
        super(context);
    }

    @NonNull
    @Getter
    @Setter
    private Element.Type elementType;

    @Getter
    @Setter
    private Expr expr;

    @Getter
    private final List<String> tags = new ArrayList<>();

    public void add(@NonNull String tag) {
        tags.add(tag);
    }

    public void add(@NonNull Action action) {
        getActions().add(action);
    }

    @Override
    public boolean apply(@NonNull ICell cell) {
        return apply(cell, cell.getText());
    }

    boolean apply(@NonNull ICell cell, @NonNull String val) {
        if (expr != null) {
            final Object result = expr.evalThis(cell);
            if (result == null)
                return false; // TODO log
            val = result.toString();
        }

        final Element element = cell.createElement(elementType, val);

        for (String tag : tags)
            element.addTag(tag);

        final List<Action> actions = getActions();
        for (Action action : actions)
            element.addAction(action);

        return true;
    }

}
