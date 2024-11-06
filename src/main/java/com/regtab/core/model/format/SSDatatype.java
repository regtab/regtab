package com.regtab.core.model.format;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The SSDatatype class represents a data type in a spreadsheet. It provides static instances for numeric, string,
 * boolean, and formula data types.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class SSDatatype {
    private final boolean numeric;
    private final boolean str;
    private final boolean bool;
    private final boolean formula;

    /**
     * Represents a numeric data type.
     */
    public static final SSDatatype NUMERIC;

    /**
     * Represents a string data type.
     */
    public static final SSDatatype STRING;

    /**
     * Represents a boolean data type.
     */
    public static final SSDatatype BOOLEAN;

    /**
     * Represents a formula data type.
     */
    public static final SSDatatype FORMULA;

    static {
        NUMERIC = new SSDatatype(true, false, false, false);
        STRING = new SSDatatype(false, true, false, false);
        BOOLEAN = new SSDatatype(false, false, true, false);
        FORMULA = new SSDatatype(false, false, false, true);
    }
}
