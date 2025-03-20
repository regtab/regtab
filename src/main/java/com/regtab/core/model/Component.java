package com.regtab.core.model;

import com.regtab.core.rtl.interpreter.Configurator;
import lombok.NonNull;
import lombok.Getter;

import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.*;

/**
 * The Component class represents a part of a record or schema with a specific type and associated actions.
 */
public final class Component {
//    @Getter
//    private static final Configurator configurator = Configurator.DEFAULT_CONFIGURATOR;
//
//    private static final String DEFAULT_CONCAT_SEPARATOR = "|";
//
//    private static String concatSeparator = DEFAULT_CONCAT_SEPARATOR;
//
//    static {
//        if (configurator != null)
//            concatSeparator = configurator.getConcatSeparator();
//    }

//    @NonNull
//    @Getter
//    @Setter
//    private String componentTextSeparator = "";

    @Getter
    private final Type type;

    @Getter
    private final ICell cell;

    private List<String> textParts = new LinkedList<>();

    public String getText() {
        final String componentTextSeparator = cell.getTable().getComponentTextSeparator();
        return String.join(componentTextSeparator, textParts);
    }

    public List<String> copyTextParts() {
        return List.copyOf(textParts);
    }
    public void prefix(@NonNull Component component) {
        textParts.addAll(0, component.textParts);
    }

    public void suffix(@NonNull Component component) {
        textParts.addAll(component.textParts);
    }

    private void addText(@NonNull String text) {
        final String normalizedText = StringUtils.normalizeSpace(text);
        textParts.add(normalizedText);
    }

    public void prefix(@NonNull String text) {
        final String normalizedText = StringUtils.normalizeSpace(text);
        textParts.addFirst(normalizedText);
    }

    public void suffix(@NonNull String text) {
        final String normalizedText = StringUtils.normalizeSpace(text);
        textParts.addLast(normalizedText);
    }

    public void factor(@NonNull Component component) {
        textParts.clear();
        textParts.addAll(component.textParts);
    }

    public void factor(@NonNull String text) {
        final String normalizedText = StringUtils.normalizeSpace(text);
        textParts.clear();
        textParts.add(normalizedText);
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
        addText(text);
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
                .append("textParts", Arrays.toString(textParts.toArray()))
                .toString();
    }
}
