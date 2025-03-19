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

// Паттерн ячейки (cell) включает либо паттерн группы компонентов (components), либо замену (replacement).
cell : label? LSQUARE (components | copy) RSQUARE quantifier? ;

// Паттерн группы компонентов (components) включает компонент (component), структуру (structured) или выбор (choice).
// Может дополняться набором условий (cond).
components : (cond ARROW)? actions? (component | struct | choice) ;

// Элемент (component) включает тип (componentType).
// Может дополняться набором тегов (tags) и набором действий (actions).
component : componentType (ASSIGN expr)? tags? (COLON actions)? ;

// Тип компонента (componentType) может быть атрибутом (ATTRIBUTE), значением (VALUE), или пропускаемым (SKIPPED).
componentType : ATTRIBUTE | VALUE | SKIPPED ;
ATTRIBUTE : 'A' | 'ATTR' ;
VALUE     : 'V' | 'VAL' ;
SKIPPED   : 'S' | 'SKIP' ;

tags : TAG+ ;

// Группа действий (actions) включает одно или несколько действий (action).
actions : action (SEMICOLON action)* ;

// Действие (action) включает тип (actionType) и одно или несколько тел (actionBody).
action : actionType ASSIGN (actionBody | (LPAREN actionBody (SEMICOLON actionBody)* RPAREN)) ;

// Тип действия:
// FACTOR -- заимствовать значение компонента из другого компонента или литерала.
// CONCAT -- соединить значение дочернего компонента со значением родительского компонента или литерала.
// RECORD -- связать значения с записью.
// SCHEMA -- связать значение с атрибутом.
actionType : FACTOR | PREFIX | SUFFIX | RECORD | JOIN | SCHEMA ;

FACTOR : 'FACTOR' ;
PREFIX : 'PREFIX' ;
SUFFIX : 'SUFFIX' ;
RECORD : 'RECORD' ;
JOIN   : 'JOIN' ;
SCHEMA : 'SCHEMA' ;

actionBody : STRING | lookup ;

// Cтруктура (structured) включает паттерн строки текста (line),
struct : LPAREN startText? component (separator component)* endText? RPAREN ;
startText : STRING ;
separator : STRING ;
endText   : STRING ;

// Выбор (choice) из двух тел (choiceBody) по условию (cond):
// если условие (cond) истино, то выбирается левое тело, иначе --- правое.
choice : cond QUESTION (choiceBody VBAR choiceBody) ;
choiceBody : component | struct ;

// Условие (cond) включает одно или несколько логических выражений (ограничений) (expr).
cond : expr (SEMICOLON expr)* ;

// Поиск компонентов (lookup).
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

    | SUB_LEFT
    | SUB_RIGHT
    | SUB_UP
    | SUB_DOWN
    | IN_SUB_ROW
    | IN_SUB_COL
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

SUB_LEFT    : '$LEFT' ;
SUB_RIGHT   : '$RIGHT' ;
SUB_UP      : '$UP' ;
SUB_DOWN    : '$DOWN' ;
IN_SUB_ROW  : '$ROW' ;
IN_SUB_COL  : '$COL' ;

// Область поиска (where).
where
: (range componentIndex? tags?)
| (range componentIndex tags?)
| (range componentIndex? tags)
| (range? componentIndex tags?)
| (range? componentIndex tags)
| (range? componentIndex? tags)
;

// Диапазон ячеек.
range : rowRange | colRange | rowRange colRange | colRange rowRange ;

rowRange   : ROW rangeBody ;
colRange   : COL rangeBody ;
rangeBody  : (relative? INT) | (start DOUBLE_PERIOD end) ;
start      : (relative? INT) | MIN ;
end        : (relative? INT) | MAX ;
relative   : PLUS | MINUS ;

ROW : 'R' ;
COL : 'C' ;
MIN : 'MIN' ;
MAX : 'MAX' ;

// Индекс компонента внутри структурированной ячейки.
componentIndex : 'E' INT ;

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

LineComment
    : '//' ~[\r\n]* -> channel(HIDDEN)
    ;