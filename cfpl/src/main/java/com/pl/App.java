package com.pl;

import java.io.*;
import java.util.List;
import java.util.Scanner;
import com.pl.Nodes.Node;

public class App 
{
    public static void main(String[] args) throws FileNotFoundException {
        
        String source = "";    

        //please change depending on environment
        File file = new File("D:\\GitHubRepo\\CS322\\CFPL_TEST_GROUP3.txt");
        
        try(Scanner fileScanner = new Scanner(file)){
            while(fileScanner.hasNextLine()){
                source += fileScanner.nextLine()+( fileScanner.hasNextLine() ? 
                                                            "\n" :// for NEXTLINE
                                                            ""); // FOR EOF;
            }
        }

        //System.out.println("\n===== Lexing =====\n");
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();
        Node ast;
        Interpreter interpreter;
//        System.out.println(tokens);
        if(lexer.hadError == true){
            System.exit(500);
        }

       // System.out.println("\n===== Parsing =====\n");
        Parser parser = new Parser(tokens);
       // System.out.println(parser.getTokens());
        try {
            lexer = new Lexer(source);
            tokens = lexer.scanTokens();
//             System.out.println("lexer tokens: "+ tokens);
            parser = new Parser(tokens);
            ast = parser.parse();
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
