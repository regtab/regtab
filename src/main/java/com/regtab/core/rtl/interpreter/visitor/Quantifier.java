package com.regtab.core.rtl.interpreter.visitor;

public record Quantifier(Times times, Integer exactly) {
    public enum Times {
        ZERO_OR_ONE, ZERO_OR_MORE, ONE_OR_MORE, EXACTLY, UNDEFINED
    }
}