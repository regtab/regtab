package com.regtab.core.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public final class ICol {
    @Getter
    private final int position;

    @Getter
    private final List<ICell> cells = new ArrayList<>();
}
