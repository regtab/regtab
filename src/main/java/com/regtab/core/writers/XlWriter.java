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
    private Workbook createWorkbook(Recordset recordset) {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet();

        // Write header
        String[] header = recordset.header();
        Row excelRow = sheet.createRow(0);

        for (int i = 0; i < header.length; i++) {
            excelRow.createCell(i).setCellValue(header[i]);
        }

        // Write records
        String[][] data = recordset.data();

        for (int i = 0; i < data.length; i++) {
            String[] record = data[i];
            excelRow = sheet.createRow(i + 1);
            for (int j = 0; j < record.length; j++) {
                excelRow.createCell(j).setCellValue(record[j]);
            }
        }

        int i = 1;

        for (i = 0; i < header.length; i++) {
            sheet.autoSizeColumn(i);
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
        Workbook workbook = createWorkbook(recordset);
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
