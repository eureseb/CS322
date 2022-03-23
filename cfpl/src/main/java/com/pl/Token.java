package com.pl;

import static com.pl.TokenType.*;

public class Token {
    private final TokenType type;
    private final String lexeme;
    private final Object literal;
    private final int line;

    public Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public boolean isNotPlusAndNotMinus(){
        return !type.equals(PLUS) && !type.equals(MINUS);
    }
    public boolean isPlusOrMinus(){
        return type.equals(PLUS) || type.equals(MINUS);
    }
    public boolean isMulOrDiv(){
        return type.equals(MULTIPLY) || type.equals(DIVIDE);
    }
    public boolean isIntOrFloat(){
        return type.equals(INT) || type.equals(FLOAT);
    }
    public boolean isEofOrStop(){
        return type.equals(EOF) || type.equals(KW_STOP);
    }
    public TokenType getType() { return type; }
    public String getLexeme() { return lexeme; }
    public Object getLiteral() { return literal; }
    public int getLine() { return line; }
    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", lexeme='" + lexeme + '\'' +
                ", literal=" + literal +
                ", line=" + line +
                '}';
    }
}
