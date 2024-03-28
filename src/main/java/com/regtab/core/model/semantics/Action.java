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

    private void performFactor(Element caller) {
        if (lookup != null) {
            final Element found = lookup.findFirst(caller);
            if (found != null) {
                String text = found.getText();
                caller.setText(text);
            }
            return;
        }
        if (string != null)
            caller.setText(string);
    }

    private static final String SEPARATOR = "/"; //TODO настройки

    private void performPrefix(Element caller) {
        if (lookup != null) {
            final Element found = lookup.findFirst(caller);
            if (found != null) {
                final String prefix = found.getText();
                if (!prefix.isEmpty()) {
                    String text = caller.getText();
                    text = prefix + SEPARATOR + text;
                    caller.setText(text);
                }
            }
            return;
        }
        if (string != null)
            caller.setText(string);
    }

    private void performSuffix(Element caller) {
        if (lookup != null) {
            final Element found = lookup.findFirst(caller);
            if (found != null) {
                final String suffix = found.getText();
                if (!suffix.isEmpty()) {
                    String text = caller.getText();
                    text = text + SEPARATOR + suffix;
                    caller.setText(text);
                }
            }
            return;
        }
        if (string != null)
            caller.setText(string);
    }

    private void performRecord(Element caller) {
        if (lookups.isEmpty() && strings.isEmpty()) return;

        final Recordset recordset = caller.getCell().getTable().getRecordset();
        final Record record = recordset.createRecord(caller);

        for (Lookup lookup : lookups) {
            final List<Element> elements = lookup.findAll(Element.Type.VALUE, caller);
            if (elements != null) {
                for (Element element : elements)
                    recordset.updateRecord(record, element);
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

    private void performGroup(Element caller) {
        if (lookups.isEmpty()) return;

        final Recordset recordset = caller.getCell().getTable().getRecordset();

        for (Lookup lookup : lookups) {
            final List<Element> elements = lookup.findAll(Element.Type.VALUE, caller);
            if (elements != null) {
                for (Element element : elements)
                    recordset.updateGroup(caller, element);
            }
        }
    }

    private void performSchema(Element element) {
        if (lookup != null) {
            final Element e = lookup.findFirst(Element.Type.ATTRIBUTE, element);
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

    void perform(Element caller) {
        //log.info("Perform action {} end caller {}", this, caller);
        switch (type) {
            case FACTOR -> performFactor(caller);
            case PREFIX -> performPrefix(caller);
            case SUFFIX -> performSuffix(caller);
            case RECORD -> performRecord(caller);
            case GROUP -> performGroup(caller);
            case SCHEMA -> performSchema(caller);
        }
    }

    public enum Type {
        FACTOR, PREFIX, SUFFIX, GROUP, SCHEMA, RECORD
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
