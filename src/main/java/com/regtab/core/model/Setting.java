package com.regtab.core.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.HashMap;

public class Setting<T> {
    private final String name;

    private Setting(String name) {
        this.name = name.toLowerCase();
    }

    public static Setting<?> get(@NonNull String name) {
        final String cannonic = name.toLowerCase().replaceAll("_", "");
        return setting.get(cannonic);
    }

    private static final Setting<Integer> BASIC_FIELD_INDEX = new Setting<>("@basicFieldIndex");
    private static final Setting<Boolean> SPLIT_COMPONENTS = new Setting<>("@splitComponents");
    private static final Setting<String> COMP_SEPARATOR = new Setting<>("@compSeparator");
    private static final Setting<Boolean> ALIGNED_NAMED_ATTRS = new Setting<>("@alignedNamedAttrs");
    private static final Setting<Boolean> NORMALIZED_SPACES = new Setting<>("@normalizedSpaces");
    private static final Setting<Boolean> NO_HEADER = new Setting<>("@noHeader");

    private static final HashMap<String, Setting<?>> setting = new HashMap<>();

    static {
        setting.put(BASIC_FIELD_INDEX.name, BASIC_FIELD_INDEX);
        setting.put(SPLIT_COMPONENTS.name, SPLIT_COMPONENTS);
        setting.put(COMP_SEPARATOR.name, COMP_SEPARATOR);
        setting.put(ALIGNED_NAMED_ATTRS.name, ALIGNED_NAMED_ATTRS);
        setting.put(NORMALIZED_SPACES.name, NORMALIZED_SPACES);
        setting.put(NO_HEADER.name, NO_HEADER);
    }
}
