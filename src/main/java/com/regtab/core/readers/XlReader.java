package com.regtab.core.readers;

import com.regtab.core.model.*;
import com.regtab.core.model.format.SSDatatype;
import com.regtab.core.model.style.*;

import lombok.extern.java.Log;
import lombok.NonNull;
import lombok.Getter;
import lombok.Setter;

import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.awt.Color;

@Log
public final class XlReader {
    private final Workbook workbook;
    private final int numOfSheets;
    private static final DataFormatter formatter = new DataFormatter();
    private final FormulaEvaluator formulaEvaluator;

    @Getter
    @Setter
    private boolean useCellValue;

    public XlReader(@NonNull File xlFile) throws IOException {
        FileInputStream fin = new FileInputStream(xlFile);
        workbook = new XSSFWorkbook(fin);
        numOfSheets = workbook.getNumberOfSheets();
        formulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
    }

    private Sheet getSheet(int sheetIndex) {
        if (sheetIndex < 0 || sheetIndex >= numOfSheets)
            throw new IllegalArgumentException(
                    String.format("Sheet index is not in [0, %d]", numOfSheets - 1)
            );

        Sheet sheet = workbook.getSheetAt(sheetIndex);

        if (sheet == null)
            throw new IllegalStateException("Sheet is null");

        return sheet;
    }

    // Return a range of not empty cells
    private int[] getCellRange(Sheet sheet) {
        int rt = sheet.getFirstRowNum();
        int rb = sheet.getLastRowNum();

        int minCl = Integer.MAX_VALUE;
        int maxCr = Integer.MIN_VALUE;

        for (int i = rt; i <= rb; i++) {
            Row row = sheet.getRow(i);

            if (row == null)
                continue;

            int cl = row.getFirstCellNum();
            int cr = row.getLastCellNum() - 1;
            if (cl < minCl) minCl = cl;
            if (cr > maxCr) maxCr = cr;
        }

        return new int[]{rt, rb, minCl, maxCr};
    }

    public ITable readTable(int sheetIndex) {

        Sheet sheet = getSheet(sheetIndex);

        int[] range = getCellRange(sheet);

        int rt = range[0];
        int rb = range[1];
        int cl = range[2];
        int cr = range[3];

        int numOfRows = rb - rt + 1;
        int numOfCols = cr - cl + 1;

        ITable table = new ITable(numOfRows, numOfCols);

        for (int i = rt; i <= rb; i++) {
            Row xlRow = sheet.getRow(i);

            for (int j = cl; j <= cr; j++) {
                String text = "";
                boolean blank = true;
                int indent = 0;

                CellStyle xlCellStyle;
                Style cellStyle = null;
                CellType xlCellType;
                SSDatatype datatype = null;

                if (xlRow != null) {
                    Cell xlCell = xlRow.getCell(j);

                    if (xlCell != null) {
                        text = getText(xlCell);
                        if (text == null || text.isBlank())
                            text = "";
                        else {
                            blank = false;
                            indent = getIndent(text);
                        }
                        xlCellStyle = xlCell.getCellStyle();
                        cellStyle = createCellStyle(xlCellStyle);
                        xlCellType = xlCell.getCellType();
                        datatype = getDataType(xlCellType);
                    }
                }

                final ICell cell = table.createCell(i, j, text);
                cell.setBlank(blank);
                cell.setIndent(indent);

                cell.setStyle(cellStyle);
                cell.setDatatype(datatype);
            }
        }

        return table;
    }

    private int getIndent(String text) {
        if (!text.isBlank()) {
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (c != 32) return i;
            }
        }
        return 0;
    }

    private SSDatatype getDataType(CellType xlCellType) {
        if (xlCellType == null) return null;

        return switch (xlCellType) {
            case NUMERIC -> SSDatatype.NUMERIC;
            case STRING -> SSDatatype.STRING;
            case BOOLEAN -> SSDatatype.BOOLEAN;
            case FORMULA -> SSDatatype.FORMULA;
            default -> null;
        };
    }

    private Font createFont(XSSFFont xlFont) {
        if (xlFont == null) return null;

        Font font = new Font();

        String fontName = xlFont.getFontName();
        font.setName(fontName);

        int size = xlFont.getFontHeightInPoints();
        font.setSize(size);

        boolean bold = xlFont.getBold();
        font.setBold(bold);

        boolean italic = xlFont.getItalic();
        font.setItalic(italic);

        boolean strikeout = xlFont.getStrikeout();
        font.setStrikeout(strikeout);

        byte underline = xlFont.getUnderline();
        if (underline != XSSFFont.U_NONE)
            font.setUnderline(true);
        if (underline == XSSFFont.U_DOUBLE || underline == XSSFFont.U_DOUBLE_ACCOUNTING)
            font.setDoubleUnderline(true);

        return font;
    }

    private HAlignment createHAlignment(HorizontalAlignment xlHAlignment) {
        if (xlHAlignment == null)
            return null;

        return switch (xlHAlignment) {
            case LEFT -> HAlignment.LEFT;
            case CENTER -> HAlignment.CENTER;
            case RIGHT -> HAlignment.RIGHT;
            case JUSTIFY -> HAlignment.JUSTIFY;
            default -> null;
        };
    }

    private VAlignment createVAlignment(VerticalAlignment xlVAlignment) {
        if (xlVAlignment == null)
            return null;

        return switch (xlVAlignment) {
            case TOP -> VAlignment.TOP;
            case CENTER -> VAlignment.CENTER;
            case BOTTOM -> VAlignment.BOTTOM;
            case JUSTIFY -> VAlignment.JUSTIFY;
            default -> null;
        };
    }

    private boolean checkBorder(BorderStyle xlBorderStyle) {
        if (xlBorderStyle == null)
            return false;

        int code = xlBorderStyle.getCode();
        return code >= 1 && code <= 13;
    }

    private Border createBorder(BorderStyle lBS, BorderStyle tBS, BorderStyle rBS, BorderStyle bBS) {
        boolean left = checkBorder(lBS);
        boolean top = checkBorder(tBS);
        boolean right = checkBorder(rBS);
        boolean bottom = checkBorder(bBS);

        return new Border(left, top, right, bottom);
    }

    private Color createColor(XSSFColor xlColor) {
        // Index 64 means that color is null
        if (xlColor == null || xlColor.getIndexed() == 64)
            return null;

        byte[] rgb = xlColor.getRGBWithTint();
        if (rgb == null)
            return null;

        return new Color(rgb[0] & 0xFF, rgb[1] & 0xFF, rgb[2] & 0xFF);
    }

    private Style createCellStyle(CellStyle xlCellStyle) {
        if (xlCellStyle == null) return null;

        Style cellStyle = new Style();

        XSSFFont xlFont = (XSSFFont) workbook.getFontAt(xlCellStyle.getFontIndex());
        Font font = createFont(xlFont);
        cellStyle.setFont(font);

        int indention = xlCellStyle.getIndention();
        cellStyle.setIndention(indention);

        int rotation = xlCellStyle.getRotation();
        cellStyle.setRotation(rotation);

        HorizontalAlignment xlHAlignment = xlCellStyle.getAlignment();
        HAlignment cHAlignment = createHAlignment(xlHAlignment);
        cellStyle.setHAlignment(cHAlignment);

        VerticalAlignment xlVAlignment = xlCellStyle.getVerticalAlignment();
        VAlignment cVAlignment = createVAlignment(xlVAlignment);
        cellStyle.setVAlignment(cVAlignment);

        BorderStyle lBS = xlCellStyle.getBorderLeft();
        BorderStyle tBS = xlCellStyle.getBorderTop();
        BorderStyle rBS = xlCellStyle.getBorderRight();
        BorderStyle bBS = xlCellStyle.getBorderBottom();
        Border border = createBorder(lBS, tBS, rBS, bBS);
        cellStyle.setBorder(border);

        // Cell style has a foreground color when there is no a background pattern.
        // Otherwise, it has a background color.
        XSSFColor xlColor = (XSSFColor) xlCellStyle.getFillForegroundColorColor();
        if (xlColor == null) {
            xlColor = (XSSFColor) xlCellStyle.getFillBackgroundColorColor();
        }
        Color color = createColor(xlColor);
        cellStyle.setBgColor(color);

        return cellStyle;
    }

    private String getCellValue(Cell xlCell) {
        CellType xlCellType = xlCell.getCellType();

        return switch (xlCellType) {
            case NUMERIC -> Double.toString(xlCell.getNumericCellValue());
            case STRING -> xlCell.getRichStringCellValue().getString();
            case BOOLEAN -> Boolean.toString(xlCell.getBooleanCellValue());
            case FORMULA -> getCellFormulaValue(xlCell);
            default -> "";
        };
    }

    private String getCellFormulaValue(Cell xlCell) {
        return switch (xlCell.getCachedFormulaResultType()) {
            case NUMERIC -> Double.toString(xlCell.getNumericCellValue());
            case STRING -> xlCell.getStringCellValue();
            case BOOLEAN -> Boolean.toString(xlCell.getBooleanCellValue());
            default -> null;
        };
    }

    private String getText(Cell xlCell) {
        return useCellValue ? getCellValue(xlCell) : getFormatCellValue(xlCell);
    }

    private String getFormatCellValue(Cell xlCell) {
        String value = null;

        try {
            formulaEvaluator.evaluate(xlCell);
            value = formatter.formatCellValue(xlCell, formulaEvaluator);
        } catch (FormulaParseException e) {
            log.warning(e.getMessage());
        }

        return value == null ? "" : value;
    }

}