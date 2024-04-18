package com.regtab.core.writers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.regtab.core.model.Recordset;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
abstract class Writer {
    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    private final File outputFile;

    public abstract void write(Recordset recordset) throws IOException;
}
