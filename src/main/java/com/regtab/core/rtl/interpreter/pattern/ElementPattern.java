package com.regtab.core.rtl.interpreter.pattern;

import com.regtab.core.model.ILine;
import com.regtab.core.model.semantics.Action;
import com.regtab.core.model.semantics.Element;
import com.regtab.core.model.semantics.Expr;
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
        final ILine first = cell.getLines().getFirst();
        final String val = first.getText();
        //return apply(cell, cell.getText());
        return apply(first, val);
    }

    boolean apply(@NonNull ICell cell, @NonNull String val) {
        final ILine first = cell.getLines().getFirst();
        return apply(first, val);
//        if (expr != null) {
//            final Object result = expr.evalThis(cell);
//            if (result == null)
//                return false; // TODO log
//            val = result.toString();
//        }
//
//        final Element element = cell.createElement(elementType, val);
//
//        for (String tag : tags)
//            element.addTag(tag);
//
//        final List<Action> actions = getActions();
//        for (Action action : actions)
//            element.addAction(action);
//
//        return true;
    }

    boolean apply(@NonNull ILine line, @NonNull String val) {
        final ICell cell = line.getCell();
        if (expr != null) {
            final Object result = expr.evalThis(cell); // TODO м.б. нужна оценка выражения в line?
            if (result == null)
                return false; // TODO log
            val = result.toString();
        }

        final Element element = line.createElement(elementType, val);

        for (String tag : tags)
            element.addTag(tag);

        final List<Action> actions = getActions();
        for (Action action : actions)
            element.addAction(action);

        return true;
    }

}
