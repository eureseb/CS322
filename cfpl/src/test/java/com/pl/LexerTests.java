package com.pl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


class LexerTests {
    
    
    @Test
    void checktoken_returns_string_token_when_given_string(){
        //Given 
        String token = "String";
        Lexer lexer = new Lexer();
        //When
        Tokens actual = lexer.checktoken(token);
        Tokens expected = Tokens.STRING;
        //Then
        assertEquals(expected, actual);
    }
}
