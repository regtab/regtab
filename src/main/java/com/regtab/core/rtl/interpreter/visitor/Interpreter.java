package com.regtab.core.rtl.interpreter.visitor;

import lombok.NonNull;

import com.regtab.core.rtl.interpreter.pattern.TablePattern;

import org.antlr.v4.runtime.tree.ParseTree;

public final class Interpreter {
    private static final TableVisitor tableVisitor = new TableVisitor();
    @NonNull
    public static TablePattern compile(@NonNull ParseTree tree) {
        return tableVisitor.visit(tree);
    }

    private Interpreter() {}
}
