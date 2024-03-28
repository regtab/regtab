package com.regtab.core.model.semantics;

import com.regtab.core.model.format.HtmlTag;
import com.regtab.core.model.ICell;
import com.regtab.core.model.format.SSDatatype;
import com.regtab.core.model.style.*;
import com.regtab.core.model.style.Font;

import lombok.NonNull;


import java.awt.*;
import java.util.HashMap;

public final class Prop<T> {
    private final String name;

    private final String cannonic;

    private final Evaluator<T> evaluator;

    private Prop(String name, Evaluator<T> evaluator) {
        this.name = name;
        this.cannonic = name.toLowerCase();
        this.evaluator = evaluator;
    }

    public static Prop get(@NonNull String name) {
        final String cannonic = name.toLowerCase().replaceAll("_", "");
        return props.get(cannonic);
    }
    
    T eval(@NonNull ICell cell) {
        return evaluator.eval(cell);
    }

    private static final Evaluator fontName = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        Font font = cellStyle.getFont();
        if (font == null) return null;
        String fontName = font.getName();
        if (fontName == null || fontName.isEmpty()) return null;

        return fontName;
    };

    private static final Evaluator fontBold = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        Font font = cellStyle.getFont();
        if (font == null) return null;

        return font.isBold();
    };

    private static final Evaluator fontItalic = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        Font font = cellStyle.getFont();
        if (font == null) return null;

        return font.isItalic();
    };

    private static final Evaluator fontStrikeout = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        Font font = cellStyle.getFont();
        if (font == null) return null;

        return font.isStrikeout();
    };

    private static final Evaluator fontUnderline = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        Font font = cellStyle.getFont();
        if (font == null) return null;

        return font.isUnderline();
    };

    private static final Evaluator fontDoubleUnderline = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        Font font = cellStyle.getFont();
        if (font == null) return null;

        return font.isDoubleUnderline();
    };

    private static final Evaluator fontSize = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        Font font = cellStyle.getFont();
        if (font == null) return null;

        return font.getSize();
    };

    private static final Evaluator hLeft = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        HAlignment hAlignment = cellStyle.getHAlignment();
        if (hAlignment == null) return null;

        return hAlignment.isLeft();
    };

    private static final Evaluator hCenter = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        HAlignment hAlignment = cellStyle.getHAlignment();
        if (hAlignment == null) return null;

        return hAlignment.isCenter();
    };

    private static final Evaluator hRight = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        HAlignment hAlignment = cellStyle.getHAlignment();
        if (hAlignment == null) return null;

        return hAlignment.isRight();
    };

    private static final Evaluator hJustify = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        HAlignment hAlignment = cellStyle.getHAlignment();
        if (hAlignment == null) return null;

        return hAlignment.isJustify();
    };

    private static final Evaluator vTop = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        VAlignment vAlignment = cellStyle.getVAlignment();
        if (vAlignment == null) return null;

        return vAlignment.isTop();
    };

    private static final Evaluator vCenter = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        VAlignment vAlignment = cellStyle.getVAlignment();
        if (vAlignment == null) return null;

        return vAlignment.isCenter();
    };

    private static final Evaluator vBottom = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        VAlignment vAlignment = cellStyle.getVAlignment();
        if (vAlignment == null) return null;

        return vAlignment.isBottom();
    };

    private static final Evaluator vJustify = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        VAlignment vAlignment = cellStyle.getVAlignment();
        if (vAlignment == null) return null;

        return vAlignment.isJustify();
    };

    private static final Evaluator bgColor = cell -> {
        Style cellStyle = cell.getStyle();
        if (cellStyle == null) return null;
        Color bgColor = cellStyle.getBgColor();
        if (bgColor == null) return null;

        String hex = Integer.toHexString(bgColor.getRGB());
        return hex.substring(2, hex.length());
        //return  Integer.toHexString(bgColor.getRGB() & 0xffffff);
    };

    private static final Evaluator indent = ICell::getIndent;

    private static final Evaluator text = ICell::getText;

    private static final Evaluator blank = ICell::isBlank;

    private static final Evaluator merged = ICell::isMerged;

    private static final Evaluator nested = ICell::isNested;

    private static final Evaluator spanning = ICell::isSpanning;

    private static final Evaluator htmlTagTH = cell -> {
        HtmlTag htmlTag = cell.getHtmlTag();
        if (htmlTag == null) return false;
        return htmlTag.isTh();
    };

    private static final Evaluator htmlTagTD = cell -> {
        HtmlTag htmlTag = cell.getHtmlTag();
        if (htmlTag == null) return false;
        return htmlTag.isTd();
    };

    private static final Evaluator ssDTNumeric = cell -> {
        SSDatatype ssDatatype = cell.getDatatype();
        if (ssDatatype == null) return false;
        return ssDatatype.isNumeric();
    };

    private static final Evaluator ssDTString = cell -> {
        SSDatatype ssDatatype = cell.getDatatype();
        if (ssDatatype == null) return false;
        return ssDatatype.isStr();
    };

    private static final Evaluator ssDTBoolean = cell -> {
        SSDatatype ssDatatype = cell.getDatatype();
        if (ssDatatype == null) return false;
        return ssDatatype.isBool();
    };

    private static final Evaluator ssDTFormula = cell -> {
        SSDatatype ssDatatype = cell.getDatatype();
        if (ssDatatype == null) return false;
        return ssDatatype.isFormula();
    };

    private static final Prop FONT_NAME = new Prop("fontName", fontName);
    private static final Prop FONT_BOLD = new Prop("bold", fontBold);
    private static final Prop FONT_ITALIC = new Prop("italic", fontItalic);
    private static final Prop FONT_STRIKEOUT = new Prop("strikeout", fontStrikeout);
    private static final Prop FONT_UNDERLINE = new Prop("underline", fontUnderline);
    private static final Prop FONT_DOUBLE_UNDERLINE = new Prop("doubleUnderline", fontDoubleUnderline);
    private static final Prop FONT_SIZE = new Prop("fontSize", fontSize);
    private static final Prop HORIZONTAL_ALIGNMENT_LEFT = new Prop("hLeft", hLeft);
    private static final Prop HORIZONTAL_ALIGNMENT_CENTER = new Prop("hCenter", hCenter);
    private static final Prop HORIZONTAL_ALIGNMENT_RIGHT = new Prop("hRight", hRight);
    private static final Prop HORIZONTAL_ALIGNMENT_JUSTIFY = new Prop("hJustify", hJustify);
    private static final Prop VERTICAL_ALIGNMENT_TOP = new Prop("vTop", vTop);
    private static final Prop VERTICAL_ALIGNMENT_CENTER = new Prop("vCenter", vCenter);
    private static final Prop VERTICAL_ALIGNMENT_BOTTOM = new Prop("vBottom", vBottom);
    private static final Prop VERTICAL_ALIGNMENT_JUSTIFY = new Prop("vJustify", vJustify);
    private static final Prop BACKGROUND_COLOR = new Prop("color", bgColor);
    private static final Prop INDENT = new Prop("indent", indent);
    private static final Prop TEXT = new Prop("text", text);
    private static final Prop BLANK = new Prop("blank", blank);
    private static final Prop MERGED = new Prop("merged", merged);
    private static final Prop NESTED = new Prop("nested", nested);
    private static final Prop SPANNING = new Prop("spanning", spanning);
    private static final Prop HTML_TAG_TH = new Prop("th", htmlTagTH);
    private static final Prop HTML_TAG_TD = new Prop("td", htmlTagTD);
    private static final Prop SS_DATATYPE_NUMERIC = new Prop("numeric", ssDTNumeric);
    private static final Prop SS_DATATYPE_STRING = new Prop("string", ssDTString);
    private static final Prop SS_DATATYPE_BOOLEAN = new Prop("bool", ssDTBoolean);
    private static final Prop SS_DATATYPE_FORMULA = new Prop("formula", ssDTFormula);

    private static final HashMap<String, Prop> props = new HashMap<>();

    static {
        props.put(FONT_NAME.cannonic, FONT_NAME);
        props.put(FONT_BOLD.cannonic, FONT_BOLD);
        props.put(FONT_ITALIC.cannonic, FONT_ITALIC);
        props.put(FONT_STRIKEOUT.cannonic, FONT_STRIKEOUT);
        props.put(FONT_UNDERLINE.cannonic, FONT_UNDERLINE);
        props.put(FONT_DOUBLE_UNDERLINE.cannonic, FONT_DOUBLE_UNDERLINE);
        props.put(FONT_SIZE.cannonic, FONT_SIZE);
        props.put(HORIZONTAL_ALIGNMENT_LEFT.cannonic, HORIZONTAL_ALIGNMENT_LEFT);
        props.put(HORIZONTAL_ALIGNMENT_CENTER.cannonic, HORIZONTAL_ALIGNMENT_CENTER);
        props.put(HORIZONTAL_ALIGNMENT_RIGHT.cannonic, HORIZONTAL_ALIGNMENT_RIGHT);
        props.put(HORIZONTAL_ALIGNMENT_JUSTIFY.cannonic, HORIZONTAL_ALIGNMENT_JUSTIFY);
        props.put(VERTICAL_ALIGNMENT_TOP.cannonic, VERTICAL_ALIGNMENT_TOP);
        props.put(VERTICAL_ALIGNMENT_CENTER.cannonic, VERTICAL_ALIGNMENT_CENTER);
        props.put(VERTICAL_ALIGNMENT_BOTTOM.cannonic, VERTICAL_ALIGNMENT_BOTTOM);
        props.put(VERTICAL_ALIGNMENT_JUSTIFY.cannonic, VERTICAL_ALIGNMENT_JUSTIFY);
        props.put(BACKGROUND_COLOR.cannonic, BACKGROUND_COLOR);
        props.put(INDENT.cannonic, INDENT);
        props.put(TEXT.cannonic, TEXT);
        props.put(BLANK.cannonic, BLANK);
        props.put(MERGED.cannonic, MERGED);
        props.put(NESTED.cannonic, NESTED);
        props.put(SPANNING.cannonic, SPANNING);
        props.put(HTML_TAG_TH.cannonic, HTML_TAG_TH);
        props.put(HTML_TAG_TD.cannonic, HTML_TAG_TD);
        props.put(SS_DATATYPE_NUMERIC.cannonic, SS_DATATYPE_NUMERIC);
        props.put(SS_DATATYPE_STRING.cannonic, SS_DATATYPE_STRING);
        props.put(SS_DATATYPE_BOOLEAN.cannonic, SS_DATATYPE_BOOLEAN);
        props.put(SS_DATATYPE_FORMULA.cannonic, SS_DATATYPE_FORMULA);
    }

    private interface Evaluator<T> {
        T eval(ICell cell);
    }
    
}
