package com.regtab.core.model.recordset;

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

}
