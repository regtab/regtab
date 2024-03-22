package com.regtab.core.printers;

import dnl.utils.text.table.TextTable;
import lombok.NonNull;
import com.regtab.core.model.Attribute;
import com.regtab.core.model.Record;
import com.regtab.core.model.Recordset;
import com.regtab.core.model.Value;

import java.util.List;

public final class Printer {
    public void print(@NonNull Recordset recordset) {
        List<Record> records = recordset.records();

        final String[] header;
        final String[][] data;
        final int numOfRows = records.size();
        Record record;

        if (records.size() > 0) {
            record = records.get(0);
            List<Value> values = record.values();
            final int numOfCols = values.size();
            header = new String[numOfCols];

            for (int i = 0; i < header.length; i++) {
                Attribute attr = values.get(i).getAttribute();
                String attrName;

                if (attr == null)
                    attrName = "UNDEFINED";
                else
                    attrName = attr.getName();

                header[i] = attrName;
            }

            data = new String[numOfRows][numOfCols];

            for (int i = 0; i < numOfRows; i++) {
                record = records.get(i);
                values = record.values();
                for (int j = 0; j < numOfCols; j++) {
                    data[i][j] = values.get(j).getData();
                }
            }

            TextTable tt = new TextTable(header, data);

            // this adds the numbering on the left
            // tt.setAddRowNumbering(true);
            // sort by the first column
            // tt.setSort(0);

            tt.printTable();
        }
    }
}
