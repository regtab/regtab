/** RTL -- аббр. от "Regular Table Language" */
grammar RTL ;

options { caseInsensitive = true ; }

// Шаблон таблицы (table) включает однин или несколько шаблонов подтаблиц (subtable).
table : subtable+ ;

// Шаблон подтаблицы включает либо однин или несколько шаблонов строк (row), либо шаблона группы строк (rows).
subtable : (row+) | rows ;
rows : LCURLY row+ (ARROW actions)? (COLON cond)? RCURLY quantifier? ;

label : TAG ;
replacement : TAG ;
quantifier : zeroOrOne | zeroOrMore | oneOrMore | exactly ;
zeroOrOne : QUESTION ;
zeroOrMore : MULT ;
oneOrMore : PLUS ;
exactly : LCURLY INT RCURLY ;

// Шаблон строки (row) включает либо шаблон группы подстрок (subrows), либо замену (replacement).
row : label? LSQUARE ( subrows | replacement) RSQUARE quantifier? ;

// Шаблон группы подстрок (subrows) включает однин или несколько шаблонов подстрок (subrow).
// Может дополняться набором действий (actions) и условий (cond).
subrows : subrow+ (ARROW actions)? (COLON cond)? ;

// Шаблон подстроки (subrow) включает либо однин или несколько шаблонов ячеек (cell), либо шаблон группы ячеек (cells).
subrow : (cell+) | cells ;

// Шаблон группы ячеек (cells) включает однин или несколько шаблонов ячеек (cell).
// Может дополняться набором действий (actions) и условий (cond).
cells : LCURLY cell+ (ARROW actions)? (COLON cond)? RCURLY quantifier? ;

// Шаблон ячейки (cell) включает либо шаблон группы элементов (elements), либо замену (replacement).
cell : label? LSQUARE (elements | replacement) RSQUARE quantifier? ;

// Шаблон группы элементов (elements) включает элемент (element), структуру (structured) или выбор (choice).
// Может дополняться набором условий (cond).
elements : (element | struct | choice) (ARROW actions)? (COLON cond)? ;

// Элемент (element) включает тип (elementType).
// Может дополняться набором тегов (tags) и набором действий (actions).
//element : elementType ('=' data)? tags? (ARROW actions)? ;
element : elementType ('=' expr)? tags? (ARROW actions)? ;

// Тип элемента (elementType) может быть атрибутом (ATTRIBUTE), значением (VALUE), или пропускаемым (SKIPPED).
elementType : ATTRIBUTE | VALUE | SKIPPED ;
ATTRIBUTE : 'a' ;
VALUE     : 'v' ;
SKIPPED   : 's' ;

tags : TAG+ ;

// Группа действий (actions) включает одно или несколько действий (action).
actions : action (SEMICOLON action)* ;

// Действие (action) включает тип (actionType) и одно или несколько тел (actionBody).
action : actionType LPAREN actionBody (SEMICOLON actionBody)* RPAREN ;

// Тип действия:
// FACTOR -- заимствовать значение элемента из другого элемента или литерала.
// CONCAT -- соединить значение дочернего элемента со значением родительского элемента или литерала.
// RECORD -- связать значения с записью.
// GROUP  -- связать значения с группой (анонимным атрибутом).
// SCHEMA -- связать значение с атрибутом.
actionType : FACTOR | CONCAT | RECORD | GROUP | SCHEMA ;

FACTOR : DOLLAR ('f' | 'factor') ;
CONCAT : DOLLAR ('c' | 'concat') ;
RECORD : DOLLAR ('r' | 'record') ;
GROUP  : DOLLAR ('g' | 'group') ;
SCHEMA : DOLLAR ('s' | 'schema') ;

actionBody : STRING | lookup ;

// Cтруктура (structured) включает шаблон строки текста (line),
//struct : (line quantifier?)+;
struct : line ; // struct : LPAREN line (SEMICOLON line)* RPAREN;
line : LPAREN startText? element (separator element)* endText? RPAREN; //TODO либо избавиться от line, либо реализовать его (спецификатор L)
startText : STRING ;
separator : STRING ;
endText   : STRING ;

// Выбор (choice) из двух тел (choiceBody) по условию (cond):
// если условие (cond) истино, то выбирается левое тело, иначе --- правое.
choice : (choiceBody OR choiceBody) QUESTION cond ; // TODO м.б. добавить LPAREN и RPAREN?
//choice : LPAREN (choiceBody OR choiceBody) QUESTION cond RPAREN ;
choiceBody : element | struct ;

// Условие (cond) включает одно или несколько ограничений (constr).
cond : constr (SEMICOLON constr)* ; // TODO constr избыточен
constr : expr ;

// Поиск элементов (lookup).
lookup : all? direction (COLON ((where cond?) | (where? cond)))? ;

// При наличии (all) выполняется поиск всех элементов, иначе только одного.
all : MULT;

// Направление поиска (direction).
direction
    : LEFT
    | RIGHT
    | UP
    | DOWN
    | INROW
    | INCOL
    | INCELL
    ;

// UP -- вверх, DOWN -- вниз, LEFT -- влево, RIGHT -- вправо (по данным направлениям от ячейки);
// INROW -- по строкам слева направо, INCOL -- по столбцам сверху вниз;
// INCELL -- по ячейкам.

LEFT   : 'left' ;
RIGHT  : 'right' ;
UP     : 'up' ;
DOWN   : 'down' ;
INROW  : 'row' ;
INCOL  : 'col' ;
INCELL : 'cell' ;

// Область поиска (where).
where
: (range index? tags?)
| (range index tags?)
| (range index? tags)
| (range? index tags?)
| (range? index tags)
| (range? index? tags)
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

// Индекс линии внутри ячейки.
//index : 'L' INT ;

// Индекс элемента внутри ячейки.
index : '!' INT ;
//index : 'E' INT ;

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
arg : STRING | INT | prop ; // TODO реализовать поддержку аргументов func

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

TAG : '#' [a-z_] [a-z_0-9]* {setText(getText().substring(1, getText().length()));} ;

ID : '@' [a-z_] [a-z_0-9]* {setText(getText().substring(1, getText().length()));} ;

INT : [0-9]+ ;

STRING : '\'' ( ESC | ~[\\'\r\n] )* '\'' {setText(getText().substring(1, getText().length()-1));} ;

fragment ESC : '\\"' | '\\\\' ;

WS : [ \r\t\u000C\n]+ -> channel(HIDDEN) ;
