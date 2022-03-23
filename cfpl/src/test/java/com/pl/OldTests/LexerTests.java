package com.pl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.pl.TokenType.*;

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
       TokenType actual = lexer.checkToken(token);
       TokenType expected = STRING;
       //Then
       assertEquals(expected, actual);
   }

   @Test
   void checkToken_returns_KW_INT_token_when_given_INT(){
       //Given
       String token = "INT";
       //When
       TokenType actual = lexer.checkToken(token);
       TokenType expected = KW_INT;
       //Then
       assertEquals(expected, actual);
   }
   @Test
   void checkToken_returns_float_token_when_given_float(){
       //Given
       String token = "-3.0";
       //When
       TokenType actual = lexer.checkToken(token);
       TokenType expected = FLOAT;
       //Then
       assertEquals(expected, actual);
   }

   @Test
   void checkToken_returns_int_token_when_given_int(){
       //Given
       String token = "3";
       //When
       TokenType actual = lexer.checkToken(token);
       TokenType expected = INT;
       //Then
       assertEquals(expected, actual);
   }

   @Test
   void checkToken_returns_alpha_token_when_given_alpha(){

       //Given
       String token = "VAR";
       //When
       TokenType actual = lexer.checkToken(token);
       TokenType expected = KW_VAR;
       //Then
       assertEquals(expected, actual);
   }

   @Test
   void checkToken_returns_string_token_when_given_lower_var(){

       //Given
       String token = "var";
       //When
       TokenType actual = lexer.checkToken(token);
       TokenType expected = STRING;
       //Then
       assertEquals(expected, actual);
   }
   @Test
   void checkToken_returns_equals_token_when_given_equals(){
           //Given
           String token = "=";
           //When
       TokenType actual = lexer.checkToken(token);
       TokenType expected = EQUALS;
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

   @Test
   void parse_get_correct_tokens_and_strings_from_statement(){

       String statement = "VAR START num2 AS INT";

       ArrayList<TokenType> expectedTokens = new ArrayList<>();
       expectedTokens.add(KW_VAR);
       expectedTokens.add(KW_START);
       expectedTokens.add(STRING);
       expectedTokens.add(KW_AS);
       expectedTokens.add(KW_INT);

       ArrayList<String> expectedStrings = new ArrayList<>();
       expectedStrings.add("VAR");
       expectedStrings.add("START");
       expectedStrings.add("num2");
       expectedStrings.add("AS");
       expectedStrings.add("INT");

       Pair<ArrayList<TokenType>, ArrayList<String>> actual_tokens_strings =
                                               lexer.getTokens(statement);

       assertAll(
           () -> assertEquals(expectedTokens, actual_tokens_strings.getFirst()),
           () -> assertEquals(expectedStrings, actual_tokens_strings.getSecond())

       );
   }
}
