package com.regtab.core.rtl.interpreter;

import com.regtab.core.model.ITable;
import com.regtab.core.rtl.interpreter.pattern.TablePattern;
import com.regtab.core.rtl.parser.RTLLexer;
import com.regtab.core.rtl.parser.RTLParser;
import lombok.extern.java.Log;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.regtab.core.rtl.interpreter.visitor.Interpreter;

@Log
public class Pattern {
    @Getter(AccessLevel.PACKAGE)
    private final TablePattern tableTemplate;

    private Pattern(TablePattern tableTemplate) {
        this.tableTemplate = tableTemplate;
    }

    @NonNull
    public static Pattern compile(String ttl) {
        if (ttl.isEmpty()) {
            throw new IllegalArgumentException("ttl is empty");
        }

        final RTLLexer lexer = new RTLLexer(CharStreams.fromString(ttl));
        final CommonTokenStream tokens = new CommonTokenStream(lexer);
        final RTLParser parser = new RTLParser(tokens);
        final ParseTree tree = parser.table();
        final TablePattern tableTemplate = Interpreter.compile(tree);

        return tableTemplate == null ? null : new Pattern(tableTemplate);
    }

    public Matcher matcher() {
        return new Matcher(this);
    }

    public static TableMap match(@NonNull String ttl, @NonNull ITable table) {
        final Pattern t = compile(ttl);
        final Matcher m = t.matcher();
        return m.match(table);
    }

    public static boolean apply(@NonNull String ttl, @NonNull ITable table) {
        final TableMap map = match(ttl, table);
        return map.apply();
    }

}