package com.regtab.core.model.recordset;

import lombok.NonNull;

import com.regtab.core.model.semantics.Element;

import java.util.*;

public final class Recordset {
    private final Map<String, Attribute> attributes = new HashMap<>();
    private final List<Record> records = new ArrayList<>();

    public List<Record> records() {
        return new ArrayList<>(records);
    }

    private final Map<Element, Value> elemValMap = new HashMap<>();

    public void updateSchema(@NonNull Element valElement, @NonNull String attrName) {
        Attribute attr = attributes.get(attrName);
        Value val = elemValMap.get(valElement);

        if (val != null) {
            if (attr == null) {
                attr = new Attribute(attrName, null);
                attributes.put(attr.getName(), attr);
            }
            attr.addValue(val);
        }
    }

    public void updateSchema(@NonNull Element valElement, @NonNull Element attrElement) {
        if (valElement == attrElement)
            throw new IllegalArgumentException("Недопустимая операция: элементы совпадают");

        String text = attrElement.getText();
        updateSchema(valElement, text);
    }

    private void updateSchema(Value val, String attrName) {
        Attribute attr = attributes.get(attrName);

        if (attr == null) {
            attr = new Attribute(attrName, null);
            attributes.put(attr.getName(), attr);
        }

        attr.addValue(val);
    }

    private final List<Element> recordedElements = new ArrayList<>();

    public Record createRecord(@NonNull Element elem) {
        Record record = new Record();
        final String text = elem.getText();
        Value v = new Value(text, elem);
        record.addValue(v);
        elemValMap.put(elem, v);
        recordedElements.add(elem);
        records.add(record);

        return record;
    }

    public void updateRecord(@NonNull Record record, @NonNull String attrName, @NonNull String valStr) {
        Value v2 = new Value(valStr, null);
        record.addValue(v2);

        updateSchema(v2, attrName);
    }

    public void updateRecord(@NonNull Record record, @NonNull String str) {
        Value v2 = new Value(str, null);
        record.addValue(v2);
    }

    public void updateRecord(@NonNull Record record, @NonNull Element elem) {
        boolean result = recordedElements.contains(elem);
        if (result)
            throw new IllegalArgumentException("Элемент уже принадлежит записи");

        Value v2 = elemValMap.get(elem);
        if (v2 == null) {
            final String text = elem.getText();
            v2 = new Value(text, elem);
        }
        record.addValue(v2);
        elemValMap.put(elem, v2);
    }

    public void excludeNulls() {
        if (records.size() == 0) return;
        Record record = records.get(0);
        final int numOfCols = record.size();

        for (Record item : records) {
            record = item;
            if (numOfCols != record.size()) {
                throw new IllegalStateException("Записи различаются количеством значений");
            }
        }

        for (int i = 0; i < numOfCols; i++) {
            Attribute previousAttr = null, currentAttr = null;
            for (Record item : records) {
                record = item;
                Value value = record.getValue(i);
                currentAttr = value.getAttribute();
                if (previousAttr == null || previousAttr == currentAttr) {
                    previousAttr = currentAttr;
                } else {
                    throw new IllegalStateException("Значения общей позиции двух записей принадлежат разным атрибутам");
                }
            }

            if (currentAttr != null) {
                for (Record item : records) {
                    record = item;
                    Value value = record.getValue(i);
                    Attribute attr = value.getAttribute();
                    if (attr == null)
                        currentAttr.addValue(value);
                }
            } else {
                String attrName = "ATTR" + i;
                currentAttr = new Attribute(attrName, null);
                attributes.put(attrName, currentAttr);
                for (Record item : records) {
                    record = item;
                    Value value = record.getValue(i);
                    currentAttr.addValue(value);
                }
            }
        }
    }

}
