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
ATTRIBUTE : 'a' | 'attr' ;
VALUE     : 'v' | 'val';
SKIPPED   : 's' | 'skip';

tags : TAG+ ;

// Группа действий (actions) включает одно или несколько действий (action).
actions : action (SEMICOLON action)* ;

// Действие (action) включает тип (actionType) и одно или несколько тел (actionBody).
action : actionType ASSIGN (actionBody | (LPAREN actionBody (SEMICOLON actionBody)* RPAREN)) ;

// Тип действия:
// FACTOR -- заимствовать значение элемента из другого элемента или литерала.
// CONCAT -- соединить значение дочернего элемента со значением родительского элемента или литерала.
// RECORD -- связать значения с записью.
// GROUP  -- связать значения с группой (анонимным атрибутом).
// SCHEMA -- связать значение с атрибутом.
actionType : FACTOR | PREFIX | SUFFIX | RECORD | GROUP | SCHEMA ;

FACTOR : 'factor' ;
PREFIX : 'prefix' ;
SUFFIX : 'suffix' ;
RECORD : 'record' ;
GROUP  : 'group' ;
SCHEMA : 'schema' ;

actionBody : STRING | lookup ;

// Cтруктура (structured) включает паттерн строки текста (line),
struct : LPAREN startText? element (separator element)* endText? RPAREN ;
startText : STRING ;
separator : STRING ;
endText   : STRING ;

// Выбор (choice) из двух тел (choiceBody) по условию (cond):
// если условие (cond) истино, то выбирается левое тело, иначе --- правое.
choice : cond QUESTION (choiceBody OR choiceBody) ;
choiceBody : element | struct ;

// Условие (cond) включает одно или несколько логических выражений (ограничений) (expr).
cond : expr (SEMICOLON expr)* ;

// Поиск элементов (lookup).
lookup : (all? direction) | (LPAREN all? direction (COLON ((where cond?) | (where? cond)))? RPAREN);

// При наличии (all) выполняется поиск всех элементов, иначе только одного.
all : MULT;

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

LEFT   : 'left' ;
RIGHT  : 'right' ;
UP     : 'up' ;
DOWN   : 'down' ;
IN_ROW  : 'row' ;
IN_COL  : 'col' ;
IN_CELL : 'cell' ;

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
rangeBody  : (relative? INT) | (start DOTS end) ;
start      : relative? INT ;
end        : relative? INT ;
relative   : PLUS | MINUS ;

ROW : 'r';
COL : 'c';

elementIndex : 'e' INT ; // Индекс элемента внутри структурированной ячейки.

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
THIS : 'this' ;

bool
 : TRUE | FALSE
 ;

AND : '&' ;
OR  : '|' ;
NOT : '~' ;

TRUE  : 'TRUE' ;
FALSE : 'FALSE' ;

GT       : '>' ;
GE       : '>=' ;
LT       : '<' ;
LE       : '<=' ;
EQ       : '==' ;
NEQ      : '!=' ;
CONTAINS : 'contains' ;
MATCHES  : 'matches' ;

arithmOp
 : PLUS
 | MINUS
 | MULT
 | MOD
 ;

strOp
 : PLUS
 ;

hexOp
 : EQ
 | NEQ
 ;

// Lexer

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

ARROW     : '->' ;
COLON     : ':' ;
SEMICOLON : ';' ;
COMMA     : ',' ;
DOLLAR    : '$' ;
QUESTION  : '?' ;
DOTS      : '..' ;
ASSIGN    : '=' ;

TAG : '#' [a-z_] [a-z_0-9]* {setText(getText().substring(1, getText().length()));} ;

ID : '@' [a-z_] [a-z_0-9]* {setText(getText().substring(1, getText().length()));} ;

INT : [0-9]+ ;

HEX : '0x' [a-f0-9]+ {setText(getText().substring(2, getText().length()));} ;

STRING : '\'' ( ESC | ~[\\'\r\n] )* '\'' {setText(getText().substring(1, getText().length()-1));} ;

fragment ESC : '\\"' | '\\\\' ;

WS : [ \r\t\u000C\n]+ -> channel(HIDDEN) ;
