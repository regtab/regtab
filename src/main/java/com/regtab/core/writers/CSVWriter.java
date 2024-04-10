package com.regtab.core.writers;

import com.regtab.core.model.Recordset;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;

@RequiredArgsConstructor
public final class CSVWriter {
    private final String path;
    public void write(Recordset recordset) throws IOException {
        final CSVFormat format = CSVFormat.DEFAULT;
        final FileWriter writer = new FileWriter(path);
        final CSVPrinter printer = new CSVPrinter(writer, format);

        final String[] header = recordset.header();
        printer.printRecord(header);

        final String[][] data = recordset.data();
        printer.printRecords(data);

        //final Collection<Recordset.Attribute> attributes = recordset.getAttributes().values();
        //printer.printRecord(attributes);

//        for (Recordset.Record record : recordset.getRecords()) {
//            final List<Recordset.Value> values = record.getValues();
//            printer.printRecord(values);
//        }

        writer.flush();
        writer.close();
        printer.close();
    }
}
