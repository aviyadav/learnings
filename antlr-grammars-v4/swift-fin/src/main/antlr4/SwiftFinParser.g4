parser grammar SwiftFinParser;

options {
	tokenVocab = SwiftFinLexer ;
}

messages : message+ EOF
         ;

message : block1 block2? block3? block4? block5?
        ;

block1 : BLOCK1 value V_END
       ;

block2 : BLOCK2 value V_END
       ;

block3 : BLOCK3 map RBRACE
       ;

block4 : BLOCK4_A block4Item+ B4_END
       | BLOCK4_B map RBRACE
       ;

block4Item : B4_COLON block4Field B4_COLON block4Line+
           ;

block4Field : B4_VALUE+
            ;

block4Line : B4_VALUE+ B4_CRLF
           | B4_VALUE+ B4_COLON (B4_VALUE | B4_COLON)* B4_CRLF
           | B4_COLON B4_COLON+ (B4_VALUE | B4_COLON)* B4_CRLF
           | B4_COLON+ B4_VALUE+ B4_COLON* B4_CRLF
           ;

block5 : BLOCK5 map RBRACE
       ;

value : V_VALUE+
      ;

map : keyValue+ ;

keyValue : LBRACE mKey M_COLON mValue? RBRACE
         ;

mKey : M_VALUE+
     ;

mValue : (M_VALUE | M_COLON)+
       ;