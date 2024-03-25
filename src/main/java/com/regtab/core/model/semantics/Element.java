package com.regtab.core.model.semantics;

import com.regtab.core.model.ICell;

import lombok.NonNull;
import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public final class Element implements Provenance {
    @Getter
    private final Type type;

    @Getter
    private final ICell cell;

    @Getter
    @Setter
    private String text;

    public Element(@NonNull ICell cell, @NonNull Type type, @NonNull String text) {
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

    public void perform(Action.Type actionType) {
        if (type == Type.SKIPPED) return;

        if (actionType == Action.Type.FACTOR) {
            for (Action action : actions) {
                if (action.getType() == actionType)
                    action.perform(this);
            }
        }
        else if (actionType == Action.Type.CONCAT) {
            for (Action action : actions) {
                if (action.getType() == actionType)
                    action.perform(this);
            }
        }

        if (type == Type.ATTRIBUTE) return;

        if (actionType == Action.Type.RECORD) {
            for (Action action : actions) {
                if (action.getType() == actionType)
                    action.perform(this);
            }
        }
        else if (actionType == Action.Type.GROUP) {
            for (Action action : actions) {
                if (action.getType() == actionType)
                    action.perform(this);
            }
        }
        else if (actionType == Action.Type.SCHEMA) {
            for (Action action : actions) {
                if (action.getType() == actionType)
                    action.perform(this);
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
