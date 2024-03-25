package com.regtab.core.model.recordset;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Record {
    private final List<Value> values = new ArrayList<>();

    void addValue(Value value) {
        values.add(value);
    }

    public List<Value> values() {
        return new ArrayList<>(values);
    }

    public int size() {
        return values.size();
    }

    public Value getValue(int index) {
        return values.get(index);
    }

    public static Record join(@NonNull Record record1, @NonNull Record record2) {
        Record join = new Record();
        join.values.addAll(record1.values);
        join.values.addAll(record2.values);
        return join;
    }
}
