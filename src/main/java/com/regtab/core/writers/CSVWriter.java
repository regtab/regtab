package com.regtab.core.writers;

import lombok.RequiredArgsConstructor;

import com.regtab.core.model.Recordset;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The `CSVWriter` class is responsible for writing a `Recordset` object to a CSV file.
 */
@RequiredArgsConstructor
public final class CSVWriter {
    private final String path;

    /**
     * Writes the provided `Recordset` object to a CSV file.
     *
     * @param recordset the `Recordset` object to be written to the CSV file
     * @throws IOException if an I/O error occurs while writing to the file
     */
    public void write(Recordset recordset) throws IOException {
//        final CSVFormat format = CSVFormat.POSTGRESQL_CSV;
//
//        final BufferedWriter writer = Files.newBufferedWriter(Paths.get(path), StandardCharsets.UTF_8);
//
//        final CSVPrinter printer = new CSVPrinter(writer, format);
//
//        final String[] header = recordset.header();
//        printer.printRecord(header);
//
//        final String[][] data = recordset.data();
//        printer.printRecords(data);
//
//        writer.flush();
//        writer.close();
//        printer.close();

        write(recordset, true);
    }

    public void write(Recordset recordset, boolean useHeader) throws IOException {
        final CSVFormat format = CSVFormat.POSTGRESQL_CSV;

        final BufferedWriter writer = Files.newBufferedWriter(Paths.get(path), StandardCharsets.UTF_8);

        final CSVPrinter printer = new CSVPrinter(writer, format);

        if (useHeader) {
            final String[] header = recordset.header();
            printer.printRecord(header);
        }

        final String[][] data = recordset.data();
        printer.printRecords(data);

        writer.flush();
        writer.close();
        printer.close();
    }
}
