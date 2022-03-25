package com.pl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pl.TokenType.*;
import static java.lang.Character.*;

public class Lexer {
    private final String source;
    private final List<Token> tokens = new ArrayList<Token>();
    private int ctr = 0;
    private Token currToken;
    private Position position;
    private static final Map<String, TokenType> reserved;
    public boolean hadError = false; // temp
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

    Lexer(String source){
        this.source = source;
        this.position = new Position(0, 1, 0, source.length());
        this.currToken = new Token(DEFAULT, "", null, 1);
    }

    
    public List<Token> scanTokens() {

        while (!isAtEnd()) {
            scanToken();
        }
        tokens.add(new Token(EOF, "", null, position.getLine()));

        return tokens;
    }
    private void scanToken() {
        char c = nextCharacterFromSource();
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
                //
                if(currToken.getType().equals(NEWLINE)){
                    comment();
                    break;
                }else if(getPrevCharacter() == '*'){
                    comment();
                }else
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
            case '\r':
            case '\n':
                addToken(NEWLINE);
                break;
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
                error(position.getLine(), "Syntax error due to " + c);
                break;
        }

    }

    private void integerOrFloat() {
        TokenType tType = INT;
        int start = position.getIndex();
        while (isDigit(peek())) {
            nextCharacterFromSource();
        }
        if (peek() == '.' && isDigit(peekNext())) {
            tType = FLOAT;
            nextCharacterFromSource();
        }

        while (isDigit(peek())) {
            nextCharacterFromSource();
        }

        int end = position.getIndex();

        if (tType == INT) {
            addToken(INT, Integer.parseInt(source.substring(start-1, end)), start-1, end+1);
        }
        else if (tType == FLOAT) {
            addToken(FLOAT, Float.parseFloat(source.substring(start-1, end)), start-1, end+1);
        }
    }

    private void bool() {
        while (peek() != '\"' && !isAtEnd()) {
            nextCharacterFromSource();
        }
    }

    private boolean isAlphaNumeric(char c) {
        return isAlphabetic(c) || isDigit(c);
    }

    private void identifier() {
        int start = position.getIndex();
        while (isAlphaNumeric(peek())) {
            nextCharacterFromSource();
        }
        int end = position.getIndex();

        String s = source.substring(start-1, end);
        TokenType t = reserved.get(s);
        if (t == null){
            t = IDENTIFIER;
        }
        addToken(t, null, start-1, end);
    }
    private void addToken(TokenType type) {
        addToken(type, null, position.getIndex()-1, position.getIndex());
        ctr++;
    }

    private void addToken(TokenType type, Object literal,int startPos,int endPos) {
        String text = source.substring(startPos, endPos);
        currToken = new Token(type, text, literal, position.getLine());
        tokens.add(currToken);
    }

    private boolean match(char expected) {
        if (isAtEnd())
            return false;
        if (source.charAt( position.getIndex() ) != expected)
            return false;

        position.advancePosition(getCurrentCharacter());
        return true;
    }

    private void character() {
        int start = position.getIndex();
        while (peek() != '\'' && !isAtEnd()) {
            nextCharacterFromSource();
        }

        // Unterminated string.
        if (isAtEnd() || getCurrentCharacter() != '\'') {
            error(position.getLine(), "Unterminated character.");
            return;
        }

        // The closing '\''
        nextCharacterFromSource();
        int end = position.getIndex();
        // Trim the surrounding quotes.
        String value = source.substring(start + 1, end);
        addToken(KW_CHAR, value, start+1, end);
    }
    private char peek() {
        if (isAtEnd())
            return '\0';
        return getCurrentCharacter();
    }
    private char peekNext() {
        if (position.getIndex() + 1 >= source.length())
            return '\0';
        return source.charAt(position.getIndex() + 1);
    }

    private void error(int line, String message) {
        hadError = true;
        System.out.println("[line: " + position.getLine() + "," + "col: "+ position.getCol() + "] Error" + getCurrentCharacter() + ": " + message);
    }
    private char nextCharacterFromSource() {
        char currentCharacter = getCurrentCharacter();
        position.advancePosition(currentCharacter);
        return currentCharacter;
    }
    private char getCurrentCharacter(){
        return source.charAt(position.getIndex());
    }
    private char getPrevCharacter() {return source.charAt(position.getIndex()-1);}
    private boolean isAtEnd() {
        return position.getIndex() >= source.length();
    }
    //if last.char == null || last.char == '\n'
    //source.charAt(position.getIndex()-1).equals('\n')

    //start =
    //addToken(COMMENT, null, start, end)

    private void comment() {

        nextCharacterFromSource();
        int start = position.getIndex();
        while(!(getCurrentCharacter() == '\n'))
        {
           nextCharacterFromSource();
        }
        int end = position.getIndex();

        addToken(COMMENT, source.substring(start-1, end), start-1, end);

        /*else{
            return;
        }*/
    }//end of comment
}
