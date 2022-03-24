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
        File file = new File("C:\\Users\\PC ADMIN\\IdeaProjects\\CS322Temp\\CFPL_TEST_GROUP3.txt");
        
        try(Scanner fileScanner = new Scanner(file)){
            while(fileScanner.hasNextLine()){
                source += fileScanner.nextLine()+( fileScanner.hasNextLine() ? 
                                                            "\n" :// for NEXTLINE
                                                            ""); // FOR EOF;
            }
        }
        
        System.out.println("\n===== Lexing =====\n");
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();
        System.out.println(tokens);
        if(lexer.hadError == true){
            System.exit(500);
        }

        System.out.println("\n===== Parsing =====\n");
        Parser parser = new Parser(tokens);
        System.out.println(parser.getTokens());
        Node ast = parser.parse();
        if(parser.hadError == true)
            System.exit(400);

        System.out.println("\n===== Interpreting =====\n");
        Interpreter interpreter = new Interpreter();
        interpreter.visit(ast);

    
    }
}
