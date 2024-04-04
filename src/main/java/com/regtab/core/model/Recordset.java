package com.regtab.core.model;

import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class Recordset {
    @Getter
    private final Map<String, Attribute> attributes = new HashMap<>();

    @Getter
    private final List<Record> records = new ArrayList<>();

    private final Map<Element, Value> elemValMap = new HashMap<>();

    void updateSchema(@NonNull Element valElement, @NonNull String attrName) {
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

    void updateSchema(@NonNull Element valElement, @NonNull Element attrElement) {
        if (valElement == attrElement)
            throw new IllegalArgumentException("Недопустимая операция: элементы совпадают");

        String text = attrElement.getText();
        updateSchema(valElement, text);
    }

    void updateSchema(Value val, String attrName) {
        Attribute attr = attributes.get(attrName);

        if (attr == null) {
            attr = new Attribute(attrName, null);
            attributes.put(attr.getName(), attr);
        }

        attr.addValue(val);
    }

    private final List<Element> recordedElements = new ArrayList<>();

    Record createRecord(@NonNull Element elem) {
        Record record = new Record();
        final String text = elem.getText();
        Value v = new Value(text, elem);
        record.getValues().add(v);
        elemValMap.put(elem, v);
        recordedElements.add(elem);
        records.add(record);

        return record;
    }

    void updateRecord(@NonNull Record record, @NonNull String attrName, @NonNull String valStr) {
        Value v2 = new Value(valStr, null);
        record.getValues().add(v2);

        updateSchema(v2, attrName);
    }

    void updateRecord(@NonNull Record record, @NonNull String str) {
        Value v2 = new Value(str, null);
        record.getValues().add(v2);
    }

    void updateRecord(@NonNull Record record, @NonNull Element elem) {
        boolean result = recordedElements.contains(elem);
        if (result)
            throw new IllegalArgumentException("Элемент уже принадлежит записи");

        Value v2 = elemValMap.get(elem);
        if (v2 == null) {
            final String text = elem.getText();
            v2 = new Value(text, elem);
        }
        record.getValues().add(v2);
        elemValMap.put(elem, v2);
    }

    void excludeNulls() {
        if (records.size() == 0) return;
        Record record = records.get(0);
        final int numOfCols = record.getValues().size();

        for (Record item : records) {
            record = item;
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
                for (Record item : records) {
                    record = item;
                    Value value = record.getValues().get(i);
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
                    Value value = record.getValues().get(i);
                    currentAttr.addValue(value);
                }
            }
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Record {
        @Getter
        private final List<Value> values = new ArrayList<>();
    }

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
