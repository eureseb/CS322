package com.pl;

import static com.pl.Tokens.*;

class Lexer {
    
    Tokens parseAlpha(String token){
        Tokens type = null;
        if (token.equals("VAR")) {
            type = KW_VAR;
        }else if (token.equals("OUTPUT")) {
            type = KW_AS;
        }else if (token.equals("AS")) {
            type = KW_AS;
        }else if (token.equals("INT")) {
            type = KW_INT;
        }else if (token.equals("CHAR")) {
            type = KW_CHAR;
        }else if (token.equals("FLOAT")) {
            type = KW_FLOAT;
        }else if (token.equals("BOOL")) {
            type = KW_BOOL;
        }else if (token.equals("START")) {
            type = KW_START;
        }else if (token.equals("STOP")) {
            type = KW_STOP;
        }else if (token.equals("OUTPUT")) {
            type = KW_OUTPUT;
        }else if (token.equals("AND")) {
            type = KW_AND;
        }else if (token.equals("OR")) {
            type = KW_OR;
        }else if (token.equals("NOT")) {
            type = KW_NOT;
        }else if (token.equals("TRUE")) {
            type = KW_TRUE;
        }else if (token.equals("FALSE")) {
            type = KW_FALSE;
        }else if (token.equals("START")) {
            type = KW_OUTPUT;
        }else if (token.isIdentifier()) {
            type = KW_IDENTIFIER;
        }

        return type;
    }
}
