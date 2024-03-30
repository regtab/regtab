package com.regtab.core;

import com.regtab.core.model.ITable;
import com.regtab.core.model.recordset.Recordset;
import com.regtab.core.printers.Printer;
import com.regtab.core.readers.XlReader;
import com.regtab.core.rtl.Matcher;
import com.regtab.core.rtl.TableMap;
import com.regtab.core.rtl.Pattern;

import java.io.File;
import java.io.IOException;

public class DemoApp {
    private static final Printer printer = new Printer();

    private static void extract(File xlFile, int sheetIdx, String ttl) throws IOException {
        final XlReader reader = new XlReader(xlFile);
        reader.setUseCellValue(false);

        final ITable table = reader.readTable(sheetIdx, false);
        final Pattern pattern = Pattern.compile(ttl);
        if (pattern == null) {
            System.err.println("Не удалось разобрать паттерн");
            System.exit(0);
        }

        final Matcher matcher = pattern.matcher();
        final TableMap map = matcher.match(table);
        if (map == null) {
            System.err.println("Не удалось сопоставить таблицу с паттерном");
            System.exit(0);
        }

        final boolean result = map.apply();
        if (!result) {
            System.err.println("Не удалось применить карту таблицы");
            System.exit(0);
        }

        // Карта таблицы успешно применена

        final Recordset recordset = table.performActions();
        printer.print(recordset);
        System.out.println();
    }

    public static void main(String[] args) throws IOException {
        final File xlFile = new File("../../data/rtl/example1.xlsx");
        final File xlFile2 = new File("../../data/rtl/example2.xlsx");

        if (true) {
            final StringBuffer sb = new StringBuffer();

            System.out.printf("TTL = %s: SHEET = %d%n", "0", 0);
            sb.append("[[skip][val: schema='cl']+]");
            sb.append("[[val: schema='rl'][val: record=((row:c0);(col:r0)); schema='dat']+]+");
            extract(xlFile, 0, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "0a", 0);
            sb.append("[[SKIP][VAL]+]");
            sb.append("[[VAL][VAL: RECORD=((ROW:C0);(COL:R0))]+]+");
            extract(xlFile, 0, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "1", 1);
            sb.append("[[ATTR]+]");
            sb.append("[SCHEMA=COL [VAL: RECORD=(*ROW;'DDD:ddd')][VAL]+]+");
            extract(xlFile, 1, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "1a", 1);
            sb.append("[[skip]+]");
            sb.append("[[val: record=(*row;'DDD:ddd')][val]+]+");
            extract(xlFile, 1, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "1b", 1);
            sb.append("[[attr]+]");
            sb.append("[schema=col [val: record=(*row;'ddd')][val]+]+");
            extract(xlFile, 1, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "2", 2);
            sb.append("[[SKIP] [~@BLANK -> VAL: SCHEMA='CL']+ [SKIP]{2} [VAL: SCHEMA='CL']+]");
            sb.append("[[VAL: SCHEMA='RL'] [~@BLANK -> VAL: RECORD=((LEFT: ~(@TEXT MATCHES '[0-9.]+')); (COL: R0)); SCHEMA='DATA']+");
            sb.append("[SKIP] [VAL: SCHEMA='RL'][VAL: RECORD=((LEFT: ~(@TEXT MATCHES '[0-9.]+')); (COL: R0)); SCHEMA='DATA']+]+");
            extract(xlFile, 2, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "2a", 2);
            sb.append("[[skip][~@blank -> val]+ [s]{2}[v]+]");
            sb.append("[");
            sb.append("[val]");
            sb.append("[~@blank -> val: record=((left: ~(@text matches '[0-9.]+')); (col:r0))]+");
            sb.append("[skip]");
            sb.append("[val]");
            sb.append("[val: record=((left: ~(@text matches '[0-9.]+')); (col: r0))]+]+");
            sb.append("]");
            extract(xlFile, 2, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "2b", 2);
            sb.append("[[skip] #c1[~@blank -> val: schema='cl']+ [skip][skip][#c1]+]");
            sb.append("[#c2[val: schema='rl'] #c3[~@blank -> val: record=((left: ~(@text matches '[0-9.]+')); (col:r0)); schema='data']+ [skip][#c2][#c3]+]+");
            extract(xlFile, 2, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "3", 3);
            sb.append("[[VAL: RECORD=*ROW; SCHEMA='A'][('[' VAL: SCHEMA='B' ',' VAL: SCHEMA='C' ',' VAL: SCHEMA='D' ']')]]+");
            extract(xlFile, 3, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "4", 4);
            sb.append("[[('['ATTR']')][VAL: RECORD=*COL; SCHEMA=(ROW:C0)]+]");
            sb.append("[[('['ATTR']')][VAL: SCHEMA=(ROW:C0)]+]+");
            extract(xlFile, 4, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "5", 5);
            sb.append("[[VAL: SCHEMA='A'][('{' VAL: SCHEMA='B' ':' VAL: RECORD=((ROW:C0);CELL); SCHEMA='DAT' '}')]]+");
            extract(xlFile, 5, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "6", 6);
            sb.append("[[(VAL: SCHEMA='A' '=' VAL: RECORD=CELL; SCHEMA='DAT')]+]+");
            extract(xlFile, 6, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "7", 7);
            sb.append("[[attr][val: schema=(row:c0); record=(*col)]+][[attr][val: schema=(row:c0)]+]{4}");
            extract(xlFile, 7, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "8", 8);
            sb.append("[[attr]+][@text != 'x' -> [val: schema=(col:r0); record=(*row)][val: schema=(col:r0)]+]+");
            extract(xlFile, 8, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "8a", 8);
            sb.append("[[attr]+][~@blank -> [val: schema=(col:r0); record=(*row)][val: schema=(col:r0)]+]+");
            extract(xlFile, 8, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "8b", 8);
            sb.append("[[attr]+][schema=(col:r0) [val: record=(*row)][val]+]+");
            extract(xlFile, 8, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "9", 9);
            sb.append("[[(VAL: SCHEMA='A' ' ' VAL: SCHEMA='B' ' ' VAL: SCHEMA='DAT'; RECORD=*CELL)]+]+");
            extract(xlFile, 9, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "10", 10);
            sb.append("[[skip][val#a : schema='A']+]");
            sb.append("[@text != 'x' -> [val : schema='B'][val : schema='DAT'; record=((up:#a);(row:c0))]+]+");
            sb.append("[[skip]+]");
            sb.append("[[skip][val#a : schema='A']+]");
            sb.append("[[val : schema='B'][val : schema='DAT'; record=((up:#a);(row:c0))]+]+");
            extract(xlFile, 10, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "10a", 10);
            sb.append("#r1[[skip][val#a : schema='A']+]");
            sb.append("#r2[@text != 'x' -> [val : schema='B'][val : schema='DAT'; record=((up:#a); (row:c0))]+]+");
            sb.append("[[skip]+]");
            sb.append("[#r1]");
            sb.append("[#r2]+");
            extract(xlFile, 10, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "11", 11);
            sb.append("[[attr][attr]]");
            sb.append("[[val : schema=up; record=(row:c1)][val: schema=up]]+");
            extract(xlFile, 11, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "12", 12);
            sb.append("[[VAL: SCHEMA=DOWN; RECORD=(ROW:C1)][VAL: SCHEMA=DOWN]]{2}");
            sb.append("[[ATTR][ATTR]]");
            extract(xlFile, 12, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "13", 13);
            sb.append("[[ATTR][VAL: SCHEMA=LEFT; RECORD=*DOWN]+]");
            sb.append("[[ATTR][VAL: SCHEMA=LEFT]+]+");
            extract(xlFile, 13, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "14", 14);
            sb.append("[[VAL: SCHEMA=RIGHT; RECORD=*DOWN]{2}[ATTR]]");
            sb.append("[[VAL: SCHEMA=RIGHT]{2}[ATTR]]+");
            extract(xlFile, 14, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "15", 15);
            sb.append("[[val: schema='A'; record=(row:c1e1)][(val: schema='B' ',' val: schema='C')]]+");
            extract(xlFile, 15, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "15a", 15);
            sb.append("[[VAL: SCHEMA='A'; RECORD=(ROW:C1#b)] [(VAL#b: SCHEMA='B' ',' VAL: SCHEMA='C')]]+");
            extract(xlFile, 15, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "16", 16);
            sb.append("[[@text contains '=' ? (val: schema='ATTR'; record=(cell:e1) '=' val: schema='DAT') | val: schema='ATTR'; record='DAT:0']+]+");
            extract(xlFile, 16, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "17", 17);
            sb.append("[[attr]+]");
            sb.append("[");
            sb.append("[@blank ? val: factor=up; schema=(col:r0); record=(*row) | val: schema=(col:r0); record=(*row)]");
            sb.append("[@blank ? val: factor=up; schema=(col:r0) | val: schema=(col:r0)]+");
            sb.append("]+");
            extract(xlFile, 17, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "18", 18);
            sb.append("[[SKIP][VAL]+]");
            sb.append("[[SKIP][VAL: PREFIX=UP; SCHEMA='A/B']+]");
            sb.append("[[VAL: SCHEMA='C'][VAL: SCHEMA='DAT'; RECORD=((COL:R1);(ROW:C0))]+]+");
            extract(xlFile, 18, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "19", 19);
            sb.append("[");
            sb.append("[ATTR]");
            sb.append("[VAL: SCHEMA=(ROW:C0); RECORD=(*COL;(*COL:C4))]");
            sb.append("[VAL: SCHEMA=(ROW:C0); RECORD=(*COL;(*COL:C5))]");
            sb.append("[ATTR]");
            sb.append("[VAL: SCHEMA=(ROW:C3)]{2}");
            sb.append("]");
            sb.append("[");
            sb.append("[ATTR]");
            sb.append("[VAL: SCHEMA=(ROW:C0)]");
            sb.append("[VAL: SCHEMA=(ROW:C0)]");
            sb.append("[ATTR]");
            sb.append("[VAL: SCHEMA=(ROW:C3)]{2}");
            sb.append("]+");
            extract(xlFile, 19, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "20", 20);
            sb.append("[[skip]{2}[v#cl : group=(*right)][val#cl]+]{3}");
            sb.append("[[val#cr : group=(*down)]{2} [val: record=((*up:#cl);(*left:#cr)); schema='DAT']+]");
            sb.append("[[val#cr]{2} [val : record=((*up:#cl);(*left:#cr)); schema='DAT']+]+");
            extract(xlFile, 20, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "21", 21);
            sb.append("[[~@numeric -> skip]{2}[~@numeric -> val#cl : group=*right][~@numeric -> val#cl]+]+");
            sb.append("[[val#cr : group=*down]{2} [val : record=((*up:#cl);(*left:#cr)); schema='DAT']+]");
            sb.append("[[val#cr]{2} [val : record=((*up:#cl);(*left:#cr)); schema='DAT']+]+");
            extract(xlFile, 21, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "21a", 21);
            sb.append("[[@blank -> skip]{2}[@string -> v#cl : group=*right][@string -> val#cl]+]+");
            sb.append("[[val#cr : group=*down]{2} [val: record=((*up:#cl);(*left:#cr)); schema='DAT']+]");
            sb.append("[[val#cr]{2} [val : record=((*up:#cl);(*left:#cr)); schema='DAT']+]+");
            extract(xlFile, 21, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "22", 22);
            sb.append("[[@blank; ~@numeric -> skip]+ [~@numeric -> val#cl]+]+");
            sb.append("[[@string -> val#cr]+ [val : record=((*up:#cl);(*left:#cr))]+]+");
            extract(xlFile, 22, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "23", 23);
            sb.append("[[attr]+]");
            sb.append("#r1[schema=(col:r0) [~@blank -> val : record=*row][~@blank -> val]*[@blank -> skip]*[~@blank -> val]*]+");
            sb.append("[@blank -> [skip]+]*");
            sb.append("[#r1]*");
            extract(xlFile, 23, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "24", 24);
            sb.append("[[val: record=(*row)][~@blank -> val]*[@blank -> val='na']*]+");
            extract(xlFile, 24, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "25", 25);
            sb.append("[[attr]+]");
            sb.append("[[@blank -> skip]+]?");
            sb.append("[~@blank -> [val : record=(*row)][val]+]+");
            extract(xlFile, 25, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "26", 26);
            sb.append("[[~@BLANK -> VAL: RECORD=(*ROW)][@BLANK -> SKIP]?[VAL]+]+");
            extract(xlFile, 26, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "27", 27);
            sb.append("[[skip][val: schema='cl']+]");
            sb.append("[[skip]+]");
            sb.append("[[val: prefix=(up: @indent==0); schema='rl'][val: record=((row: c0); (col: r0)); schema='dat']+]{2}");
            extract(xlFile, 27, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "28", 28);
            sb.append("[[skip][val]+]");
            sb.append("[[val: prefix=(up: @indent==this@indent-2)][val: record=((row: c0); (col: r0))]+]+");
            extract(xlFile, 28, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "28a", 28);
            sb.append("[[skip][val]+] ");
            sb.append("[[val : prefix=(up: this@indent==@indent+2)][val : record=((row: c0); (col: r0))]+]+");
            extract(xlFile, 28, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "29", 29);
            sb.append("[[val]+]{2}");
            sb.append("[[val : record=(up: this@color==@color)]+]{2}");
            extract(xlFile, 29, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "II 0", 0);
            sb.append("{[[val]+][[val : record=up]+]}+");
            extract(xlFile2, 0, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "II 1", 1);
            sb.append("{");
            sb.append("[[attr]+]");
            sb.append("[~@blank -> schema=up [val: record=*row][val]+]+");
            sb.append("[[@blank -> skip]+]?");
            sb.append("}+");
            extract(xlFile2, 1, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "II 2", 2);
            sb.append("[[attr]+]");
            sb.append("{");
            sb.append("[schema=col [val : record=(*row;down)][val]+]");
            sb.append("[[val : schema='D'][skip]+]");
            sb.append("}+");
            extract(xlFile2, 2, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "II 3", 3);
            sb.append("[[VAL : RECORD=(*RIGHT:C1..2); RECORD=(*RIGHT:C3..4)][V]{4}]+");
            extract(xlFile2, 3, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "II 4", 4);
            sb.append("[[VAL: RECORD=(CELL:R2C1)][VAL][VAL]]");
            sb.append("[[VAL][VAL: RECORD=(CELL:R0C2)][VAL]]");
            sb.append("[[VAL][VAL][VAL: RECORD=(CELL:R0C1)]]");
            extract(xlFile2, 4, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "II 5", 5);
            sb.append("[[ATTR]+]");
            sb.append("{");
            sb.append("[SCHEMA=COL[VAL:RECORD=(*ROW;(CELL:R+1C+2))][VAL]{2}]");
            sb.append("[[SKIP]{2}[VAL:SCHEMA='D']]");
            sb.append("}+");
            extract(xlFile2, 5, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "II 6", 6);
            sb.append("[{[ATTR][VAL:RECORD=*COL;SCHEMA=LEFT]{2}}+]");
            sb.append("[{[ATTR][VAL:SCHEMA=LEFT]{2}}+]+");
            extract(xlFile2, 6, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "II 7", 7);
            sb.append("[[ATTR]{[~@BLANK->VAL:RECORD=*COL;SCHEMA=LEFT]{2}[@BLANK->SKIP]?}+]");
            sb.append("[[ATTR]{[~@BLANK->VAL:SCHEMA=LEFT]{2}[@BLANK->SKIP]?}+]+");
            extract(xlFile2, 7, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "II 8", 8);
            sb.append("[[ATTR]+]");
            sb.append("{");
            sb.append("[SCHEMA=COL[VAL:RECORD=*RIGHT][VAL]+]");
            sb.append("[[SKIP]+]");
            sb.append("}{3}");
            sb.append("[[ATTR]+]");
            sb.append("[SCHEMA=UP[VAL:RECORD=*RIGHT][VAL]+]+");
            extract(xlFile2, 8, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "II 9", 9);
            sb.append("[[attr=@substr(0,1)]+]");
            sb.append("[schema=col [val=@lowerCase() : record=*row][val=@lowerCase()]+]+");
            extract(xlFile2, 9, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "II 10", 10);
            sb.append("{@bold ->");
            sb.append("[[attr]+]");
            sb.append("[~@blank -> schema=up [val: record=*row][val]+]+");
            sb.append("[[@blank -> skip]+]?");
            sb.append("}+");
            extract(xlFile2, 10, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "II 11", 11);
            sb.append("[[ATTR]{@BOLD -> [~@BLANK -> VAL: RECORD=(*COL); SCHEMA=(LEFT)]{2}[@blank -> SKIP]?}+]");
            sb.append("[[ATTR]{[~@BLANK -> VAL : SCHEMA=LEFT]{2}[@BLANK -> SKIP]?}+]+");
            extract(xlFile2, 11, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "II 12", 12);
            sb.append("{");
            sb.append("[{[~@blank -> attr]{3}[@blank -> skip]?}+]");
            sb.append("[{schema=up [~@blank -> val: record=(*row:c+1..+2)][~@blank -> val]{2}");
            sb.append("[@blank -> skip]?}+]+");
            sb.append("[@blank -> [skip]+]?");
            sb.append("}+");
            extract(xlFile2, 12, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "II 13", 13);
            sb.append("[{[~@blank -> attr]{3}[@blank -> skip]?}+]");
            sb.append("{schema=col");
            sb.append("[{[~@blank -> val: record=(*row:c+1..+2)][~@blank -> val]{2}[@blank -> skip]?}+]+[@blank -> [skip]+]?");
            sb.append("}+");
            extract(xlFile2, 13, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "II 14", 14);
            sb.append("{SCHEMA=CELL");
            sb.append("[[(ATTR '=' VAL: RECORD=(*DOWN:R+1..+2))]+]");
            sb.append("[[(ATTR '=' VAL)]+]{2}");
            sb.append("}+");
            extract(xlFile2, 14, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "II 15", 15);
            sb.append("[[ATTR][ATTR]]");
            sb.append("[[SKIP][ATTR]]");
            sb.append("{");
            sb.append("[SCHEMA=(COL:R0) [VAL: RECORD=(*CELL:C+1R+0..+1)][VAL]]");
            sb.append("[[SKIP][VAL: SCHEMA=(COL:R1)]]");
            sb.append("}+");
            extract(xlFile2, 15, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "II 16", 16);
            sb.append("[[@COLOR==0xFFFF00 ? (VAL : RECORD=CELL '=' VAL) | SKIP]+]+");
            extract(xlFile2, 16, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "II 17", 17);
            sb.append("[[V][V : SUFFIX=LEFT][V: RECORD=LEFT]]+");
            extract(xlFile2, 17, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "II 18", 18);
            sb.append("[[V=@token(' ', 1)][V : RECORD=LEFT]]+");
            extract(xlFile2, 18, sb.toString());
            sb.setLength(0);

            System.out.printf("TTL = %s: SHEET = %d%n", "II 19", 19);
            sb.append("[[A]{2}[V #HEAD]+]{2}");
            sb.append("[[V #STUB : SCHEMA=COL]{2}[V : RECORD=((*COL:#HEAD);(*ROW:#STUB))]+]+");
            extract(xlFile2, 19, sb.toString());
            sb.setLength(0);

//            System.out.printf("TTL = %s: SHEET = %d%n", "II 20", 20);
//            sb.append("{SCHEMA=CELL");
//            sb.append("[[(ATTR '=' VAL: RECORD=(*DOWN:R+1..+2))]+]");
//            sb.append("[[(ATTR '=' VAL)]+]{2}");
//            sb.append("}+");
//            extract(xlFile2, 20, sb.toString());
//            sb.setLength(0);
        }
    }
}
