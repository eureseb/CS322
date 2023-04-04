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

    public boolean isPlusOrMinus() {
        return type.equals(PLUS) || type.equals(MINUS);
    }

    public boolean isMulOrDiv() {
        return type.equals(MULTIPLY) || type.equals(DIVIDE);
    }

    public boolean isIntOrFloat() {
        return type.equals(INT) || type.equals(FLOAT);
    }

    public boolean isEofOrStop() {
        return type.equals(EOF) || type.equals(KW_STOP);
    }

    public boolean isDataType() {
        return type.equals(KW_INT) || type.equals(KW_FLOAT) || type.equals(KW_BOOLEAN) || type.equals(KW_CHAR)
                || type.equals(INT)
                || type.equals(FLOAT) || type.equals(BOOL) || type.equals(CHAR);
    }

    public boolean hasLiteral() {
        return type.equals(CHAR) || type.equals(BOOL);
    }

    public TokenType getType() {
        return type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public Object getLiteral() {
        return literal;
    }

    public int getLine() {
        return line;
    }

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
