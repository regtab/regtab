package com.regtab.core.model.recordset;

import com.regtab.core.model.semantics.Element;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.util.*;

@Log
public final class Recordset {
    private final Map<String, Attribute> attributes = new HashMap<>();
    private final List<Record> records = new ArrayList<>();

    public List<Record> records() {
        return new ArrayList<>(records);
    }

    private final Map<Element, Value> elemValMap = new HashMap<>();

    private void addValue(Value value, Record record) {
        record.addValue(value);
        final Element element = value.getElement();
        elemValMap.put(element, value);
    }

    public void updateSchema(@NonNull Element valElement, @NonNull String attrName) {
        Attribute attr = attributes.get(attrName);
        Value val = elemValMap.get(valElement);

        if (val != null) {
            if (attr == null) {
                attr = new Attribute(attrName);
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
            attr = new Attribute(attrName);
            attributes.put(attr.getName(), attr);
        }

        attr.addValue(val);
    }

    private final List<Element> recordedElements = new ArrayList<>();

    public Record createRecord(@NonNull Element elem) {
        Record record = new Record();
        Value v = new Value(elem);
        addValue(v, record);
        recordedElements.add(elem);
        records.add(record);

        return record;
    }

    public void updateRecord(@NonNull Record record, @NonNull String attrName, @NonNull String valStr) {
        Value v2 = new Value(valStr);
        addValue(v2, record);

        updateSchema(v2, attrName);
    }

    public void updateRecord(@NonNull Record record, @NonNull String str) {
        Value v2 = new Value(str);
        addValue(v2, record);
    }

    public void updateRecord(@NonNull Record record, @NonNull Element elem) {
        boolean result = recordedElements.contains(elem);
        if (result)
            throw new IllegalArgumentException("Элемент уже принадлежит записи");

        Value v2 = elemValMap.get(elem);
        if (v2 == null)
            v2 = new Value(elem);
        addValue(v2, record);
    }

    private final HashMap<Element, Group> elemGroupMap = new HashMap<>();
    private final List<Group> groups = new ArrayList<>();

    public void updateGroup(@NonNull Element elem1, @NonNull Element elem2) {
        if (elem1.getType() == Element.Type.ATTRIBUTE)
            throw new IllegalArgumentException("Первый элемент является атрибутом");

        if (elem2.getType() == Element.Type.ATTRIBUTE)
            throw new IllegalArgumentException("Второй элемент является атрибутом");

        if (elem1 == elem2)
            throw new IllegalArgumentException("Элементы совпадают");

        Group group1 = elemGroupMap.get(elem1);
        Group group2 = elemGroupMap.get(elem2);

        if (group1 != null && group2 != null) {
            group1.join(group2);
            elemGroupMap.replace(elem2, group1);
            groups.remove(group2);
        } else if (group1 != null && group2 == null) {
            Value value2 = elemValMap.get(elem2);
            if (value2 != null) {
                group1.addValue(value2);
                elemGroupMap.put(elem2, group1);
            }
        } else if (group1 == null && group2 != null) {
            Value value1 = elemValMap.get(elem1);
            if (value1 != null) {
                group2.addValue(value1);
                elemGroupMap.put(elem1, group2);
            }
        } else {
            Value value1 = elemValMap.get(elem1);
            Value value2 = elemValMap.get(elem2);
            if (value1 != null && value2 != null) {
                Group group = new Group();
                groups.add(group);

                group.addValue(value1);
                elemGroupMap.put(elem1, group);
                group.addValue(value2);
                elemGroupMap.put(elem2, group);
            }
        }
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
                currentAttr = new Attribute(attrName);
                attributes.put(attrName, currentAttr);
                for (Record item : records) {
                    record = item;
                    Value value = record.getValue(i);
                    currentAttr.addValue(value);
                }
            }
        }
    }

    public void genAttributes() {
        for (int i = 0; i < groups.size(); i++) {
            Group group = groups.get(i);
            String name = "GROUP" + i;
            Attribute attribute = new Attribute(name, group);
            attributes.put(name, attribute);
        }
    }

}
