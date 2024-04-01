package com.regtab.core.rtl;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public final class Configurator {
    @Getter
    private static final String DEFAULT_CONCAT_SEPARATOR = "/";

    @Getter
    private static final String DEFAULT_AV_SEPARATOR = ":";

    @NonNull
    @Getter
    @Setter
    private String concatSeparator;

    @NonNull
    @Getter
    @Setter
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
