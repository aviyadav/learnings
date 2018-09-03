lexer grammar SwiftFinLexer;

BLOCK1 : LBrace '1' Colon -> pushMode(InsideValue)
       ;

BLOCK2 : LBrace '2' Colon -> pushMode(InsideValue)
       ;

BLOCK3 : LBrace '3' Colon -> pushMode(InsideMaps)
       ;

fragment
Block4 : LBrace '4' Colon
       ;

BLOCK4_A : Block4 Crlf -> pushMode(InsideB4)
       ;

BLOCK4_B : Block4 -> pushMode(InsideMaps)
       ;


BLOCK5 : LBrace '5' Colon -> pushMode(InsideMaps)
       ;

LBRACE : LBrace;
RBRACE : RBrace;
COLON : Colon;
CRLF : Crlf;

fragment
Crlf : '\r'? '\n'
     ;


fragment LBrace : '{' ;
fragment RBrace : '}' ;
fragment Colon : ':' ;
fragment Minus : '-' ;
fragment Any : . ;

mode InsideMaps;

M_LBRACE : LBrace -> type(LBRACE), pushMode(InsideMaps);
M_RBRACE : RBrace -> type(RBRACE), popMode;
M_COLON  : Colon ;
M_VALUE  : ~[:] ;

mode InsideB4;

B4_END      : Minus RBrace -> popMode;
B4_COLON    : Colon ;
B4_CRLF     : Crlf ;
B4_VALUE    : ~[:] ;
//SPECIALS    : Any ; // every other token

mode InsideValue;
V_END   : RBrace -> popMode;
V_VALUE : Any ;