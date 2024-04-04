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
        Attribute attribute = attributes.get(attrName);
        final Value value = elemValMap.get(valElement);

        if (value != null) {
            if (attribute == null) {
                attribute = new Attribute(attrName, null);
                attributes.put(attribute.getName(), attribute);
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
        Attribute attribute = attributes.get(attrName);

        if (attribute == null) {
            attribute = new Attribute(attrName, null);
            attributes.put(attribute.getName(), attribute);
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

        return record;
    }

    void updateRecord(@NonNull Record record, @NonNull String attrName, @NonNull String valStr) {
        final Value value = new Value(valStr, null);
        record.getValues().add(value);

        updateSchema(value, attrName);
    }

    void updateRecord(@NonNull Record record, @NonNull String str) {
        final Value value = new Value(str, null);
        record.getValues().add(value);
    }

    void updateRecord(@NonNull Record record, @NonNull Element elem) {
        final boolean result = recordedElements.contains(elem);
        if (result)
            throw new IllegalArgumentException("Элемент уже принадлежит записи");

        Value value = elemValMap.get(elem);
        if (value == null) {
            final String text = elem.getText();
            value = new Value(text, elem);
            elemValMap.put(elem, value);
        }
        record.getValues().add(value);
    }

    void excludeNulls() {
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
