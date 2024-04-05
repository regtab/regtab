package com.regtab.core.model;

import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Func<T> {
    private final String name;

    private final List<Object> args = new ArrayList<>(2);

    public void addArg(@NonNull Object arg) {
        args.add(arg);
    }

    @NonNull
    private final Evaluator<T> evaluator;

    private Func(String name, Func.Evaluator<T> evaluator) {
        this.name = name.toLowerCase();
        this.evaluator = evaluator;
    }

    public static Func<?> get(@NonNull String name) {
        final String cannonic = name.toLowerCase().replaceAll("_", "");
        return funcs.get(cannonic);
    }

    T eval(@NonNull ICell cell) {
        return evaluator.eval(cell, args.toArray());
    }

    private static final Evaluator<String> substr = (cell, args) -> {
        if (args.length < 2)
            throw new IllegalArgumentException("No required arguments");

        if (args[0] == null)
            throw new IllegalArgumentException("First argument is null");
        if (!(args[0] instanceof Integer))
            throw new IllegalArgumentException("First argument is not integer");
        final int start = (Integer) args[0];

        if (args[1] == null)
            throw new IllegalArgumentException("Second argument is null");
        if (!(args[1] instanceof Integer))
            throw new IllegalArgumentException("Second argument is not integer");
        final int end = (Integer) args[1];

        final String text = cell.getText();
        if (text == null)
            return null; // Impossible

        return text.substring(start, end);
    };

    private static final Evaluator<String> token = (cell, args) -> {
        if (args.length < 2)
            throw new IllegalArgumentException("No required arguments");

        if (args[0] == null)
            throw new IllegalArgumentException("First argument is null");
        if (!(args[0] instanceof String))
            throw new IllegalArgumentException("First argument is not string");
        final String separator = (String) args[0];

        if (args[1] == null)
            throw new IllegalArgumentException("Second argument is null");
        if (!(args[1] instanceof Integer))
            throw new IllegalArgumentException("Second argument is not integer");
        final int index = (Integer) args[1];

        final String text = cell.getText();
        if (text == null)
            return null; // Impossible
        final String[] tokens = text.split(separator);

        return tokens[index];
    };

    private static final Evaluator<String> replace = (cell, args) -> {
        if (args.length < 2)
            throw new IllegalArgumentException("No required arguments");

        if (args[0] == null)
            throw new IllegalArgumentException("First argument is null");
        if (!(args[0] instanceof String))
            throw new IllegalArgumentException("First argument is not string");
        final String regex = (String) args[0];

        if (args[1] == null)
            throw new IllegalArgumentException("First argument is null");
        if (!(args[1] instanceof String))
            throw new IllegalArgumentException("First argument is not string");
        final String replacement = (String) args[1];

        final String text = cell.getText();
        if (text == null)
            return null; // Impossible

        return text.replaceFirst(regex, replacement);
    };

    private static final Evaluator<String> replaceAll = (cell, args) -> {
        if (args.length < 2)
            throw new IllegalArgumentException("No required arguments");

        if (args[0] == null)
            throw new IllegalArgumentException("First argument is null");
        if (!(args[0] instanceof String))
            throw new IllegalArgumentException("First argument is not string");
        final String regex = (String) args[0];

        if (args[1] == null)
            throw new IllegalArgumentException("First argument is null");
        if (!(args[1] instanceof String))
            throw new IllegalArgumentException("First argument is not string");
        final String replacement = (String) args[1];

        final String text = cell.getText();
        if (text == null)
            return null; // Impossible

        return text.replaceAll(regex, replacement);
    };

    private static final Evaluator<String> upperCase = (cell, args) -> {
        final String text = cell.getText();
        if (text == null)
            return null; // Impossible

        return text.toUpperCase();
    };

    private static final Evaluator<String> lowerCase = (cell, args) -> {
        final String text = cell.getText();
        if (text == null)
            return null; // Impossible

        return text.toLowerCase();
    };

    private static final Func<String> SUBSTR = new Func<>("@substr", substr);
    private static final Func<String> TOKEN = new Func<>("@token", token);
    private static final Func<String> REPLACE = new Func<>("@replace", replace);
    private static final Func<String> REPLACE_ALL = new Func<>("@replaceAll", replaceAll);
    private static final Func<String> UPPER_CASE = new Func<>("@upperCase", upperCase);
    private static final Func<String> LOWER_CASE = new Func<>("@lowerCase", lowerCase);

    private static final HashMap<String, Func<?>> funcs = new HashMap<>();

    static {
        funcs.put(SUBSTR.name, SUBSTR);
        funcs.put(TOKEN.name, TOKEN);
        funcs.put(REPLACE.name, REPLACE);
        funcs.put(REPLACE_ALL.name, REPLACE_ALL);
        funcs.put(UPPER_CASE.name, UPPER_CASE);
        funcs.put(LOWER_CASE.name, LOWER_CASE);
    }

    private interface Evaluator<T> {
        T eval(ICell cell, Object... args);
    }
}
