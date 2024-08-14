package com.regtab.core.readers;

import lombok.NonNull;

import com.regtab.core.model.ITable;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * The CSVReader class is responsible for reading CSV files and converting them into an ITable object.
 */
@RequiredArgsConstructor
public class CSVReader {
    @NonNull
    private final String path;

    /**
     * Reads the CSV file specified by the path and converts it into an ITable object.
     *
     * @return The ITable object representing the CSV file.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public ITable read() throws IOException {
        final Reader reader = new FileReader(path);

        final CSVFormat csvFormat = CSVFormat.DEFAULT
                .builder()
                .setSkipHeaderRecord(true)
                .build();

        final List<CSVRecord> records = csvFormat.parse(reader).getRecords();

        int numOfRows = 0;
        int numOfCols = 0;

        for (CSVRecord record : records) {
            final String[] values = record.values();
            if (numOfCols < values.length)
                numOfCols = values.length;

            numOfRows++;
        }

        final ITable table = new ITable(numOfRows, numOfCols);

        int rowIndex = 0;
        for (CSVRecord record : records) {
            int colIndex = 0;
            final String[] values = record.values();
            for (String value :  values) {
                table.createCell(rowIndex, colIndex, null, value);
                colIndex++;
            }

            rowIndex++;
        }

        return table;
    }
}

