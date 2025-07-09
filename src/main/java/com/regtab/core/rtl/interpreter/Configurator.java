package com.regtab.core.rtl.interpreter;

import lombok.NonNull;
import lombok.Getter;
import lombok.Setter;

/**
 * The `Configurator` class is used to hold configuration settings for the interpreter.
 */
@Setter
public final class Configurator {
    @Getter
    private static final String DEFAULT_CONCAT_SEPARATOR = "|";

    @Getter
    private static final String DEFAULT_AV_SEPARATOR = ":";

    @NonNull
    @Getter
    private String concatSeparator;

    @NonNull
    @Getter
    private String avSeparator;

    public static final Configurator DEFAULT_CONFIGURATOR;

    static {
        DEFAULT_CONFIGURATOR = new Configurator(DEFAULT_CONCAT_SEPARATOR, DEFAULT_AV_SEPARATOR);
    }

    private Configurator(String concatSeparator, String avSeparator) {
        this.concatSeparator = concatSeparator;
        this.avSeparator = avSeparator;
    }
}
