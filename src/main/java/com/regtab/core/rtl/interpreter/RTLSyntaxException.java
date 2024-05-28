package com.regtab.core.rtl.interpreter;

import lombok.Getter;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * A custom exception that indicates a syntax error in the RegTab Language (RTL) code.
 * This exception extends {@link IllegalArgumentException} and includes a {@link ParseTree}
 * field to hold the ANTLR parse tree that caused the exception.
 */
public class RTLSyntaxException extends IllegalArgumentException {
    @Getter
    private final ParseTree tree;

    /**
     * Constructs a new RTLSyntaxException with the specified detail message and parse tree.
     *
     * @param message The detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param tree    The ANTLR parse tree that caused the exception.
     */
    public RTLSyntaxException(String message, ParseTree tree) {
        super(message);
        this.tree = tree;
    }

    /**
     * Returns a detailed error message that includes the text of the parse tree and the original error message.
     *
     * @return The detailed error message.
     */
    public String getMessage() {
        final String msg = super.getMessage();
        final String text = tree.getText();
        return String.format("\"%s\" could not be compiled: %s", text, msg);
    }
}
