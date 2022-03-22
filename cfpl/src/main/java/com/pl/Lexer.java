package com.pl;

import static com.pl.TokenType.*;

import java.util.ArrayList;
import java.util.regex.Pattern;

class Lexer {
    private static final String[] symbol_list = {",", "+", "*", "/", "==", "(", ")", "=", "<=", ">=", "&"};
    private int status = 0;


     Pair<ArrayList<TokenType>, ArrayList<String>> parse(String stmt){
        String statement = replaceStatement(stmt);
        String[] tokens = statement.split(" ");
        ArrayList<TokenType> tokenList = new ArrayList<TokenType>();
        ArrayList<String> actualStringList = new ArrayList<String>();

        int ctr = 0;
        for(String token: tokens){
            ctr++;
            TokenType type = null;
            if (!token.isBlank() ){
                type = checkToken(token);
                // System.out.println(token + type);
            }

            if(type == TokenType.ERROR){
                tokenList.clear();
                tokenList.add(TokenType.ERROR);
                break;
            }
            else {
                tokenList.add(type);
            }
            actualStringList.add(token);

        }

        return new Pair<ArrayList<TokenType>, ArrayList<String>>(tokenList, actualStringList);
    }

    TokenType checkToken(String token){
        TokenType type = null;

        if (isString(token)) {
            type = TokenType.STRING;
        }

        if (isFloat(token)) {
            type = TokenType.FLOAT;
        }

        else if (isInt(token)) {
            type = TokenType.INT;
        }
        
        else if (isAlpha(token)) {
            if (type != null) {
                if (parseAlpha(token) != null) {   
                    type = parseAlpha(token);
                }
            }
        }

        else if (isSpecial(token)) {
            type = parseSpecial(token);
        }
        
        else {
            type = TokenType.ERROR;
        }

        return type;
    }


    TokenType parseAlpha(String token){
        TokenType type = null;
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
        }else if (isIdentifier(token)) {
            type = IDENTIFIER;
        }
        
        return type;
    }

    TokenType parseSpecial(String token){
        TokenType type = null;
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
        }else if (token.equals("!=")) {
            type = NOT_EQUAL;
        }else{
            type = ERROR;
        }
        
        return type;
    }

    String replaceStatement(String statement){
        for(int i = 0; i < symbol_list.length; i++){
            statement = statement.replace(symbol_list[i], ""+symbol_list[i]+"");
        }
        return statement.replace("= =", "==");
    }
    private boolean isString(String token){
        return Pattern.matches("(.+?)", token);
    }

    private boolean isFloat(String token){
        return Pattern.matches("-?\\d+\\.\\d+", token);
    }

    private boolean isInt(String token){
        return Pattern.matches("(-?)\\d+", token);
    }
    
    private boolean isSpecial(String token){
        return Pattern.matches(".", token);
    }

    private boolean isAlpha(String token){
        return Pattern.matches("^[a-zA-Z_$][a-zA-Z_$0-9]*$", token);
    }

    int getStatus(){
        return this.status;
    }

    
}
