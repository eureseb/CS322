package com.pl;

import static com.pl.Tokens.*;

import java.util.regex.Pattern;

class Lexer {
    

    /*
    def checktoken(self, token):
    if re.match(r'\"(.+?)\"', token):
        type = Tokens.STRING
    if (re.match(r'((-*)\d+\.\d+)', token)):
        type = Tokens.FLOAT
    elif (re.match(r'((-*)\d)', token)):
        type = Tokens.INT
    elif (re.match(r'(^[a-zA-Z_$][a-zA-Z_$0-9]*$)', token)):
        type = self.parseAlpha(token)
    elif (re.match(r'.', token)):
        type = self.parseSpecial(token)
    else:
        type = Tokens.ERROR
    return type
    */

    Tokens checktoken(String token){
        Tokens type = null;

        //\"(.+?)\"
        
        if (isString(token)){
            type = Tokens.STRING;
        }

        return type;
    }

    private boolean isString(String token){
        return Pattern.matches("(.+?)", token);
    }
    
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
            type = KW_BOOLEAN;
        }else if (token.equals("START")) {
            type = KW_START;
        }else if (token.equals("STOP")) {
            type = KW_STOP;
        }else if (token.equals("OUTPUT")) {
            type = KW_OUTPUT;
        }else if (token.equals("AND")) {
            type = AND;
        }else if (token.equals("OR")) {
            type = OR;
        }else if (token.equals("NOT")) {
            type = NOT;
        }else if (token.equals("TRUE")) {
            type = BOOL_TRUE;
        }else if (token.equals("FALSE")) {
            type = BOOL_FALSE;
        }else if (token.equals("START")) {
            type = KW_OUTPUT;
        }else if (isIdentifier(token)) {
            type = IDENTIFIER;
        }
        
        return type;
    }

    Tokens parseSpecial(String token){
        Tokens type = null;
        if (token.equals("&")) {
            type = CONCATENATOR;
        }else if (token.equals("=")) {
            type = EQUALS;
        }else if (token.equals("(")) {
            type = PAREN_OPEN;
        }else if (token.equals(")")) {
            type = PAREN_CLOSE;
        }else if (token.equals("+")) {
            type = PLUS;
        }else if (token.equals("-")) {
            type = MINUS;
        }else if (token.equals("*")) {
            type = MULTIPLY;
        }else if (token.equals("/")) {
            type = DIVIDE;
        }else if (token.equals("%")) {
            type = MODULO;
        }else if (token.equals("[")) {
            type = ESCAPE_OPEN;
        }else if (token.equals("]")) {
            type = ESCAPE_CLOSE;
        }else if (token.equals(">")) {
            type = GREATER_THAN;
        }else if (token.equals(">=")) {
            type = GREATER_OR_EQUAL;
        }else if (token.equals("<")) {
            type = LESS_THAN;
        }else if (token.equals("<=")) {
            type = LESS_OR_EQUAL;
        }else if (token.equals("==")) {
            type = LOGICAL_EQUAL;
        }else if (token.equals("!=)")) {
            type = NOT_EQUAL;
        }else{
            type = ERROR;
        }
        
        return type;
    }
}
