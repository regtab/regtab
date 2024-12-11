package com.regtab.core.model;

import lombok.NonNull;
import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * The Action class represents an action that can be performed on an Component.
 */
public final class Action {
    @Getter
    private final Action.Type type;

    /**
     * Constructs an Action with the specified type.
     *
     * @param type the type of the action
     */
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

    /**
     * Adds a lookup to the action.
     *
     * @param lookup the lookup to add
     */
    public void addLookup(@NonNull Lookup lookup) {
        if (type == Type.RECORD) {
            lookup.setAll(true);
            lookups.add(lookup);
        } else {
            this.lookup = lookup;
        }
    }

    /**
     * Adds a string to the action.
     *
     * @param string the string to add
     */
    public void addString(@NonNull String string) {
        if (type == Type.RECORD)
            strings.add(string);
        else
            this.string = string;
    }

    private void performFactor(Component caller) {
        if (lookup != null) {
            final Component found = lookup.findFirst(caller);
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

    private void performPrefix(Component caller) {
        final String prefix;
        final String prefixedText;

        if (lookup != null) {
            final Component found = lookup.findFirst(caller);
            if (found != null) {
                prefix = found.getText();
                if (!prefix.isEmpty()) {
                    final String text = caller.getText();
                    prefixedText = String.join(concatSeparator, prefix, text);
                    caller.setText(prefixedText);
                }
            }
            return;
        }
        if (string != null) {
            prefix = string;
            if (!prefix.isEmpty()) {
                final String text = caller.getText();
                prefixedText = String.join(concatSeparator, prefix, text);
                caller.setText(prefixedText);
            }
        }
    }

    private void performSuffix(Component caller) {
        final String suffix;
        final String suffixedText;

        if (lookup != null) {
            final Component found = lookup.findFirst(caller);
            if (found != null) {
                suffix = found.getText();
                if (!suffix.isEmpty()) {
                    final String text = caller.getText();
                    suffixedText = String.join(concatSeparator, text, suffix);
                    caller.setText(suffixedText);
                }
            }
            return;
        }
        if (string != null) {
            suffix = string;
            if (!suffix.isEmpty()) {
                final String text = caller.getText();
                suffixedText = String.join(concatSeparator, text, suffix);
                caller.setText(suffixedText);
            }
        }
    }

    private void performRecord(Component caller, final Recordset recordset) {
        if (lookups.isEmpty() && strings.isEmpty()) return;

        final Recordset.Record record = recordset.createRecord(caller);

        for (Lookup lookup : lookups) {
            final List<Component> components = lookup.findAll(Component.Type.VALUE, caller);
            if (components != null) {
                for (Component component : components)
                    recordset.updateRecord(record, component);
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

    private void performJoin(Component caller, final Recordset recordset) {
        if (lookup != null) {
            final Component e = lookup.findFirst(Component.Type.VALUE, caller);
            if (e != null) {
                recordset.joinRecords(e, caller);
            }
            return;
        }
        if (string != null) {
            // TODO recordset.joinRecords(component, string);
        }
    }

    private void performSchema(Component caller, final Recordset recordset) {
        if (lookup != null) {
            final Component e = lookup.findFirst(Component.Type.ATTRIBUTE, caller);
            if (e != null) {
                recordset.updateSchema(caller, e);
            }
            return;
        }
        if (string != null) {
            recordset.updateSchema(caller, string);
        }
    }

    /**
     * Performs the action on the specified component and recordset.
     *
     * @param caller the component to perform the action on
     * @param recordset the recordset to use
     */
    void perform(@NonNull Component caller, @NonNull Recordset recordset) {
        switch (type) {
            case FACTOR -> performFactor(caller);
            case PREFIX -> performPrefix(caller);
            case SUFFIX -> performSuffix(caller);
            case RECORD -> performRecord(caller, recordset);
            case JOIN -> performJoin(caller, recordset);
            case SCHEMA -> performSchema(caller, recordset);
        }
    }

    /**
     * The Type enum represents the different types of actions that can be performed.
     */
    public enum Type {
        FACTOR, PREFIX, SUFFIX, RECORD, JOIN, SCHEMA
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
