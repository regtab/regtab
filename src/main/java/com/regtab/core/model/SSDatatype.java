package com.regtab.core.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class SSDatatype {
    private final boolean numeric;
    private final boolean str;
    private final boolean bool;
    private final boolean formula;

    public static final SSDatatype NUMERIC;
    public static final SSDatatype STRING;
    public static final SSDatatype BOOLEAN;
    public static final SSDatatype FORMULA;

    static {
        NUMERIC = new SSDatatype(true, false, false, false);
        STRING = new SSDatatype(false, true, false, false);
        BOOLEAN = new SSDatatype(false, false, true, false);
        FORMULA = new SSDatatype(false, false, false, true);
    }
}
