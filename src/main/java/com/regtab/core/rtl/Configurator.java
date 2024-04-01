package com.regtab.core.rtl;

import lombok.Getter;
import lombok.Setter;

public final class Configurator {
    @Getter
    @Setter
    private String concatSeparator;// = DEFAULT_CONCAT_SEPARATOR;

    @Getter
    @Setter
    private String avSeparator;// = DEFAULT_AV_SEPARATOR;
}
