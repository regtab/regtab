package com.regtab.core.model;

import lombok.NonNull;
import lombok.Getter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Component class represents a part of a record or schema with a specific type and associated actions.
 */
public final class Component {
    @Getter
    private final Type type;

    @Getter
    private final ICell cell;

    @Getter
    private String text;

    public void setText(String text) {
        this.text = StringUtils.normalizeSpace(text);
    }

    /**
     * Constructs a component with the specified cell, type, and text.
     *
     * @param cell The cell associated with this component.
     * @param type The type of this component.
     * @param text The text associated with this component.
     */
    Component(@NonNull ICell cell, @NonNull Type type, @NonNull String text) {
        this.cell = cell;
        this.type = type;
        setText(text);
    }

    private final List<String> tags = new ArrayList<>();

    /**
     * Adds a tag to this component.
     *
     * @param tag The tag to be added.
     */
    public void addTag(@NonNull String tag) {
        tags.add(tag);
    }

    /**
     * Checks if this component has a specific tag.
     *
     * @param tag The tag to check.
     * @return True if the component has the tag, false otherwise.
     */
    boolean hasTag(String tag) {
        for (String t : tags)
            if (t.equals(tag)) return true;

        return false;
    }

    private final List<Action> actions = new ArrayList<>();

    /**
     * Adds an action to this component.
     *
     * @param action The action to be added.
     */
    public void addAction(@NonNull Action action) {
        actions.add(action);
    }

    /**
     * Performs actions of a specific type on this component.
     *
     * @param actionType The type of actions to perform.
     * @param recordset The recordset to perform the actions on.
     */
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
        } else if (actionType == Action.Type.JOIN) {
            for (Action action : actions) {
                if (action.getType() == actionType)
                    action.perform(this, recordset);
            }
        } else if (actionType == Action.Type.SCHEMA) {
            for (Action action : actions) {
                if (action.getType() == actionType)
                    action.perform(this, recordset);
            }
        }
    }

    /**
     * The Type enum represents the different types of components.
     */
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
