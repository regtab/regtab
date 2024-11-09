package com.regtab.core.model;

import lombok.*;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.*;

/**
 * The Recordset class represents a set of records. It provides methods to manage the schema and data of the records.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class Recordset {
    @Getter
    private final List<Attribute> attributes = new ArrayList<>();

    @Getter
    private final List<Record> records = new ArrayList<>();

    /**
     * Returns the header of the recordset as an array of attribute names.
     *
     * @return An array of attribute names.
     */
    public String[] header() {
        final int size = attributes.size();
        final String[] header = new String[size];

        for (int i = 0; i < size; i++) {
            header[i] = attributes.get(i).getName();
        }

        return header;
    }

    /**
     * Returns the data of the recordset as a 2D array of string values.
     *
     * @return A 2D array of string values.
     */
    public String[][] data() {
        int size = records.size();
        final String[][] data = new String[size][];

        for (int i = 0; i < records.size(); i++) {
            Record record = records.get(i);
            size = record.values.size();
            data[i] = new String[size];
            for (int j = 0; j < size; j++) {
                data[i][j] = record.values.get(j).getString();
            }
        }

        return data;
    }

    private final Map<String, Attribute> attrMap = new HashMap<>();

    private final Map<Element, Value> elemValMap = new HashMap<>();

    void updateSchema(@NonNull Element valElement, @NonNull String attrName) {
        Attribute attribute = attrMap.get(attrName);
        final Value value = elemValMap.get(valElement);

        if (value != null) {
            if (attribute == null) {
                attribute = new Attribute(attrName, null);
                attrMap.put(attribute.getName(), attribute);
            }
            attribute.addValue(value);
        }
    }

    void updateSchema(@NonNull Element valElement, @NonNull Element attrElement) {
        if (valElement == attrElement)
            throw new IllegalArgumentException("Недопустимая операция: элементы совпадают");

        final String text = attrElement.getText();
        updateSchema(valElement, text);
    }

    void updateSchema(Value value, String attrName) {
        Attribute attribute = attrMap.get(attrName);

        if (attribute == null) {
            attribute = new Attribute(attrName, null);
            attrMap.put(attribute.getName(), attribute);
        }

        attribute.addValue(value);
    }

    private final List<Element> recordedElements = new ArrayList<>();

    Record createRecord(@NonNull Element element) {
        final Record record = new Record();
        final String text = element.getText();
        final Value value = new Value(text, element);
        record.getValues().add(value);
        elemValMap.put(element, value);
        recordedElements.add(element);
        records.add(record);

        //recordMap.put(element, record);
        recordMultiMap.put(element, record);

        return record;
    }

    void updateRecord(@NonNull Record record, @NonNull String attrName, @NonNull String valStr) {
        final Value value = new Value(valStr, null);
        record.getValues().add(value);

        updateSchema(value, attrName);
    }

    void updateRecord(@NonNull Record record, @NonNull String string) {
        final Value value = new Value(string, null);
        record.getValues().add(value);
    }

    void updateRecord(@NonNull Record record, @NonNull Element element) {
        final boolean result = recordedElements.contains(element);
        if (result)
            throw new IllegalArgumentException("Элемент уже принадлежит записи");

        Value value = elemValMap.get(element);
        if (value == null) {
            final String text = element.getText();
            value = new Value(text, element);
            elemValMap.put(element, value);
        }
        record.getValues().add(value);
    }

    private final MultiValuedMap<Element, Record> recordMultiMap = new ArrayListValuedHashMap<>();

    void joinRecords(@NonNull Element leftElem, @NonNull Element joinedElem) {
        // Найти все записи, связанные с элементом leftElem
        final Collection<Record> leftRecs = recordMultiMap.get(leftElem);
        if (leftRecs == null)
            return;

        // Найти все записи, связанные с элементом joinedElem
        final Collection<Record> joinedRecs = recordMultiMap.remove(joinedElem);
        if (joinedRecs == null)
            return;

        for (Record leftRecord : leftRecs)
            recordMultiMap.put(joinedElem, leftRecord);

        // Изъять все найденные записи, связанные с элементом joinedElem
        for (Record joinedRecord : joinedRecs)
            records.remove(joinedRecord);

        // Обновить все найденные записи, связанные с элементом leftElem
        for (Record leftRecord : leftRecs) {
            List<Recordset.Value> leftValues = leftRecord.getValues();
            for (Record joinedRecord : joinedRecs) {
                List<Recordset.Value> joinedValues = joinedRecord.getValues();
                for (Recordset.Value value : joinedValues) {
                    if (value.provenance == joinedElem)
                        continue;
                    leftValues.add(value);
                }
            }
        }

    }

    void complete() {
        if (records.size() == 0) return;

        Record record = records.get(0);
        final int numOfCols = record.getValues().size();

        for (int i = 0; i < records.size(); i++) {
            record = records.get(i);
            if (numOfCols != record.getValues().size()) {
                throw new IllegalStateException("Записи различаются количеством значений");
            }
        }

        for (int i = 0; i < numOfCols; i++) {
            Attribute previousAttr = null, currentAttr = null;
            for (Record item : records) {
                record = item;
                Value value = record.getValues().get(i);
                currentAttr = value.getAttribute();
                if (previousAttr == null || previousAttr == currentAttr) {
                    previousAttr = currentAttr;
                } else {
                    throw new IllegalStateException("Значения общей позиции двух записей принадлежат разным атрибутам");
                }
            }

            if (currentAttr != null) {
                for (int j = 0; j < records.size(); j++) {
                    record = records.get(j);
                    Value value = record.getValues().get(i);
                    Attribute attr = value.getAttribute();
                    if (attr == null)
                        currentAttr.addValue(value);
                }
            } else {
                String attrName = "ATTR" + i;
                currentAttr = new Attribute(attrName, null);
                attrMap.put(attrName, currentAttr);
                for (Record item : records) {
                    record = item;
                    Value value = record.getValues().get(i);
                    currentAttr.addValue(value);
                }
            }
        }

        final List<Value> values = records.get(0).values;
        for (Value value : values) {
            Attribute attribute = value.getAttribute();
            attributes.add(attribute);
        }
    }

    /**
     * Nested static class representing a record in the recordset.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Record {
        @Getter
        private final List<Value> values = new ArrayList<>();
    }

    /**
     * Nested static class representing a value in a record.
     */
    public static final class Value {
        @Getter
        @Setter(AccessLevel.PRIVATE)
        private Attribute attribute;

        @NonNull
        @Getter
        private final String string;

        @Getter
        private final Element provenance;

        private Value(@NonNull String string, Element provenance) {
            this.string = string;
            if (provenance != null && provenance.getType() != Element.Type.VALUE)
                throw new IllegalArgumentException("Invalid element type");
            this.provenance = provenance;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                    .append("attribute", attribute)
                    .append("string", string)
                    .toString();
        }

    }

    /**
     * Nested static class representing an attribute in the recordset schema.
     */
    public static final class Attribute {
        @Getter
        private final String name;

        @Getter
        private final Element provenance;

        private Attribute(@NonNull String name, Element provenance) {
            this.name = name;
            if (provenance != null && provenance.getType() != Element.Type.ATTRIBUTE)
                throw new IllegalArgumentException("Invalid element type");
            this.provenance = provenance;
        }

        @Getter
        private final List<Value> values = new ArrayList<>();

        private void addValue(@NonNull Recordset.Value value) {
            values.add(value);
            value.setAttribute(this);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                    .append("name", name)
                    .toString();
        }

    }

}
