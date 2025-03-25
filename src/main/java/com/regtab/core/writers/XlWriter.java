package com.regtab.core.writers;

import com.regtab.core.model.Recordset;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * The `XlWriter` class is responsible for writing a `Recordset` object to an Excel file.
 */
public final class XlWriter extends Writer {
    /**
     * Creates an Excel workbook from the provided `Recordset` object.
     *
     * @param recordset the `Recordset` object to be written to the Excel workbook
     * @return the created Excel workbook
     */
    private Workbook createWorkbook(Recordset recordset, boolean useHeader) {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet();

        int rowShift = 0;

        // Write header if applicable
        if (useHeader) {
            String[] header = recordset.header();

            if (header.length > 0) {
                Row excelRow = sheet.createRow(0);

                for (int i = 0; i < header.length; i++) {
                    excelRow.createCell(i).setCellValue(header[i]);
                }

                rowShift = 1;
            }
        }

        // Write records
        String[][] data = recordset.data();

        if (data.length > 0) {
            for (int i = 0; i < data.length; i++) {
                String[] record = data[i];
                Row excelRow = sheet.createRow(i + rowShift);
                for (int j = 0; j < record.length; j++) {
                    excelRow.createCell(j).setCellValue(record[j]);
                }
            }

            // Set auto sizes for the workbook columns
            final int numOfColumns = data[0].length;

            for (int i = 0; i < numOfColumns; i++) {
                sheet.autoSizeColumn(i);
            }
        }

        return workbook;
    }

    /**
     * Writes the provided `Recordset` object to an Excel file.
     *
     * @param recordset the `Recordset` object to be written to the Excel file
     * @throws IOException if an I/O error occurs during the write operation
     */
    @Override
    public void write(Recordset recordset) throws IOException {
//        Workbook workbook = createWorkbook(recordset, false);
//        FileOutputStream out = new FileOutputStream(getOutputFile());
//        workbook.write(out);
//        out.close();
        write(recordset, true);
    }

    public void write(Recordset recordset, boolean useHeader) throws IOException {
        Workbook workbook = createWorkbook(recordset, useHeader);
        FileOutputStream out = new FileOutputStream(getOutputFile());
        workbook.write(out);
        out.close();
    }

    /**
     * Constructs an `XlWriter` object with the specified output file path.
     *
     * @param outputFile the path to the output Excel file
     */
    public XlWriter(String outputFile) {
        super(new File(outputFile));
    }
}
