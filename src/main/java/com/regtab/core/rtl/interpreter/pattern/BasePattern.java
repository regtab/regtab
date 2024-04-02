package com.regtab.core.rtl.interpreter.pattern;

import lombok.Getter;

import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

abstract class BasePattern {
    @Getter
    private final ParseTree tree;

    BasePattern(ParseTree tree) {
        this.tree = tree;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("ttl", tree.getText())
                .toString();
    }
}
