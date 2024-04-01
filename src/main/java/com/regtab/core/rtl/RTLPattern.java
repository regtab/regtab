package com.regtab.core.rtl;

import com.regtab.core.model.ITable;
import com.regtab.core.rtl.interpreter.pattern.TablePattern;
import com.regtab.core.rtl.parser.RTLLexer;
import com.regtab.core.rtl.parser.RTLParser;
import lombok.Setter;
import lombok.extern.java.Log;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.regtab.core.rtl.interpreter.visitor.Interpreter;

@Log
public class RTLPattern {
    @Getter(AccessLevel.PACKAGE)
    private final TablePattern tablePattern;

    private RTLPattern(TablePattern tablePattern) {
        this.tablePattern = tablePattern;
    }

    @NonNull
    public static RTLPattern compile(String ttl) {
        if (ttl.isEmpty()) {
            throw new IllegalArgumentException("ttl is empty");
        }

        final RTLLexer lexer = new RTLLexer(CharStreams.fromString(ttl));
        lexer.removeErrorListeners();
        lexer.addErrorListener(RTLErrorListener.INSTANCE);
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final RTLParser parser = new RTLParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(RTLErrorListener.INSTANCE);
        final ParseTree tree = parser.table();

        final TablePattern tablePattern = Interpreter.compile(tree);

        return tablePattern == null ? null : new RTLPattern(tablePattern);
    }

    public RTLMatcher matcher() {
        return new RTLMatcher(this);
    }

    public static TableMap match(@NonNull String ttl, @NonNull ITable table) {
        final RTLPattern t = compile(ttl);
        final RTLMatcher m = t.matcher();
        return m.match(table);
    }

    public static boolean apply(@NonNull String ttl, @NonNull ITable table) {
        final TableMap map = match(ttl, table);
        return map.apply();
    }

    @Getter
    @Setter
    private static Configurator configurator;// = DEFAULT_CONCAT_SEPARATOR;

}