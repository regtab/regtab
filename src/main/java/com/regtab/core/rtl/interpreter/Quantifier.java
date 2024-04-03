package com.regtab.core.rtl.interpreter;

record Quantifier(Times times, Integer exactly) {
    enum Times {
        ZERO_OR_ONE, ZERO_OR_MORE, ONE_OR_MORE, EXACTLY, UNDEFINED
    }
}