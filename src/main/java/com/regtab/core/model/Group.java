package com.regtab.core.model;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private final List<Value> values = new ArrayList<>();

    public void addValue(Value value) {
        values.add(value);
        value.setGroup(this);
    }

    public void join(Group group) {
        for (Value value : group.values) {
            values.add(value);
            value.setGroup(this);
        }
        group.values.clear();
    }

    void fillAttribute(@NonNull Attribute attr) {
        for (Value value : values)
            attr.addValue(value);
    }
}
