package com.regtab.core.model;

import lombok.NonNull;
import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public final class Element {
    @Getter
    private final Type type;

    @Getter
    private final ICell cell;

    @Getter
    @Setter
    private String text;

    Element(@NonNull ICell cell, @NonNull Type type, @NonNull String text) {
        this.cell = cell;
        this.type = type;
        this.text = text;
    }

    private final List<String> tags = new ArrayList<>();

    public void addTag(@NonNull String tag) {
        tags.add(tag);
    }

    boolean hasTag(String tag) {
        for (String t : tags)
            if (t.equals(tag)) return true;

        return false;
    }

    private final List<Action> actions = new ArrayList<>();

    public void addAction(@NonNull Action action) {
        actions.add(action);
    }

    public void perform(@NonNull Action.Type actionType, @NonNull Recordset recordset) {
        if (type == Type.SKIPPED) return;

        if (actionType == Action.Type.FACTOR) {
            for (Action action : actions) {
                if (action.getType() == actionType)
                    action.perform(this, recordset);
            }
        }
        else if (actionType == Action.Type.PREFIX) {
            for (Action action : actions) {
                if (action.getType() == actionType)
                    action.perform(this, recordset);
            }
        }
        else if (actionType == Action.Type.SUFFIX) {
            for (Action action : actions) {
                if (action.getType() == actionType)
                    action.perform(this, recordset);
            }
        }

        if (type == Type.ATTRIBUTE) return;

        if (actionType == Action.Type.RECORD) {
            for (Action action : actions) {
                if (action.getType() == actionType)
                    action.perform(this, recordset);
            }
        }
        else if (actionType == Action.Type.SCHEMA) {
            for (Action action : actions) {
                if (action.getType() == actionType)
                    action.perform(this, recordset);
            }
        }
    }

    public enum Type {
        SKIPPED, ATTRIBUTE, VALUE
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("type", type)
                .append("text", text)
                .toString();
    }
}
