/** RTL -- аббр. от "Regular Table Language" */
grammar RTL ;

options { caseInsensitive = true ; }

// Паттерн таблицы (table) включает однин или несколько паттернов подтаблиц (subtable).
table : subtable+ ;

// Паттерн подтаблицы включает либо однин или несколько паттернов строк (row),
// либо одного паттерна группы строк (rows).
subtable : (row+) | rows ;
rows : LCURLY (cond ARROW)? (actions)? row+ RCURLY quantifier? ;

label : TAG ;
copy : TAG ;
quantifier : zeroOrOne | zeroOrMore | oneOrMore | exactly ;
zeroOrOne : QUESTION ;
zeroOrMore : MULT ;
oneOrMore : PLUS ;
exactly : LCURLY INT RCURLY ;

// Паттерн строки (row) включает либо паттерн группы подстрок (subrows), либо копию (copy).
row : label? LSQUARE ( subrows | copy) RSQUARE quantifier? ;

// Паттерн группы подстрок (subrows) включает однин или несколько паттернов подстрок (subrow).
// Может дополняться набором действий (actions) и условий (cond).
subrows : (cond ARROW)? (actions)? subrow+ ;

// Паттерн подстроки (subrow) включает либо однин или несколько паттернов ячеек (cell),
// либо паттерн группы ячеек (cells).
subrow : (cell+) | cells ;

// Паттерн группы ячеек (cells) включает однин или несколько паттернов ячеек (cell).
// Может дополняться набором действий (actions) и условий (cond).
cells : LCURLY (cond ARROW)? (actions)? cell+ RCURLY quantifier? ;

// Паттерн ячейки (cell) включает либо паттерн группы элементов (elements), либо замену (replacement).
cell : label? LSQUARE (elements | copy) RSQUARE quantifier? ;

// Паттерн группы элементов (elements) включает элемент (element), структуру (structured) или выбор (choice).
// Может дополняться набором условий (cond).
elements : (cond ARROW)? actions? (element | struct | choice) ;

// Элемент (element) включает тип (elementType).
// Может дополняться набором тегов (tags) и набором действий (actions).
element : elementType (ASSIGN expr)? tags? (COLON actions)? ;

// Тип элемента (elementType) может быть атрибутом (ATTRIBUTE), значением (VALUE), или пропускаемым (SKIPPED).
elementType : ATTRIBUTE | VALUE | SKIPPED ;
ATTRIBUTE : 'A' | 'ATTR' ;
VALUE     : 'V' | 'VAL' ;
SKIPPED   : 'S' | 'SKIP' ;

tags : TAG+ ;

// Группа действий (actions) включает одно или несколько действий (action).
actions : action (SEMICOLON action)* ;

// Действие (action) включает тип (actionType) и одно или несколько тел (actionBody).
action : actionType ASSIGN (actionBody | (LPAREN actionBody (SEMICOLON actionBody)* RPAREN)) ;

// Тип действия:
// FACTOR -- заимствовать значение элемента из другого элемента или литерала.
// CONCAT -- соединить значение дочернего элемента со значением родительского элемента или литерала.
// RECORD -- связать значения с записью.
// SCHEMA -- связать значение с атрибутом.
actionType : FACTOR | PREFIX | SUFFIX | RECORD | JOIN | SCHEMA ;

FACTOR : 'FACTOR' ;
PREFIX : 'PREFIX' ;
SUFFIX : 'SUFFIX' ;
RECORD : 'RECORD' ;
JOIN : 'JOIN' ;
SCHEMA : 'SCHEMA' ;

actionBody : STRING | lookup ;

// Cтруктура (structured) включает паттерн строки текста (line),
struct : LPAREN startText? element (separator element)* endText? RPAREN ;
startText : STRING ;
separator : STRING ;
endText   : STRING ;

// Выбор (choice) из двух тел (choiceBody) по условию (cond):
// если условие (cond) истино, то выбирается левое тело, иначе --- правое.
choice : cond QUESTION (choiceBody VBAR choiceBody) ;
choiceBody : element | struct ;

// Условие (cond) включает одно или несколько логических выражений (ограничений) (expr).
cond : expr (SEMICOLON expr)* ;

// Поиск элементов (lookup).
lookup : (direction limit?) | (LPAREN direction limit? (COLON ((where cond?) | (where? cond)))? RPAREN);

limit : LCURLY INT RCURLY ;

// Направление поиска (direction).
direction
    : LEFT
    | RIGHT
    | UP
    | DOWN
    | IN_ROW
    | IN_COL
    | IN_CELL
    ;

// UP -- вверх, DOWN -- вниз, LEFT -- влево, RIGHT -- вправо (по данным направлениям от ячейки);
// IN_ROW -- в строках слева направо, IN_COL -- в столбцах сверху вниз, IN_CELL -- в ячейках.

LEFT    : 'LEFT' ;
RIGHT   : 'RIGHT' ;
UP      : 'UP' ;
DOWN    : 'DOWN' ;
IN_ROW  : 'ROW' ;
IN_COL  : 'COL' ;
IN_CELL : 'CELL' ;

// Область поиска (where).
where
: (range elementIndex? tags?)
| (range elementIndex tags?)
| (range elementIndex? tags)
| (range? elementIndex tags?)
| (range? elementIndex tags)
| (range? elementIndex? tags)
;

// Диапазон ячеек.
range : rowRange | colRange | rowRange colRange | colRange rowRange ;

rowRange   : ROW rangeBody ;
colRange   : COL rangeBody ;
rangeBody  : (relative? INT) | (start DOUBLE_PERIOD end) ;
start      : relative? INT ;
end        : relative? INT ;
relative   : PLUS | MINUS ;

ROW : 'R';
COL : 'C';

// Индекс элемента внутри структурированной ячейки.
elementIndex : 'E' INT ;

expr
 : LPAREN expr RPAREN                                 #parenExpr
 | NOT expr                                           #notExpr
 | leftExpr = expr op = arithmOp rightExpr = expr     #arithmExpr
 | leftExpr = expr op = strOp rightExpr = expr        #strExpr
 | leftExpr = expr op = compOp rightExpr = expr       #compExpr
 | leftExpr = expr op = binaryOp rightExpr = expr     #binaryExpr
 | prop                                               #propExpr
 | func                                               #funcExpr
 | THIS (prop | func)                                 #thisExpr
 | INT                                                #intLiteral
 | DOUBLE                                             #doubleLiteral
 | STRING                                             #strLiteral
 | HEX                                                #hexLiteral
 | bool                                               #boolLiteral
 ;

compOp
 : GT | GE | LT | LE | EQ | NEQ | CONTAINS | MATCHES
 ;

binaryOp
 : AND | OR
 ;

// Функция
func : ID LPAREN (arg (COMMA arg)*)? RPAREN ;
arg : STRING | INT ;

// Свойство ячейки
prop : ID ;

// Ячейка, из которой было вызвано данное действие.
THIS : 'THIS' ;

bool
 : TRUE | FALSE
 ;

AND : DOUBLE_AMPERSAND | 'AND' ;
OR  : DOUBLE_VBAR | 'OR' ;
NOT : EXCLAMATION | 'NOT';

TRUE  : 'TRUE' ;
FALSE : 'FALSE' ;

GT       : '>' ;
GE       : '>=' ;
LT       : '<' ;
LE       : '<=' ;
EQ       : '==' ;
NEQ      : '!=' ;
CONTAINS : 'CONTAINS' ;
MATCHES  : 'MATCHES' ;

arithmOp
 : PLUS
 | MINUS
 | MULT
 | MOD
 ;

strOp
    : PLUS
    ;

PLUS  : '+' ;
MINUS : '-' ;
MULT  : '*' ;
MOD   : '%' ;

LPAREN  : '(' ;
RPAREN  : ')' ;
LCURLY  : '{' ;
RCURLY  : '}' ;
LSQUARE : '[' ;
RSQUARE : ']' ;

ARROW       : '->' ;
COLON       : ':' ;
SEMICOLON   : ';' ;
COMMA       : ',' ;
DOLLAR      : '$' ;
QUESTION    : '?' ;
VBAR        : '|' ;
EXCLAMATION : '!' ;

DOUBLE_AMPERSAND : '&&' ;
DOUBLE_VBAR : '||' ;

DOUBLE_PERIOD : '..' ;
ASSIGN    : '=' ;
PERIOD    : '.' ;

TAG : '#' [a-z_] [a-z_0-9]* ;

ID : '@' [a-z_] [a-z_0-9]* ;

INT : [0-9]+ ;

DOUBLE : INT+ PERIOD INT+ ;

HEX : '0x' [a-f0-9]+ ;

STRING
    : '"'      (ESC | '""'   | ~["])* '"'
    | '\''     (ESC | '\'\'' | ~['])* '\''
    | '\u201C' (ESC | .)*? ('\u201D' | '\u2033')   // smart quotes
    ;

fragment ESC
    : '\u0060\''    // backtick single-quote
    | '\u0060"'     // backtick double-quote
    ;

WS : [ \r\t\u000C\n]+ -> channel(HIDDEN) ;