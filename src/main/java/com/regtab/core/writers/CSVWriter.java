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

@RequiredArgsConstructor
public final class CSVWriter {
    private final String path;
    public void write(Recordset recordset) throws IOException {
        final CSVFormat format = CSVFormat.DEFAULT;
        final BufferedWriter writer = Files.newBufferedWriter(Paths.get(path), StandardCharsets.UTF_8);

        final CSVPrinter printer = new CSVPrinter(writer, format);

        final String[] header = recordset.header();
        printer.printRecord(header);

        final String[][] data = recordset.data();
        printer.printRecords(data);

        writer.flush();
        writer.close();
        printer.close();
    }
}
