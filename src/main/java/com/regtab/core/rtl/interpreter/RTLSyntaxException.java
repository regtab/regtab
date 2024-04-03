package com.regtab.core.rtl.interpreter;

import lombok.Getter;
import org.antlr.v4.runtime.tree.ParseTree;

public class RTLSyntaxException extends IllegalArgumentException {
    @Getter
    private final ParseTree tree;

    public RTLSyntaxException(String message, ParseTree tree) {
        super(message);
        this.tree = tree;
    }

    public String getMessage() {
        final String msg = super.getMessage();
        final String text = tree.getText();
        return String.format("\"%s\" could not be compiled: %s", text, msg);
    }
}
