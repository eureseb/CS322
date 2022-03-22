package com.pl;

import static com.pl.TokenType.*;
import static java.lang.Character.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<Token>();
    private int start = 0;
    private int current = 0;
    public static int line = 1;

    private static final Map<String, TokenType> reserved;

    static {
        reserved = new HashMap<>();
        reserved.put("VAR", KW_VAR);
        reserved.put("AS", KW_AS);
        reserved.put("INT", KW_INT);
        reserved.put("FLOAT", KW_FLOAT);
        reserved.put("CHAR", KW_CHAR);
        reserved.put("BOOL", KW_BOOLEAN);
        reserved.put("START", KW_START);
        reserved.put("STOP", KW_STOP);
        reserved.put("OUTPUT", KW_OUTPUT);
        reserved.put("IF", IF);
        reserved.put("ELSE", ELSE);
        reserved.put("AND", AND);
        reserved.put("OR", OR);
        reserved.put("WHILE", WHILE);
    }

    public Scanner(String source) {
        this.source = source;
    }

    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private char advance() {
        current++;
        return source.charAt(current - 1);
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(':
                addToken(PAREN_OPEN);
                break;
            case ')':
                addToken(PAREN_CLOSE);
                break;
            case ',':
                addToken(COMMA);
                break;
            case '-':
                addToken(MINUS);
                break;
            case '+':
                addToken(PLUS);
                break;
            case '*':
                addToken(MULTIPLY);
                break;
            case '/':
                addToken(DIVIDE);
                break;
            case '%':
                addToken(MODULO);
                break;
            case ':':
                addToken(COLON);
                break;
            case '&':
                addToken(AND);
                break;
            case '>':
                addToken(match('=') ? GREATER_OR_EQUAL : GREATER_THAN);
                break;
            case '<':
                if (match('=')) {
                    addToken(GREATER_OR_EQUAL);
                } else if (match('>')) {
                    addToken(NOT_EQUAL);
                } else {
                    addToken(LESS_THAN);
                }
                break;
            case '=':
                addToken(match('=') ? LOGICAL_EQUAL : EQUALS);
                break;
            case '\n':
                line++;
                break;
            case '\r':
            case '\t':
            case ' ':
                break;
            case '\'':
                character();
                break;
            case '\"':
                bool();
                break;
            default:
                if (isDigit(c)) {
                    integerOrFloat();
                    break;
                } else if (isAlphabetic(c)) {
                    identifier();
                    break;
                }
                error(line, "Syntax error due to " + c);
                break;
        }

    }

    private void integerOrFloat() {
        TokenType t = INT;
        while (isDigit(peek())) {
            advance();
        }
        if (peek() == '.' && isDigit(peekNext())) {
            t = FLOAT;
            advance();
        }
        while (isDigit(peek()))
            advance();
        if (t == INT)
            addToken(INT, Integer.parseInt(source.substring(start, current)));
        else if (t == FLOAT)
            addToken(FLOAT, Float.parseFloat(source.substring(start, current)));
    }

    private void bool() {
        while (peek() != '\"' && !isAtEnd()) {
            if (peek() == '\n')
                line++;
            advance();
        }
    }

    private boolean isAlphaNumeric(char c) {
        return isAlphabetic(c) || isDigit(c);
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) {
            advance();
        }
        String s = source.substring(start, current);
        TokenType t = reserved.get(s);
        if (t == null)
            t = IDENTIFIER;
        addToken(t);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    private boolean match(char expected) {
        if (isAtEnd())
            return false;
        if (source.charAt(current) != expected)
            return false;

        current++;
        return true;
    }

    private void character() {
        while (peek() != '\'' && !isAtEnd()) {
            if (peek() == '\n')
                line++;
            advance();
        }

        // Unterminated string.
        if (isAtEnd()) {
            error(line, "Unterminated character.");
            return;
        }

        // The closing '.''
        advance();

        // Trim the surrounding quotes.
        String value = source.substring(start + 1, current - 1);
        addToken(KW_CHAR, value);
    }

    private char peek() {
        if (isAtEnd())
            return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length())
            return '\0';
        return source.charAt(current + 1);
    }

    private void error(int line, String message) {
        System.out.println("[line " + line + "] Error" + "" + ": " + message);
    }

}
