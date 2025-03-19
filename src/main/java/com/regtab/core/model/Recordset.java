package com.regtab.core.model;

import lombok.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.*;

/**
 * The Recordset class represents a set of records. It provides methods to manage the schema and data of the records.
 */
@Slf4j
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

    private final Map<Component, Value> elemValMap = new HashMap<>();

    void updateSchema(@NonNull Component valComponent, @NonNull String attrName) {
        Attribute attribute = attrMap.get(attrName);
        final Value value = elemValMap.get(valComponent);

        if (value != null) {
            if (attribute == null) {
                attribute = new Attribute(attrName, null);
                attrMap.put(attribute.getName(), attribute);
            }
            attribute.addValue(value);
        }
    }

    void updateSchema(@NonNull Component valComponent, @NonNull Component attrComponent) {
        if (valComponent == attrComponent)
            throw new IllegalArgumentException("Недопустимая операция: элементы совпадают");

        final String text = attrComponent.getText();
        updateSchema(valComponent, text);
    }

    void updateSchema(Value value, String attrName) {
        Attribute attribute = attrMap.get(attrName);

        if (attribute == null) {
            attribute = new Attribute(attrName, null);
            attrMap.put(attribute.getName(), attribute);
        }

        attribute.addValue(value);
    }

    private final List<Component> recordedComponents = new ArrayList<>();

    Record createRecord(@NonNull Component component) {
        final Record record = new Record();
        // final String text = component.getText();
        // final Value value = new Value(text, component);
        // record.getValues().add(value);
        // elemValMap.put(component, value);

        final List<String> textParts = component.copyTextParts();
        if (!textParts.isEmpty()) {
            for (String text : textParts) {
                final Value value = new Value(text, component);
                record.getValues().add(value);
                elemValMap.put(component, value);
            }
        } else {
            throw new RuntimeException("Try to create record by an empty component");
        }

        recordedComponents.add(component);
        records.add(record);

        recordMultiMap.put(component, record);

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

    void updateRecord(@NonNull Record record, @NonNull Component component) {
        final boolean result = recordedComponents.contains(component);
        if (result)
            throw new IllegalArgumentException("Элемент уже принадлежит записи");

        Value value = elemValMap.get(component);
        if (value == null) {
            final String text = component.getText();
            value = new Value(text, component);
            elemValMap.put(component, value);
        }
        record.getValues().add(value);
    }

    private final MultiValuedMap<Component, Record> recordMultiMap = new ArrayListValuedHashMap<>();

    void joinRecords(@NonNull Component leftElem, @NonNull Component joinedElem) {
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

        //Record record = records.get(0);

        int maxRecordSize = 0;
        for (Record record : records) {
            int recordSize = record.getValues().size();
            maxRecordSize = Math.max(maxRecordSize, recordSize);
        }

        //final int numOfCols = record.getValues().size();

        for (int i = 0; i < records.size(); i++) {
            Record record = records.get(i);
            int recordSize = record.getValues().size();
            if (recordSize < maxRecordSize) {
                //throw new IllegalStateException("Записи различаются количеством значений");
                log.debug("Записи различаются количеством значений: {} < {}", recordSize, maxRecordSize);
                for (int j = 0; j < maxRecordSize - recordSize; j++) {
                    Value emptyValue = new Value("", null);
                    record.getValues().add(emptyValue);
                }
            }
        }

        for (int i = 0; i < maxRecordSize; i++) {
            Attribute previousAttr = null, currentAttr = null;
            for (Record item : records) {
                Record record = item;
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
                    Record record = records.get(j);
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
                    Record record = item;
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
        private final Component provenance;

        private Value(@NonNull String string, Component provenance) {
            this.string = string;
            if (provenance != null && provenance.getType() != Component.Type.VALUE)
                throw new IllegalArgumentException("Invalid component type");
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
        private final Component provenance;

        private Attribute(@NonNull String name, Component provenance) {
            this.name = name;
            if (provenance != null && provenance.getType() != Component.Type.ATTRIBUTE)
                throw new IllegalArgumentException("Invalid component type");
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
