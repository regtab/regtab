package com.regtab.core.writers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.regtab.core.model.Recordset;

import java.io.File;
import java.io.IOException;

/**
 * An abstract base class for writers that write `Recordset` objects to various output formats.
 */
@RequiredArgsConstructor
abstract class Writer {
    /**
     * The output file to which the data will be written.
     */
    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    private final File outputFile;

    /**
     * Writes the provided `Recordset` object to the output file.
     * This method must be implemented by any concrete subclass.
     *
     * @param recordset the `Recordset` object to be written to the output file
     * @throws IOException if an I/O error occurs during the write operation
     */
    public abstract void write(Recordset recordset) throws IOException;

    public abstract void write(Recordset recordset, boolean useHeader) throws IOException;

}
