package com.pl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.*;
import java.util.List;
import java.util.Scanner;
import com.pl.Nodes.Node;
import com.pl.Nodes.ProgramNode;


public class LanguageTests {
    Lexer lexer;
    List<Token> tokens;
    Parser parser;
    Node ast;
    String source;

    @BeforeEach
    void setup(){
        this.source = "";
    }
    @AfterEach
    void set_env_up(){
        
        
        try {
            lexer = new Lexer(this.source);
        tokens = lexer.scanTokens();
        System.out.println("lexer tokens: "+ tokens);
        parser = new Parser(tokens);
            ast = parser.parse();
            System.out.println("AST:" + ast +"\n");
        }catch(Exception e){
            System.out.println("Error in parser");
        }
    }

    @Test
    void main_program_with_testing_comments(){
        System.out.println("\n=== 1Gr1 ===\n");
        this.source = "VAR eurese AS INT\n" +
                "VAR MELVIN AS BOOL\n" +
                "START\n" +
                "*testin\n" +
                "*mel\n" +
                "STOP";
    }
    @Test
    void main_program_with_1Gr1(){
        System.out.println("\n=== 1Gr1 ===\n");
        this.source = "STOP";
    }
    @Test
    void main_program_with_1Gr2(){
        System.out.println("\n=== 1Gr2 ===\n");
        this.source = "START";
    }
    @Test
    void main_program_with_2Gr1(){
        System.out.println("\n=== 2Gr1 ===\n");
        this.source = "START STOP";
    }
    @Test
    void main_program_with_3Gr1(){
        System.out.println("\n=== 3Gr1 ===\n");
        this.source =   "VAR xyz AS INT\n"+
                        "START\n"+
                        "STOP";
    }
    @Test
    void main_program_with_3Gr2(){
        System.out.println("\n=== 3Gr2 ===\n");
        this.source = "VAR xyz AS FLOAT\n"+
                    "START\n"+
                    "STOP";
    }
    @Test
    void main_program_with_4Gr1Gr2(){
        System.out.println("\n=== 4Gr1Gr2 ===\n");
        this.source = "START\n"+
                        "STOP";
    }
    @Test
    void main_program_with_5Gr1(){
        System.out.println("\n=== 5Gr1 ===\n");
        this.source = "VAR WHILE AS INT\n"+
                    "START\n"+
                    "STOP";
    }
    @Test
    void main_program_with_5Gr2(){
        System.out.println("\n=== 5Gr2 ===\n");
        this.source = "VAR INPUT AS FLOAT\n"+
                    "START\n"+
                    "STOP";
    }
    @Test
    void main_program_with_6Gr1(){
        System.out.println("\n=== 6Gr1 ===\n");
        this.source = "VAR a AS INT\n"+
                        "VAR b AS INT\n"+
                    "START\n"+
                    "STOP";
    }
    @Test
    void main_program_with_7Gr1(){
        System.out.println("\n=== 7Gr1 ===\n");
        this.source = "VAR 1x AS BOOL\n"+
                        "START\n"+
                        "STOP";
    }
    


}
