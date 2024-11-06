package com.regtab.core.model;

import com.regtab.core.model.format.HtmlTag;
import com.regtab.core.model.format.SSDatatype;
import com.regtab.core.model.style.*;
import com.regtab.core.model.style.Font;

import lombok.NonNull;

import java.awt.*;
import java.util.HashMap;

/**
 * The Prop class represents a property of a cell in a table. It provides methods to evaluate the property value
 * based on the cell's style and other properties.
 */
public final class Prop<T> {
    private final String name;

    private final Evaluator<T> evaluator;

    private Prop(String name, Evaluator<T> evaluator) {
        this.name = name.toLowerCase();
        this.evaluator = evaluator;
    }

    /**
     * Retrieves a property by its name.
     *
     * @param name The name of the property.
     * @return The property with the given name, or null if no such property exists.
     */
    public static Prop<?> get(@NonNull String name) {
        final String cannonic = name.toLowerCase().replaceAll("_", "");
        return props.get(cannonic);
    }

    /**
     * Evaluates the property value for the given cell.
     *
     * @param cell The cell for which to evaluate the property.
     * @return The evaluated property value.
     */
    T eval(@NonNull ICell cell) {
        return evaluator.eval(cell);
    }

    private static final Evaluator<String> fontName = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        Font font = cellStyle.getFont();
        if (font == null) return null;
        String fontName = font.getName();
        if (fontName == null || fontName.isEmpty()) return null;

        return fontName;
    };

    private static final Evaluator<Boolean> fontBold = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        Font font = cellStyle.getFont();
        if (font == null) return null;

        return font.isBold();
    };

    private static final Evaluator<Boolean> fontItalic = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        Font font = cellStyle.getFont();
        if (font == null) return null;

        return font.isItalic();
    };

    private static final Evaluator<Boolean> fontStrikeout = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        Font font = cellStyle.getFont();
        if (font == null) return null;

        return font.isStrikeout();
    };

    private static final Evaluator<Boolean> fontUnderline = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        Font font = cellStyle.getFont();
        if (font == null) return null;

        return font.isUnderline();
    };

    private static final Evaluator<Boolean> fontDoubleUnderline = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        Font font = cellStyle.getFont();
        if (font == null) return null;

        return font.isDoubleUnderline();
    };

    private static final Evaluator<Integer> fontSize = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        Font font = cellStyle.getFont();
        if (font == null) return null;

        return font.getSize();
    };

    private static final Evaluator<Boolean> hLeft = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        HAlignment hAlignment = cellStyle.getHAlignment();
        if (hAlignment == null) return null;

        return hAlignment.isLeft();
    };

    private static final Evaluator<Boolean> hCenter = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        HAlignment hAlignment = cellStyle.getHAlignment();
        if (hAlignment == null) return null;

        return hAlignment.isCenter();
    };

    private static final Evaluator<Boolean> hRight = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        HAlignment hAlignment = cellStyle.getHAlignment();
        if (hAlignment == null) return null;

        return hAlignment.isRight();
    };

    private static final Evaluator<Boolean> hJustify = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        HAlignment hAlignment = cellStyle.getHAlignment();
        if (hAlignment == null) return null;

        return hAlignment.isJustify();
    };

    private static final Evaluator<Boolean> vTop = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        VAlignment vAlignment = cellStyle.getVAlignment();
        if (vAlignment == null) return null;

        return vAlignment.isTop();
    };

    private static final Evaluator<Boolean> vCenter = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        VAlignment vAlignment = cellStyle.getVAlignment();
        if (vAlignment == null) return null;

        return vAlignment.isCenter();
    };

    private static final Evaluator<Boolean> vBottom = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        VAlignment vAlignment = cellStyle.getVAlignment();
        if (vAlignment == null) return null;

        return vAlignment.isBottom();
    };

    private static final Evaluator<Boolean> vJustify = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        VAlignment vAlignment = cellStyle.getVAlignment();
        if (vAlignment == null) return null;

        return vAlignment.isJustify();
    };

    private static final Evaluator<String> bgColor = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        Color bgColor = cellStyle.getBgColor();
        if (bgColor == null) return null;

        String hex = Integer.toHexString(bgColor.getRGB());
        return hex.substring(2);
    };

    private static final Evaluator<Integer> indent = ICell::getIndent;

    private static final Evaluator<String> text = ICell::getText;

    private static final Evaluator<Integer> asInteger = ICell::asInteger;

    private static final Evaluator<Double> asDouble = ICell::asDouble;

    private static final Evaluator<Boolean> blank = ICell::isBlank;

    private static final Evaluator<Boolean> caps = ICell::isCaps;

    private static final Evaluator<Boolean> merged = ICell::isMerged;

    private static final Evaluator<Boolean> hidden = ICell::isHidden;

    private static final Evaluator<Boolean> htmlTagTH = cell -> {
        HtmlTag htmlTag = cell.getHtmlTag();
        if (htmlTag == null) return false;
        return htmlTag.isTh();
    };

    private static final Evaluator<Boolean> htmlTagTD = cell -> {
        HtmlTag htmlTag = cell.getHtmlTag();
        if (htmlTag == null) return false;
        return htmlTag.isTd();
    };

    private static final Evaluator<Boolean> ssDTNumeric = cell -> {
        SSDatatype ssDatatype = cell.getDatatype();
        if (ssDatatype == null) return false;
        return ssDatatype.isNumeric();
    };

    private static final Evaluator<Boolean> ssDTString = cell -> {
        SSDatatype ssDatatype = cell.getDatatype();
        if (ssDatatype == null) return false;
        return ssDatatype.isStr();
    };

    private static final Evaluator<Boolean> ssDTBoolean = cell -> {
        SSDatatype ssDatatype = cell.getDatatype();
        if (ssDatatype == null) return false;
        return ssDatatype.isBool();
    };

    private static final Evaluator<Boolean> ssDTFormula = cell -> {
        SSDatatype ssDatatype = cell.getDatatype();
        if (ssDatatype == null) return false;
        return ssDatatype.isFormula();
    };

    private static final Evaluator<Integer> r = ICell::r;
    private static final Evaluator<Integer> c = ICell::c;
    private static final Evaluator<Integer> rt = ICell::rt;
    private static final Evaluator<Integer> rb = ICell::rb;
    private static final Evaluator<Integer> cl = ICell::cl;
    private static final Evaluator<Integer> cr = ICell::cr;

    private static final Prop<String> FONT_NAME = new Prop<>("@fontName", fontName);
    private static final Prop<Boolean> FONT_BOLD = new Prop<>("@bold", fontBold);
    private static final Prop<Boolean> FONT_ITALIC = new Prop<>("@italic", fontItalic);
    private static final Prop<Boolean> FONT_STRIKEOUT = new Prop<>("@strikeout", fontStrikeout);
    private static final Prop<Boolean> FONT_UNDERLINE = new Prop<>("@underline", fontUnderline);
    private static final Prop<Boolean> FONT_DOUBLE_UNDERLINE = new Prop<>("@doubleUnderline", fontDoubleUnderline);
    private static final Prop<Integer> FONT_SIZE = new Prop<>("@fontSize", fontSize);
    private static final Prop<Boolean> HORIZONTAL_ALIGNMENT_LEFT = new Prop<>("@hLeft", hLeft);
    private static final Prop<Boolean> HORIZONTAL_ALIGNMENT_CENTER = new Prop<>("@hCenter", hCenter);
    private static final Prop<Boolean> HORIZONTAL_ALIGNMENT_RIGHT = new Prop<>("@hRight", hRight);
    private static final Prop<Boolean> HORIZONTAL_ALIGNMENT_JUSTIFY = new Prop<>("@hJustify", hJustify);
    private static final Prop<Boolean> VERTICAL_ALIGNMENT_TOP = new Prop<>("@vTop", vTop);
    private static final Prop<Boolean> VERTICAL_ALIGNMENT_CENTER = new Prop<>("@vCenter", vCenter);
    private static final Prop<Boolean> VERTICAL_ALIGNMENT_BOTTOM = new Prop<>("@vBottom", vBottom);
    private static final Prop<Boolean> VERTICAL_ALIGNMENT_JUSTIFY = new Prop<>("@vJustify", vJustify);
    private static final Prop<String> BACKGROUND_COLOR = new Prop<>("@color", bgColor);
    private static final Prop<Integer> INDENT = new Prop<>("@indent", indent);
    private static final Prop<String> TEXT = new Prop<>("@text", text);
    private static final Prop<Integer> AS_INTEGER = new Prop<>("@asInteger", asInteger);
    private static final Prop<Double> AS_DOUBLE = new Prop<>("@asDouble", asDouble);
    private static final Prop<Boolean> BLANK = new Prop<>("@blank", blank);
    private static final Prop<Boolean> CAPS = new Prop<>("@caps", caps);
    private static final Prop<Boolean> MERGED = new Prop<>("@merged", merged);
    private static final Prop<Boolean> HIDDEN = new Prop<>("@hidden", hidden);
    private static final Prop<Boolean> HTML_TAG_TH = new Prop<>("@th", htmlTagTH);
    private static final Prop<Boolean> HTML_TAG_TD = new Prop<>("@td", htmlTagTD);
    private static final Prop<Boolean> SS_DATATYPE_NUMERIC = new Prop<>("@numeric", ssDTNumeric);
    private static final Prop<Boolean> SS_DATATYPE_STRING = new Prop<>("@string", ssDTString);
    private static final Prop<Boolean> SS_DATATYPE_BOOLEAN = new Prop<>("@bool", ssDTBoolean);
    private static final Prop<Boolean> SS_DATATYPE_FORMULA = new Prop<>("@formula", ssDTFormula);

    private static final Prop<Integer> R = new Prop<>("@r", r);
    private static final Prop<Integer> C = new Prop<>("@c", c);
    private static final Prop<Integer> RT = new Prop<>("@rt", rt);
    private static final Prop<Integer> RB = new Prop<>("@rb", rb);
    private static final Prop<Integer> CL = new Prop<>("@cl", cl);
    private static final Prop<Integer> CR = new Prop<>("@cr", cr);

    private static final HashMap<String, Prop<?>> props = new HashMap<>();

    static {
        props.put(FONT_NAME.name, FONT_NAME);
        props.put(FONT_BOLD.name, FONT_BOLD);
        props.put(FONT_ITALIC.name, FONT_ITALIC);
        props.put(FONT_STRIKEOUT.name, FONT_STRIKEOUT);
        props.put(FONT_UNDERLINE.name, FONT_UNDERLINE);
        props.put(FONT_DOUBLE_UNDERLINE.name, FONT_DOUBLE_UNDERLINE);
        props.put(FONT_SIZE.name, FONT_SIZE);
        props.put(HORIZONTAL_ALIGNMENT_LEFT.name, HORIZONTAL_ALIGNMENT_LEFT);
        props.put(HORIZONTAL_ALIGNMENT_CENTER.name, HORIZONTAL_ALIGNMENT_CENTER);
        props.put(HORIZONTAL_ALIGNMENT_RIGHT.name, HORIZONTAL_ALIGNMENT_RIGHT);
        props.put(HORIZONTAL_ALIGNMENT_JUSTIFY.name, HORIZONTAL_ALIGNMENT_JUSTIFY);
        props.put(VERTICAL_ALIGNMENT_TOP.name, VERTICAL_ALIGNMENT_TOP);
        props.put(VERTICAL_ALIGNMENT_CENTER.name, VERTICAL_ALIGNMENT_CENTER);
        props.put(VERTICAL_ALIGNMENT_BOTTOM.name, VERTICAL_ALIGNMENT_BOTTOM);
        props.put(VERTICAL_ALIGNMENT_JUSTIFY.name, VERTICAL_ALIGNMENT_JUSTIFY);
        props.put(BACKGROUND_COLOR.name, BACKGROUND_COLOR);
        props.put(INDENT.name, INDENT);
        props.put(TEXT.name, TEXT);
        props.put(AS_INTEGER.name, AS_INTEGER);
        props.put(AS_DOUBLE.name, AS_DOUBLE);
        props.put(BLANK.name, BLANK);
        props.put(CAPS.name, CAPS);
        props.put(MERGED.name, MERGED);
        props.put(HIDDEN.name, HIDDEN);
        props.put(HTML_TAG_TH.name, HTML_TAG_TH);
        props.put(HTML_TAG_TD.name, HTML_TAG_TD);
        props.put(SS_DATATYPE_NUMERIC.name, SS_DATATYPE_NUMERIC);
        props.put(SS_DATATYPE_STRING.name, SS_DATATYPE_STRING);
        props.put(SS_DATATYPE_BOOLEAN.name, SS_DATATYPE_BOOLEAN);
        props.put(SS_DATATYPE_FORMULA.name, SS_DATATYPE_FORMULA);

        props.put(R.name, R);
        props.put(C.name, C);
        props.put(RT.name, RT);
        props.put(RB.name, RB);
        props.put(CL.name, CL);
        props.put(CR.name, CR);
    }

    /**
     * The Evaluator interface defines a method to evaluate the property value based on the cell's state.
     *
     * @param <T> The type of the property value.
     */
    private interface Evaluator<T> {
        /**
         * Evaluates the property value for the given cell.
         *
         * @param cell The cell for which to evaluate the property.
         * @return The evaluated property value.
         */
        T eval(ICell cell);
    }
    
}
