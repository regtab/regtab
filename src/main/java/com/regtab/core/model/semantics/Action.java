package com.regtab.core.model.semantics;

import lombok.NonNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.regtab.core.model.recordset.Record;
import com.regtab.core.model.recordset.Recordset;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public final class Action implements Provenance {
    @Getter
    private final Action.Type type;

    private final List<Body> bodies = new ArrayList<>();

    public void addBody(@NonNull Body body) {
        bodies.add(body);
    }

    private void performFactor(Element element) {
        for (Body body : bodies) {
            Lookup lookup = body.lookup;
            if (lookup != null) {
                Element foundElem = lookup.findElement(element);
                if (foundElem != null) {
                    String text = foundElem.getText();
                    element.setText(text);
                }
                return;
            }
            String str = body.string;
            if (str != null)
                element.setText(str);
        }
    }

    private static final String SEPARATOR = "/";

    private void performContact(Element element) {
        for (Body body : bodies) {
            Lookup lookup = body.lookup;
            if (lookup != null) {
                Element foundElem = lookup.findElement(element);
                if (foundElem != null) {
                    String prefix = foundElem.getText();
                    if (!prefix.isEmpty()) {
                        String text = element.getText();
                        text = prefix + SEPARATOR + text;
                        element.setText(text);
                    }
                }
                return;
            }
            String str = body.string;
            if (str != null)
                element.setText(str);
        }
    }

    private void performRecord(Element element) {
        if (bodies.isEmpty()) return;

        Recordset recordset = element.getCell().getTable().getRecordset();

        final Record record = recordset.createRecord(element);

        for (Body body : bodies) {
            Lookup lookup = body.lookup;
            if (lookup != null) {
                List<Element> elements = lookup.findElements(Element.Type.VALUE, element);
                if (elements != null) {
                    for (Element elem : elements)
                        recordset.updateRecord(record, elem);
                }
            } else {
                String str = body.string;
                if (str == null) return;

                String attrName;
                String valStr;
                int sepPos = str.indexOf(':');

                if (sepPos > -1) {
                    attrName = str.substring(0, sepPos);
                    valStr = str.substring(sepPos + 1, str.length());

                    if (attrName.isBlank() || valStr.isBlank())
                        throw new IllegalStateException(
                                "Недопустимое значение параметра STRING действия RECORD"
                        );

                    recordset.updateRecord(record, attrName, valStr);
                } else {
                    valStr = str;

                    if (valStr.isBlank())
                        throw new IllegalStateException(
                                "Недопустимое значение параметра STRING действия RECORD"
                        );

                    recordset.updateRecord(record, valStr);
                }
            }
        }
    }

    private void performGroup(Element element) {
        Recordset recordset = element.getCell().getTable().getRecordset();
        for (Body body : bodies) {
            Lookup lookup = body.lookup;
            if (lookup != null) {
                List<Element> elements = lookup.findElements(Element.Type.VALUE, element);
                if (elements != null) {
                    for (Element e : elements)
                        recordset.updateGroup(element, e);
                }
            }
        }
    }

    private void performSchema(Element element) {
        Recordset recordset = element.getCell().getTable().getRecordset();
        for (Body body : bodies) {
            Lookup lookup = body.lookup;
            if (lookup != null) {
                Element e = lookup.findElement(Element.Type.ATTRIBUTE, element);
                if (e != null)
                    recordset.updateSchema(element, e);
            } else {
                String str = body.string;
                if (str != null)
                    recordset.updateSchema(element, str);
            }
        }
    }

    public void perform(Element element) {
        //log.info("Perform action {} end element {}", this, element);
        switch (type) {
            case FACTOR -> performFactor(element);
            case CONCAT -> performContact(element);
            case RECORD -> performRecord(element);
            case GROUP -> performGroup(element);
            case SCHEMA -> performSchema(element);
        }
    }

    public enum Type {
        FACTOR, CONCAT, GROUP, SCHEMA, RECORD
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("type", type)
                //.append("lookup", lookup)
                //.append("str", str)
                .toString();
    }

    public record Body(Lookup lookup, String string) {
    }
}
