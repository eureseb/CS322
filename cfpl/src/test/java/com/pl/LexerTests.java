package com.pl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LexerTests {
    
    private Lexer lexer = null;
    
    @BeforeEach
    void setup(){
        lexer = new Lexer();
    }

    @Test
    void checkToken_returns_string_token_when_given_string(){
        //Given 
        String token = "Thisastring";
        //When
        Tokens actual = lexer.checkToken(token);
        Tokens expected = Tokens.STRING;
        //Then
        assertEquals(expected, actual);
    }
    @Test
    void checkToken_returns_float_token_when_given_float(){
        //Given 
        String token = "-3.0";
        //When
        Tokens actual = lexer.checkToken(token);
        Tokens expected = Tokens.FLOAT;
        //Then
        assertEquals(expected, actual);
    }

    @Test
    void checkToken_returns_int_token_when_given_int(){
        //Given 
        String token = "3";
        //When
        Tokens actual = lexer.checkToken(token);
        Tokens expected = Tokens.INT;
        //Then
        assertEquals(expected, actual);
    }

    @Test
    void checkToken_returns_alpha_token_when_given_alpha(){

        //Given 
        String token = "VAR";
        //When
        Tokens actual = lexer.checkToken(token);
        Tokens expected = Tokens.KW_VAR;
        //Then
        assertEquals(expected, actual);
    }
  
    @Test
    void checkToken_returns_string_token_when_given_lower_var(){

        //Given 
        String token = "var";
        //When
        Tokens actual = lexer.checkToken(token);
        Tokens expected = Tokens.STRING;
        //Then
        assertEquals(expected, actual);
    }
    @Test
    void checkToken_returns_equals_token_when_given_equals(){
            //Given 
            String token = "=";
            //When
            Tokens actual = lexer.checkToken(token);
            Tokens expected = Tokens.EQUALS;
            //Then
            assertEquals(expected, actual);
        }

    @Test
    void replaceStatement_replaces_everything(){
        assertAll(
            () -> assertEquals("*&^", lexer.replaceStatement("*&^")),
            () -> assertEquals("string32&(3&*_", lexer.replaceStatement("string32&(3&*_")),
            () -> assertEquals("123#12", lexer.replaceStatement("123#12")),
            () -> assertEquals("1>0", lexer.replaceStatement("1>0"))
        );
    }
}
