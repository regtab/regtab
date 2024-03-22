package com.regtab.core.rtl.interpreter.pattern;

import com.regtab.core.model.Action;
import com.regtab.core.model.Condition;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import com.regtab.core.rtl.parser.RTLParser.*;

import java.util.ArrayList;
import java.util.List;

public final class RowPattern extends RepeatablePattern {

    public SubrowsContext subrowsContext;
    public RowPattern(RowContext context) {
        super(context);
    }

    @Override
    public void setCondition(@NonNull Condition condition) {
        super.setCondition(condition);

        for (SubrowPattern subrowTemplate : subrowTemplates) {
            subrowTemplate.setCondition(condition);
        }
    }

    @Getter
    @Setter(AccessLevel.PACKAGE)
    private SubtablePattern subtableTemplate;

    @Getter
    private final List<SubrowPattern> subrowTemplates = new ArrayList<>(1);

    public void add(SubrowPattern tmpl) {
        tmpl.setRowTemplate(this);
        subrowTemplates.add(tmpl);
    }

    public void add(@NonNull Action action) {
        getActions().add(action);

        for (SubrowPattern subrowTemplate : subrowTemplates) {
            subrowTemplate.add(action);
        }
    }

//    public boolean apply(@NonNull CRow row) {
//        final Queue<CCell> cells = new LinkedList<>(row.cells());
//
//        for (SubrowTemplate subrowTemplate : subrowTemplates) {
//            int repetitionCount0 = subrowTemplate.getRepetitionCount();
//            for (int i = 0; i < repetitionCount0; i++) {
//                List<CellTemplate> cellTemplates = subrowTemplate.getCellTemplates();
//                for (CellTemplate cellTemplate : cellTemplates) {
//                    int repetitionCount = cellTemplate.getRepetitionCount();
//                    for (int j = 0; j < repetitionCount; j++) {
//                        CCell cell = cells.poll();
//                        boolean result = cellTemplate.getElementsTemplate().apply(cell);
//                        if (!result)
//                            return false;
//                    }
//                }
//            }
//        }
//
//        return true;
//    }

}
