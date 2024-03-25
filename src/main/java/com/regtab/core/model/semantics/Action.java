package com.regtab.core.model.semantics;

import lombok.NonNull;
import lombok.Getter;

import com.regtab.core.model.recordset.Record;
import com.regtab.core.model.recordset.Recordset;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public final class Action {
    @Getter
    private final Action.Type type;

    public Action(@NonNull Type type) {
        this.type = type;
        if (type == Type.RECORD || type == Type.GROUP) {
            lookups = new ArrayList<>(1);
            strings = new ArrayList<>(1);
        }
    }

    private Lookup lookup;
    private String string;

    private List<Lookup> lookups;
    private List<String> strings;

    public void addLookup(@NonNull Lookup lookup) {
        if (type == Type.RECORD || type == Type.GROUP)
            lookups.add(lookup);
        else
            this.lookup = lookup;
    }

    public void addString(@NonNull String string) {
        if (type == Type.RECORD || type == Type.GROUP)
            strings.add(string);
        else
            this.string = string;
    }

    private void performFactor(Element element) {
        if (lookup != null) {
            Element foundElem = lookup.findElement(element);
            if (foundElem != null) {
                String text = foundElem.getText();
                element.setText(text);
            }
            return;
        }
        if (string != null)
            element.setText(string);

    }

    private static final String SEPARATOR = "/"; //TODO настройки

    private void performContact(Element element) {
        if (lookup != null) {
            Element foundElem = lookup.findElement(element);
            if (foundElem != null) {
                String prefix = foundElem.getText();
                if (!prefix.isEmpty()) {
                    String text = element.getText();
                    text = prefix + SEPARATOR + text;
                    element.setText(text);
                }
            }
            return;
        }
        if (string != null)
            element.setText(string);
    }

    private void performRecord(Element element) {
        if (lookups.isEmpty() && strings.isEmpty()) return;

        final Recordset recordset = element.getCell().getTable().getRecordset();
        final Record record = recordset.createRecord(element);

        for (Lookup lookup : lookups) {
            final List<Element> elements = lookup.findElements(Element.Type.VALUE, element);
            if (elements != null) {
                for (Element elem : elements)
                    recordset.updateRecord(record, elem);
            }
        }

        for (String string : strings) {
            String attrName;
            String valStr;
            int sepPos = string.indexOf(':'); // TODO настройки

            if (sepPos > -1) {
                attrName = string.substring(0, sepPos);
                valStr = string.substring(sepPos + 1, string.length());

                if (attrName.isBlank() || valStr.isBlank())
                    throw new IllegalStateException(
                            "Недопустимое значение параметра STRING действия RECORD"
                    );

                recordset.updateRecord(record, attrName, valStr);
            } else {
                valStr = string;

                if (valStr.isBlank())
                    throw new IllegalStateException(
                            "Недопустимое значение параметра STRING действия RECORD"
                    );

                recordset.updateRecord(record, valStr);
            }
        }
    }

    private void performGroup(Element element) {
        if (lookups.isEmpty()) return;

        final Recordset recordset = element.getCell().getTable().getRecordset();
        for (Lookup lookup : lookups) {
            final List<Element> elements = lookup.findElements(Element.Type.VALUE, element);
            if (elements != null) {
                for (Element e : elements)
                    recordset.updateGroup(element, e);
            }
        }
    }

    private void performSchema(Element element) {
        if (lookup != null) {
            final Element e = lookup.findElement(Element.Type.ATTRIBUTE, element);
            if (e != null) {
                final Recordset recordset = element.getCell().getTable().getRecordset();
                recordset.updateSchema(element, e);
            }
            return;
        }
        if (string != null) {
            final Recordset recordset = element.getCell().getTable().getRecordset();
            recordset.updateSchema(element, string);
        }
    }

    public void perform(Element element) {
        //log.info("Perform action {} end element {}", this, element);
        switch (type) {
            case FACTOR -> performFactor(element);
            case CONCAT -> performContact(element);
            case RECORD -> performRecord(element);
            case GROUP -> performGroup(element);
            case SCHEMA -> performSchema(element);
        }
    }

    public enum Type {
        FACTOR, CONCAT, GROUP, SCHEMA, RECORD
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("type", type)
                //.append("lookup", lookup)
                //.append("str", str)
                .toString();
    }

}
