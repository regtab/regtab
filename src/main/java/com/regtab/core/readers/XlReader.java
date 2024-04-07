package com.regtab.core.readers;

import com.regtab.core.model.*;
import com.regtab.core.model.format.SSDatatype;
import com.regtab.core.model.style.*;

import lombok.extern.slf4j.Slf4j;
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
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.awt.Color;
import java.util.HashMap;

@Slf4j
public final class XlReader {
    private final Workbook workbook;
    private final int numOfSheets;
    private static final DataFormatter formatter = new DataFormatter();
    private final FormulaEvaluator formulaEvaluator;

    @Getter
    @Setter
    private boolean formatMode;

    @Getter
    @Setter
    private boolean multilineMode;

    public XlReader(@NonNull File xlFile) throws IOException {
        FileInputStream fin = new FileInputStream(xlFile);
        workbook = new XSSFWorkbook(fin);
        numOfSheets = workbook.getNumberOfSheets();
        formulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
    }

    // Return a range of not empty cells
    private CellRangeAddress findCellRangeAddress(Sheet sheet) {
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

        return new CellRangeAddress(rt, rb, minCl, maxCr);
    }

    private final HashMap<CellAddress, Cell> mergedCells = new HashMap<>();

    private void readMergedCells(Sheet sheet) {
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            final CellRangeAddress region = sheet.getMergedRegion(i);
            final int firstRowIdx = region.getFirstRow();
            final int firstColIdx = region.getFirstColumn();
            final int lastRowIdx = region.getLastRow();
            final int lastColIdx = region.getLastColumn();

            final Row xlRow = sheet.getRow(firstRowIdx);
            final Cell xlCell = xlRow.getCell(firstColIdx);

            for (int j = firstRowIdx; j <= lastRowIdx; j++) {
                for (int k = firstColIdx; k <= lastColIdx; k++) {
                    CellAddress address = new CellAddress(j, k);
                    mergedCells.put(address, xlCell);
                }
            }
        }
    }

    public ITable readTable(int sheetIndex, String range) {
        if (sheetIndex < 0 || sheetIndex >= numOfSheets)
            throw new IllegalArgumentException(
                    String.format("Sheet index is not in [0, %d]", numOfSheets - 1)
            );

        final Sheet sheet = workbook.getSheetAt(sheetIndex);

        if (sheet == null)
            throw new IllegalStateException("Sheet is null");

        readMergedCells(sheet);

        final CellRangeAddress cellRangeAddress;
        if (range == null) {
            cellRangeAddress = findCellRangeAddress(sheet);
        } else {
            cellRangeAddress = CellRangeAddress.valueOf(range);
        }

        return readTable(sheet, cellRangeAddress);
    }

    private HashMap<Integer, Integer> countLines(Sheet sheet, CellRangeAddress range) {
        final int rt = range.getFirstRow();
        final int rb = range.getLastRow();
        final int cl = range.getFirstColumn();
        final int cr = range.getLastColumn();

        final HashMap<Integer, Integer> counts = new HashMap<>();

        int rowCount = 0;
        for (int i = rt; i <= rb; i++) {
            final Row xlRow = sheet.getRow(i);

            int countOfLines = 1;

            for (int j = cl; j <= cr; j++) {
                if (xlRow != null) {
                    Cell xlCell = xlRow.getCell(j);
                    if (xlCell != null) {
                        final int r = xlCell.getRowIndex();
                        final int c = xlCell.getColumnIndex();
                        final Cell xlMergedCell = mergedCells.get(new CellAddress(r, c));
                        if (xlMergedCell != null)
                            xlCell = xlMergedCell;

                        String text = getText(xlCell);
                        if (text != null && !text.isBlank()) {
                            final String[] lines = text.split("\\R");
                                if (lines.length > countOfLines)
                                    countOfLines = lines.length;
                        }
                    }
                }
            }
            counts.put(rowCount, countOfLines);
            rowCount++;
        }

        return counts;
    }

    private ITable readTable(Sheet sheet, CellRangeAddress range) {
        final int rt = range.getFirstRow();
        final int rb = range.getLastRow();
        final int cl = range.getFirstColumn();
        final int cr = range.getLastColumn();

        final HashMap<Integer, Integer> counts;
        final int numOfRows;

        if (multilineMode) {
            counts = countLines(sheet, range);
            numOfRows = counts.values().stream().reduce(0, Integer::sum);
        } else {
            counts = null;
            numOfRows = rb - rt + 1;
        }

        final int numOfCols = cr - cl + 1;

        final ITable table = new ITable(numOfRows, numOfCols);

        int rowIndex = 0;
        int lineIndex = 0;

        for (int i = rt; i <= rb; i++) {
            final Row xlRow = sheet.getRow(i);

            final int countOfLines;
            if (multilineMode) {
                assert counts != null;
                countOfLines = counts.get(rowIndex);
            } else {
                countOfLines = 1;
            }

            int colIndex = 0;
            for (int j = cl; j <= cr; j++) {
                Cell xlCell;

                String text = "";
                String[] lines = null;

                if (xlRow != null) {
                    xlCell = xlRow.getCell(j);

                    if (xlCell != null) {
                        final int r = xlCell.getRowIndex();
                        final int c = xlCell.getColumnIndex();
                        final Cell xlMergedCell = mergedCells.get(new CellAddress(r, c));
                        if (xlMergedCell != null)
                            xlCell = xlMergedCell;
                        text = getText(xlCell);
                        if (text != null && !text.isBlank()) {
                            if (multilineMode)
                                lines = text.split("\\R");
                        }
                    }
                } else {
                    xlCell = null;
                }

                int r = lineIndex;
                int c = colIndex;

                if (multilineMode) {
                    for (int k = 0; k < countOfLines; k++) {
                        r = lineIndex + k;
                        if (lines == null) {
                            createCell(table, r, c, "", xlCell);
                        } else {
                            if (k < lines.length) {
                                String line = lines[k];
                                createCell(table, r, c, line, xlCell);
                            } else {
                                createCell(table, r, c, "", xlCell);
                            }
                        }
                    }
                } else {
                    createCell(table, r, c, text, xlCell);
                }

                colIndex++;
            }
            lineIndex += countOfLines;
            rowIndex++;
        }

        return table;
    }

    private void createCell(ITable table, int r, int c, String text, Cell xlCell) {
        final boolean blank;
        final int indent;

        if (text == null || text.isBlank()) {
            text = "";
            blank = true;
            indent = 0;
        } else {
            blank = false;
            indent = getIndent(text);
        }

        final ICell cell = table.createCell(r, c, text);

        cell.setBlank(blank);
        cell.setIndent(indent);

        if (xlCell != null) {
            final CellStyle xlCellStyle = xlCell.getCellStyle();
            final Style cellStyle = createCellStyle(xlCellStyle);
            cell.setStyle(cellStyle);

            final CellType xlCellType = xlCell.getCellType();
            final SSDatatype datatype = getDataType(xlCellType);
            cell.setDatatype(datatype);

            final int rowIndex = xlCell.getRowIndex();
            final int columnIndex = xlCell.getColumnIndex();
            final CellAddress cellAddress =new CellAddress(rowIndex, columnIndex);
            final Cell xlMergedCell = mergedCells.get(cellAddress);
            if (xlMergedCell != null) {
                cell.setMerged(true);
            }
        }

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
        return formatMode ? getFormatCellValue(xlCell) : getCellValue(xlCell);
    }

    private String getFormatCellValue(Cell xlCell) {
        String value = null;

        try {
            formulaEvaluator.evaluate(xlCell);
            value = formatter.formatCellValue(xlCell, formulaEvaluator);
        } catch (FormulaParseException e) {
            log.warn(e.getMessage());
        }

        return value == null ? "" : value;
    }

}