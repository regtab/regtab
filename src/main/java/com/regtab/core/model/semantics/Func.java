package com.regtab.core.model.semantics;

import com.regtab.core.model.ICell;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Func {
    private final String name;

    private final List<Object> args = new ArrayList<>();

    public void addArg(@NonNull Object arg) {
        args.add(arg);
    }

    @NonNull
    private final Evaluator evaluator;

    public static Func get(String name){
        return funcs.get(name);
    }

    Object eval(@NonNull ICell cell) {
        return evaluator.eval(cell, args.toArray());
    }

    private static final Evaluator substr = (cell, args) -> {
        int from = (Integer) args[0];
        int to = (Integer) args[1];
        String text = cell.getText();
        return text.substring(from, to);
    };

    private static final Evaluator token = (cell, args) -> {
        String separator = (String) args[0];
        int index = (Integer) args[1];
        String text = cell.getText();
        String[] tokens = text.split(separator);
        return tokens[index];
    };

    private static final Evaluator replace = (cell, args) -> {
        String regex = (String) args[0];
        String replacement = (String) args[1];
        String text = cell.getText();
        return text.replaceFirst(regex, replacement);
    };

    private static final Evaluator replaceAll = (cell, args) -> {
        String regex = (String) args[0];
        String replacement = (String) args[1];
        String text = cell.getText();
        return text.replaceAll(regex, replacement);
    };

    private static final Evaluator upperCase = (cell, args) -> {
        String text = cell.getText();
        return text.toUpperCase();
    };

    private static final Evaluator lowerCase = (cell, args) -> {
        String text = cell.getText();
        return text.toLowerCase();
    };

    private static final Func SUBSTR = new Func("substr", substr);
    private static final Func TOKEN = new Func("token", token);
    private static final Func REPLACE = new Func("replace", replace);
    private static final Func REPLACE_ALL = new Func("replaceAll", replaceAll);
    private static final Func UPPER_CASE = new Func("upperCase", upperCase);
    private static final Func LOWER_CASE = new Func("lowerCase", lowerCase);

    private static final HashMap<String, Func> funcs = new HashMap<>();

    static {
        funcs.put(SUBSTR.name, SUBSTR);
        funcs.put(TOKEN.name, TOKEN);
        funcs.put(REPLACE.name, REPLACE);
        funcs.put(REPLACE_ALL.name, REPLACE_ALL);
        funcs.put(UPPER_CASE.name, UPPER_CASE);
        funcs.put(LOWER_CASE.name, LOWER_CASE);
    }

    private interface Evaluator {
        Object eval(ICell cell, Object... args);
    }
}
