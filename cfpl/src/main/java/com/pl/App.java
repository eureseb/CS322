package com.pl;

import java.io.*;
import java.util.List;
import java.util.Scanner;
import com.pl.Nodes.Node;

public class App 
{
    public static void main(String[] args) throws FileNotFoundException {
        
        StringBuilder source = new StringBuilder();

        //please change depending on environment
        File file = new File("D:\\GitHubRepo\\CS322\\CFPL_TEST_GROUP3.txt");
        
        try(Scanner fileScanner = new Scanner(file)){
            while(fileScanner.hasNextLine()){
                source.append(fileScanner.nextLine()).append(fileScanner.hasNextLine() ?
                        "\n" :// for NEXTLINE
                        ""); // FOR EOF;
            }
        }

        //System.out.println("\n===== Lexing =====\n");
        Lexer lexer = new Lexer(source.toString());
        List<Token> tokens = lexer.scanTokens();
        Node ast;
        Interpreter interpreter;
//        System.out.println(tokens);
        if(lexer.hadError){
            System.out.println("error in lexer");
            System.exit(404);
        }

       // System.out.println("\n===== Parsing =====\n");
        Parser parser;
        try {
            lexer = new Lexer(source.toString());
            tokens = lexer.scanTokens();
//             System.out.println("lexer tokens: "+ tokens);
            parser = new Parser(tokens);
            ast = parser.parse();
//
//            System.out.println("ast");
//            System.out.println(ast);

            interpreter = new Interpreter();
//             System.out.println("AST:" + ast +"\n");
            interpreter.visit(ast);
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println("Error in parser");
        }



       // System.out.println("\n===== Interpreting =====\n");
        
    
    }
}
