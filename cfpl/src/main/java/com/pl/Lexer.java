package com.pl;

import static com.pl.Tokens.*;

class Lexer {
    
    Tokens parseAlpha(String token){
        Tokens type = null;
        if (token.equals("VAR")) {
            // type = KW_VAR;
        }
        else if (token.equals("OUTPUT")) {
            // type = KW_OUTPUT;
        }
        return type;
    }
}
