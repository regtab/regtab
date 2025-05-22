package com.regtab.core.model;

import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The Func class represents a function that can be applied to a cell.
 * It uses a lambda expression to evaluate the function.
 *
 * @param <T> The type of the result of the function.
 */
public final class Func<T> {
    private final String name;

    private final List<Object> args = new ArrayList<>(2);

    /**
     * Adds an argument to the function.
     *
     * @param arg The argument to add.
     */
    public void addArg(@NonNull Object arg) {
        args.add(arg);
    }

    @NonNull
    private final Evaluator<T> evaluator;

    private Func(String name, Func.Evaluator<T> evaluator) {
        this.name = name.toLowerCase();
        this.evaluator = evaluator;
    }

//    /**
//     * Retrieves a function by its name.
//     *
//     * @param name The name of the function.
//     * @return The function with the given name, or null if not found.
//     */
//    public static Func<?> get(@NonNull String name) {
//        final String cannonic = name.toLowerCase().replaceAll("_", "");
//        return funcs.get(cannonic);
//    }

    /**
     * Retrieves a function by its name.
     *
     * @param name The name of the function.
     * @return The function with the given name, or null if not found.
     */
    public static Func<?> create(@NonNull String name) {
        final String cannonic = name.toLowerCase().replaceAll("_", "");
        Evaluator<?> evaluator = evaluators.get(cannonic);
        if (evaluator != null) {
            return new Func(name, evaluator);
        }

        return null;
    }

    /**
     * Evaluates the function on the given cell.
     *
     * @param cell The cell to evaluate the function on.
     * @return The result of the function.
     */
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

    private static final String SUBSTR = "@substr";
    private static final String TOKEN = "@token";
    private static final String REPLACE = "@replace";
    private static final String REPLACE_ALL = "@replaceAll";
    private static final String UPPER_CASE = "@upperCase";
    private static final String LOWER_CASE = "@lowerCase";

    private static final HashMap<String, Evaluator<?>> evaluators = new HashMap<>();

    static {
        evaluators.put(SUBSTR.toLowerCase(), substr);
        evaluators.put(TOKEN.toLowerCase(), token);
        evaluators.put(REPLACE.toLowerCase(), replace);
        evaluators.put(REPLACE_ALL.toLowerCase(), replaceAll);
        evaluators.put(UPPER_CASE.toLowerCase(), upperCase);
        evaluators.put(LOWER_CASE.toLowerCase(), lowerCase);
    }

//    private static final Func<String> SUBSTR = new Func<>("@substr", substr);
//    private static final Func<String> TOKEN = new Func<>("@token", token);
//    private static final Func<String> REPLACE = new Func<>("@replace", replace);
//    private static final Func<String> REPLACE_ALL = new Func<>("@replaceAll", replaceAll);
//    private static final Func<String> UPPER_CASE = new Func<>("@upperCase", upperCase);
//    private static final Func<String> LOWER_CASE = new Func<>("@lowerCase", lowerCase);

//    private static final HashMap<String, Func<?>> funcs = new HashMap<>();
//
//    static {
//        funcs.put(SUBSTR.name, SUBSTR);
//        funcs.put(TOKEN.name, TOKEN);
//        funcs.put(REPLACE.name, REPLACE);
//        funcs.put(REPLACE_ALL.name, REPLACE_ALL);
//        funcs.put(UPPER_CASE.name, UPPER_CASE);
//        funcs.put(LOWER_CASE.name, LOWER_CASE);
//    }

    /**
     * The Evaluator interface defines a method for evaluating a function on a cell.
     *
     * @param <T> The type of the result of the function.
     */
    private interface Evaluator<T> {
        /**
         * Evaluates the function on the given cell with the given arguments.
         *
         * @param cell The cell to evaluate the function on.
         * @param args The arguments to the function.
         * @return The result of the function.
         */
        T eval(ICell cell, Object... args);
    }
}
