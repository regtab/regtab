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

        final ITable table = reader.readTable(sheetIdx);
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
        File xlFile = new File("../../data/rtl/example1.xlsx");
        File xlFile2 = new File("../../data/rtl/example2.xlsx");

        if (true) {
            System.out.printf("TTL = %s: SHEET = %d%n", "0", 0);
            String ttl0 = "[[s][v -> $s('cl')]+][[v -> $s('rl')][v -> $r(row: c0; col: r0); $s('dat')]+]+";
            extract(xlFile, 0, ttl0);

            System.out.printf("TTL = %s : SHEET = %d%n", "0a", 0);
            String ttl0a = "[[S][V]+][[V][V->$R(ROW:C0;COL:R0)]+]+";
            extract(xlFile, 0, ttl0a);

            System.out.printf("TTL = %s : SHEET = %d%n", "1", 1);
            String ttl1 = "[[A]+][[V->$R(*ROW;'DDD:ddd')][V]+ -> $S(COL)]+";
            extract(xlFile, 1, ttl1);

            System.out.printf("TTL = %s : SHEET = %d%n", "1a", 1);
            String ttl1a = "[[s]+][[v->$r(*row;'DDD:ddd')][v]+]+";
            extract(xlFile, 1, ttl1a);

            System.out.printf("TTL = %s : SHEET = %d%n", "1b", 1);
            String ttl1b = "[[a]+][[v->$r(*row;'ddd')][v]+ -> $s(col)]+";
            extract(xlFile, 1, ttl1b);

            System.out.printf("TTL = %s : SHEET = %d%n", "2", 2);
            String ttl2 = "[[S] [V -> $S('CL') : ~@blank]+ [S]{2} [V -> $S('CL')]+]" +
                    "[[V -> $S('RL')] [V -> $R(LEFT : ~(@text matches '[0-9.]+'); COL:R0); $S('DATA') : ~@blank]+ " +
                    "[S] [V -> $S('RL')][V -> $R(LEFT : ~(@text matches '[0-9.]+'); COL:R0); $S('DATA')]+]+";
            extract(xlFile, 2, ttl2);

            System.out.printf("TTL = %s : SHEET = %d%n", "2a", 2);
            String ttl2a = "[[s][v: ~@blank]+ [s]{2}[v]+]" +
                    "[[v][v -> $r(left : ~(@text matches '[0-9.]+'); col: r0) : ~@blank]+ " +
                    "[s][v][v -> $r(left : ~(@text matches '[0-9.]+'); col: r0)]+]+";
            extract(xlFile, 2, ttl2a);

            System.out.printf("TTL = %s : SHEET = %d%n", "2b", 2);
            String ttl2b = "[[S] #c1[V -> $S('CL') : ~@blank]+ [S][S][#c1]+]" +
                    "[#c2[V -> $S('RL')] #c3[V -> $R(LEFT : ~(@text matches '[0-9.]+'); COL:R0); $S('DATA') : ~@blank]+ " +
                    "[s][#c2][#c3]+]+";
            extract(xlFile, 2, ttl2b);

            System.out.printf("TTL = %s : SHEET = %d%n", "3", 3);
            String ttl3 = "[[V->$R(*ROW);$S('A')][('[' v->$S('B') ',' v->$S('C') ',' v->$S('D') ']')]]+";
            extract(xlFile, 3, ttl3);

            System.out.printf("TTL = %s : SHEET = %d%n", "4", 4);
            String ttl4 = "[[('['A']')][V->$R(*COL);$S(ROW:C0)]+][[('['A']')][V->$S(ROW:C0)]+]+";
            extract(xlFile, 4, ttl4);

            System.out.printf("TTL = %s : SHEET = %d%n", "5", 5);
            String ttl5 = "[[V->$S('A')][('{' V->$S('B') ':' V->$R(ROW:C0;CELL);$S('DAT') '}')]]+";
            extract(xlFile, 5, ttl5);

            System.out.printf("TTL = %s : SHEET = %d%n", "6", 6);
            String ttl6 = "[[(v->$s('A') '=' v->$r(cell);$s('DAT'))]+]+";
            extract(xlFile, 6, ttl6);

            System.out.printf("TTL = %s : SHEET = %d%n", "7", 7);
            String ttl7 = "[[a][v->$s(row:c0);$r(*col)]+][[a][v->$s(row:c0)]+]{4}";
            extract(xlFile, 7, ttl7);

            System.out.printf("TTL = %s : SHEET = %d%n", "8", 8);
            String ttl8 = "[[a]+][[v->$s(col:r0);$r(*row)][v->$s(col:r0)]+ : @text != 'x']+";
            extract(xlFile, 8, ttl8);

            System.out.printf("TTL = %s : SHEET = %d%n", "8a", 8);
            String ttl8a = "[[a]+][[v -> $s(col:r0); $r(*row)][v -> $s(col:r0)]+ : ~@blank]+";
            extract(xlFile, 8, ttl8a);

            System.out.printf("TTL = %s : SHEET = %d%n", "8b", 8);
            String ttl8b = "[[a]+][[v->$r(*row)][v]+ -> $s(col:r0)]+";
            extract(xlFile, 8, ttl8b);

            System.out.printf("TTL = %s : SHEET = %d%n", "9", 9);
            String ttl9 = "[[(V->$S('A') ' ' V->$S('B') ' ' V->$S('DAT');$R(*CELL))]+]+";
            extract(xlFile, 9, ttl9);

            System.out.printf("TTL = %s : SHEET = %d%n", "10", 10);
            String ttl10 = "[[s][v#a -> $s('A')]+][[v -> $s('B')][v -> $s('DAT'); $r(up: #a; row: c0)]+ : @text != 'x']+" +
                    "[[s]+][[s][v#a -> $s('A')]+][[v -> $s('B')][v -> $s('DAT'); $r(up: #a; row: c0)]+]+";
            extract(xlFile, 10, ttl10);

            System.out.printf("TTL = %s : SHEET = %d%n", "10a", 10);
            String ttl10a = "#r1[[s][v#a -> $s('A')]+] #r2[[v -> $s('B')][v -> $s('DAT'); $r(up:#a; row:c0)]+ : @text != 'x']+" +
                    "[[s]+][#r1][#r2]+";
            extract(xlFile, 10, ttl10a);

            System.out.printf("TTL = %s : SHEET = %d%n", "11", 11);
            String ttl11 = "[[a][a]][[v -> $s(up);$r(row:c1)][v -> $s(up)]]+";
            extract(xlFile, 11, ttl11);

            System.out.printf("TTL = %s : SHEET = %d%n", "12", 12);
            String ttl12 = "[[V -> $S(DOWN);$R(ROW:C1)][V -> $S(DOWN)]]{2}[[a][a]]";
            extract(xlFile, 12, ttl12);

            System.out.printf("TTL = %s : SHEET = %d%n", "13", 13);
            String ttl13 = "[[A][V -> $S(LEFT);$R(*DOWN)]+][[A][V -> $S(LEFT)]+]+";
            extract(xlFile, 13, ttl13);

            System.out.printf("TTL = %s : SHEET = %d%n", "14", 14);
            String ttl14 = "[[V -> $S(RIGHT);$R(*DOWN)]{2}[A]][[V -> $S(RIGHT)]{2}[A]]+";
            extract(xlFile, 14, ttl14);

            System.out.printf("TTL = %s : SHEET = %d%n", "15", 15);
            String ttl15 = "[[v -> $s('A');$r(row:c1e1)][(v -> $s('B') ',' v -> $s('C'))]]+";
            extract(xlFile, 15, ttl15);

            System.out.printf("TTL = %s : SHEET = %d%n", "15a", 15);
            String ttl15a = "[[V -> $S('A'); $R(ROW:C1#b)] [(V#b -> $S('B') ',' V -> $S('C'))]]+";
            extract(xlFile, 15, ttl15a);

            System.out.printf("TTL = %s : SHEET = %d%n", "16", 16);
            String ttl16 = "[[(v -> $s('ATTR'); $r(CELL:E1) '=' v -> $s('DAT')) | v -> $s('ATTR'); $r('DAT:0') ? @text contains '=']+]+";
            extract(xlFile, 16, ttl16);

            System.out.printf("TTL = %s : SHEET = %d%n", "17", 17);
            String ttl17 = "[[a]+][[v -> $f(up); $s(col:r0); $r(*row) | v -> $s(col:r0); $r(*row) ? @blank]" +
                    "[v -> $f(up); $s(col:r0) | v -> $s(col:r0) ? @blank]+]+";
            extract(xlFile, 17, ttl17);

            System.out.printf("TTL = %s : SHEET = %d%n", "18", 18);
            String ttl18 = "[[S][V]+][[S][V->$C(UP);$S('A|B')]+][[V->$S('C')][V->$S('DAT');$R(COL:R1;ROW:C0)]+]+";
            extract(xlFile, 18, ttl18);

            System.out.printf("TTL = %s : SHEET = %d%n", "19", 19);
            String ttl19 = "[[A]" +
                    "[V -> $S(ROW:C0); $R(*COL;*COL:C4)]" +
                    "[V -> $S(ROW:C0); $R(*COL;*COL:C5)]" +
                    "[A]" +
                    "[V -> $S(ROW:C3)]{2}]" +
                    "[[A]" +
                    "[V -> $S(ROW:C0)]" +
                    "[V -> $S(ROW:C0)]" +
                    "[A]" +
                    "[V -> $S(ROW:C3)]{2}]+";
            extract(xlFile, 19, ttl19);

            System.out.printf("TTL = %s : SHEET = %d%n", "20", 20);
            String ttl20 = "[[s]{2}[v#cl -> $g(*right)][v#cl]+]{3}" +
                    "[[v#cr -> $g(*down)]{2} [v -> $r(*up:#cl;*left:#cr);$s('DAT')]+]" +
                    "[[v#cr]{2} [v -> $r(*up:#cl;*left:#cr); $s('DAT')]+]+";
            extract(xlFile, 20, ttl20);

            System.out.printf("TTL = %s : SHEET = %d%n", "20a", 21);
            String ttl20a = "[[s: ~@numeric]{2}[v#cl -> $g(*right) : ~@numeric][v#cl : ~@numeric]+]+" +
                    "[[v#cr -> $g(*down)]{2} [v -> $r(*up:#cl;*left:#cr); $s('DAT')]+]" +
                    "[[v#cr]{2} [v -> $r(*up:#cl;*left:#cr); $s('DAT')]+]+";
            extract(xlFile, 21, ttl20a);

            System.out.printf("TTL = %s : SHEET = %d%n", "20b", 21);
            String ttl20b = "[[s: @blank]{2}[v#cl -> $g(*right): @string][v#cl: @string]+]+" +
                    "[[v#cr -> $g(*down)]{2} [v -> $r(*up:#cl;*left:#cr); $s('DAT')]+]" +
                    "[[v#cr]{2} [v -> $r(*up:#cl;*left:#cr); $s('DAT')]+]+";
            extract(xlFile, 21, ttl20b);

            System.out.printf("TTL = %s : SHEET = %d%n", "20c", 22);
            String ttl20c = "[[s: @blank; ~@numeric]+ [v#cl: ~@numeric]+]+" +
                    "[[v#cr: @string]+ [v -> $r(*up:#cl;*left:#cr)]+]+";
            extract(xlFile, 22, ttl20c);

            System.out.printf("TTL = %s : SHEET = %d%n", "23", 23);
            String ttl23 = "[[A]+] #r1[[V->$R(*ROW): ~@blank][V: ~@blank]*[S: @blank]*[V: ~@blank]* -> $S(COL:R0)]+[[s]+ : @blank]*[#r1]*";
            extract(xlFile, 23, ttl23);

            System.out.printf("TTL = %s : SHEET = %d%n", "24", 24);
            String ttl24 = "[[V->$R(*ROW)][V: ~@blank]*[V='na': @blank]*]+";
            extract(xlFile, 24, ttl24);

            System.out.printf("TTL = %s : SHEET = %d%n", "25", 25);
            String ttl25 = "[[a]+][[s: @blank]+]?[[V->$R(*ROW)][V]+ : ~@blank]+";
            extract(xlFile, 25, ttl25);

            System.out.printf("TTL = %s : SHEET = %d%n", "26", 26);
            String ttl26 = "[[V->$R(*ROW): ~@blank][S:@blank]?[V]+]+";
            extract(xlFile, 26, ttl26);

            System.out.printf("TTL = %s: SHEET = %d%n", "27", 27);
            String ttl27 = "[[s][v -> $s('cl')]+] [[s]+] [[v -> $c(up: @indent==0); $s('rl')][v-> $r(row: c0; col: r0); $s('dat')]+]{2}";
            extract(xlFile, 27, ttl27);

            System.out.printf("TTL = %s: SHEET = %d%n", "28", 28);
            String ttl28 = "[[s][v]+] [[v -> $c(up: @indent==this@indent-2)][v-> $r(row: c0; col: r0)]+]+";
            extract(xlFile, 28, ttl28);

            System.out.printf("TTL = %s: SHEET = %d%n", "28a", 28);
            String ttl28a = "[[s][v]+] [[v -> $c(up: this@indent==@indent+2)][v-> $r(row: c0; col: r0)]+]+";
            extract(xlFile, 28, ttl28a);

            System.out.printf("TTL = %s: SHEET = %d%n", "29", 29);
            String ttl29 = "[[v]+]{2} [[v -> $r(up: this@color==@color)]+]{2}";
            extract(xlFile, 29, ttl29);

            System.out.printf("TTL = %s: SHEET = %d%n", "II 0", 0);
            String ttlII0 = "{[[v]+][[v -> $r(up)]+]}+";
            extract(xlFile2, 0, ttlII0);

            System.out.printf("TTL = %s : SHEET = %d%n", "II 1", 1);
            //String ttlII1 = "{[[a]+][[V->$R(*ROW)][V]+ -> $S(COL:R0) : ~@blank]+ [[S: @blank]+]?}+";
            String ttlII1 = "{[[a]+][[V->$R(*ROW)][V]+ -> $S(UP) : ~@blank]+ [[S: @blank]+]?}+";
            extract(xlFile2, 1, ttlII1);

            System.out.printf("TTL = %s : SHEET = %d%n", "II 2", 2);
            String ttlII2 = "[[a]+]{[[V->$R(*ROW;DOWN)][V]+ -> $S(COL)][[V -> $S('D')][S]+]}+";
            //String ttlII2 = "[[a]+]{[[V->$R(*ROW)][V]+ -> $S(COL)][[V -> $R(UP);$S('D')][S]+]}+";
            extract(xlFile2, 2, ttlII2);

            System.out.printf("TTL = %s : SHEET = %d%n", "II 3", 3);
            String ttlII3 = "[[V->$R(*RIGHT:C1..2);$R(*RIGHT:C3..4)][V]{4}]+";
            //String ttlII3 = "[[V->$R(*RIGHT:C1..2;*RIGHT:C3..4)][V]{4}]+";
            extract(xlFile2, 3, ttlII3);

            System.out.printf("TTL = %s : SHEET = %d%n", "II 4", 4);
            String ttlII4 = "[[V->$R(CELL:R2C1)][V][V]] [[V][V->$R(CELL:R0C2)][V]] [[V][V][V->$R(CELL:R0C1)]]";
            extract(xlFile2, 4, ttlII4);

            System.out.printf("TTL = %s : SHEET = %d%n", "II 5", 5);
            String ttlII5 = "[[A]+]{[[V->$R(*ROW;CELL:R+1C+2)][V]{2} -> $S(COL)][[S]{2}[V -> $S('D')]]}+";
            extract(xlFile2, 5, ttlII5);

            System.out.printf("TTL = %s : SHEET = %d%n", "II 6", 6);
            String ttlII6 = "[{[A][V->$R(*COL);$S(LEFT)]{2}}+][{[A][V->$S(LEFT)]{2}}+]+";
            extract(xlFile2, 6, ttlII6);

            System.out.printf("TTL = %s : SHEET = %d%n", "II 7", 7);
            String ttlII7 = "[[A]{[V->$R(*COL);$S(LEFT):~@blank]{2}[S:@blank]?}+][[A]{[V->$S(LEFT):~@blank]{2}[S:@blank]?}+]+";
            extract(xlFile2, 7, ttlII7);

            System.out.printf("TTL = %s : SHEET = %d%n", "II 8", 8);
            String ttlII8 = "[[A]+]{[[V->$R(*RIGHT)][V]+ ->$S(COL)][[S]+]}{3}[[A]+][[V->$R(*RIGHT)][V]+ ->$S(UP)]+";
            extract(xlFile2, 8, ttlII8);

            System.out.printf("TTL = %s : SHEET = %d%n", "II 9", 9);
            //String ttlII9 = "[[A]+][[V->$R(*ROW)][V]+ -> $S(COL)]+";
            String ttlII9 = "[[A=@substr(0,1)]+][[V=@lowerCase()->$R(*ROW)][V=@lowerCase()]+ -> $S(COL)]+";
            extract(xlFile2, 9, ttlII9);

            System.out.printf("TTL = %s : SHEET = %d%n", "II 10", 10);
            String ttlII10 = "{[[a]+][[V->$R(*ROW)][V]+ -> $S(UP) : ~@blank]+ [[S: @blank]+]? : @bold}+";
            extract(xlFile2, 10, ttlII10);

            System.out.printf("TTL = %s : SHEET = %d%n", "II 11", 11);
            String ttlII11 = "[[A]{[V->$R(*COL);$S(LEFT):~@blank]{2}[S:@blank]? : @bold}+][[A]{[V->$S(LEFT):~@blank]{2}[S:@blank]?}+]+";
            extract(xlFile2, 11, ttlII11);

            System.out.printf("TTL = %s : SHEET = %d%n", "II 12", 12);
            String ttlII12 = "{[{[A:~@blank]{3}[S:@blank]?}+][{[V->$R(*ROW:C+1..+2):~@blank][V:~@blank]{2}[S:@blank]?->$S(UP)}+]+[[s]+:@blank]?}+";
            extract(xlFile2, 12, ttlII12);

            System.out.printf("TTL = %s : SHEET = %d%n", "II 13", 13);
            String ttlII13 = "[{[A:~@blank]{3}[S:@blank]?}+]{[{[V->$R(*ROW:C+1..+2):~@blank][V:~@blank]{2}[S:@blank]?}+]+[[s]+:@blank]? ->$S(COL)}+";
            extract(xlFile2, 13, ttlII13);

//            System.out.printf("TTL = %s : SHEET = %d%n", "II 14", 14);
//            String ttlII14 = "[[(A '=' V -> $R(*LINE))(A '=' V)+ -> $S(LINE)]+]+";
//            extract(xlFile2, 14, ttlII14);

//            System.out.printf("TTL = %s : SHEET = %d%n", "II 15", 15);
//            String ttlII15 = "[[A][(A)+]][[V -> $S(COL); $R(*RIGHT)][(V -> $COL:L0)(V -> $COL:L1)]]";
//            extract(xlFile2, 15, ttlII15);
//
//            System.out.printf("TTL = %s : SHEET = %d%n", "II 15a", 15);
//            String ttlII15a = "[[A][(A)+]][[V -> $S(COL); $R(RIGHT:L1)][(V -> $COL:L0)(V -> $COL:L1)]]";
//            extract(xlFile2, 15, ttlII15a);
        }
    }
}
