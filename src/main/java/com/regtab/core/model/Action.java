package com.regtab.core.model;

import lombok.NonNull;
import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public final class Action {
    @Getter
    private final Action.Type type;

    public Action(@NonNull Type type) {
        this.type = type;
        if (type == Type.RECORD) {
            lookups = new ArrayList<>(1);
            strings = new ArrayList<>(1);
        }
    }

    private Lookup lookup;
    private String string;

    private List<Lookup> lookups;
    private List<String> strings;

    public void addLookup(@NonNull Lookup lookup) {
        if (type == Type.RECORD)
            lookups.add(lookup);
        else
            this.lookup = lookup;
    }

    public void addString(@NonNull String string) {
        if (type == Type.RECORD)
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

    private static final String DEFAULT_CONCAT_SEPARATOR = "/";
    private static final String DEFAULT_AV_SEPARATOR = ":";

    @NonNull
    @Getter
    @Setter
    private String concatSeparator = DEFAULT_CONCAT_SEPARATOR;

    @NonNull
    @Getter
    @Setter
    private String avSeparator = DEFAULT_AV_SEPARATOR;

    private void performPrefix(Element caller) {
        if (lookup != null) {
            final Element found = lookup.findFirst(caller);
            if (found != null) {
                final String prefix = found.getText();
                if (!prefix.isEmpty()) {
                    String text = caller.getText();
                    text = prefix + concatSeparator + text;
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
                    text = text + concatSeparator + suffix;
                    caller.setText(text);
                }
            }
            return;
        }
        if (string != null)
            caller.setText(string);
    }

    private void performRecord(Element caller, final Recordset recordset) {
        if (lookups.isEmpty() && strings.isEmpty()) return;

        final Recordset.Record record = recordset.createRecord(caller);

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
            int sepPos = string.indexOf(avSeparator);

            if (sepPos > -1) {
                attrName = string.substring(0, sepPos);
                valStr = string.substring(sepPos + 1);

                if (attrName.isBlank() || valStr.isBlank())
                    throw new IllegalArgumentException("Invalid parameter in action " + this);

                recordset.updateRecord(record, attrName, valStr);
            } else {
                valStr = string;

                if (valStr.isBlank())
                    throw new IllegalArgumentException("Invalid parameter in action " + this);

                recordset.updateRecord(record, valStr);
            }
        }
    }

    private void performSchema(Element element, final Recordset recordset) {
        if (lookup != null) {
            final Element e = lookup.findFirst(Element.Type.ATTRIBUTE, element);
            if (e != null) {
                recordset.updateSchema(element, e);
            }
            return;
        }
        if (string != null) {
            recordset.updateSchema(element, string);
        }
    }

    void perform(@NonNull Element caller, @NonNull Recordset recordset) {
        switch (type) {
            case FACTOR -> performFactor(caller);
            case PREFIX -> performPrefix(caller);
            case SUFFIX -> performSuffix(caller);
            case RECORD -> performRecord(caller, recordset);
            case SCHEMA -> performSchema(caller, recordset);
        }
    }

    public enum Type {
        FACTOR, PREFIX, SUFFIX, SCHEMA, RECORD
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
