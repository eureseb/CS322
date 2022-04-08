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
//        File file = new File("C:\\Users\\PC ADMIN\\IdeaProjects\\CS322\\CFPL_TEST_GROUP3.txt"); BULAN FILE DIR
        try(Scanner fileScanner = new Scanner(file)){
            while(fileScanner.hasNextLine()){
                source.append(fileScanner.nextLine()).append(fileScanner.hasNextLine() ?
                        "\n" :// for NEXTLINE
                        ""); // FOR EOF;
            }
        }

        try {
            //System.out.println("\n===== Lexing =====\n");

            Lexer lexer = new Lexer(source.toString());
            if(lexer.hadError){
                System.out.println("error in lexer");
                System.exit(404);
            }
            List<Token> tokens = lexer.scanTokens();
//          System.out.println("lexer tokens: "+ tokens);

            // System.out.println("\n===== Parsing =====\n");
            Parser parser = new Parser(tokens);
            Node ast = parser.parse();
            if(parser.hadError){
                System.out.println("error in parser");
                System.exit(504);
            }
//          System.out.println(ast);

            // System.out.println("\n===== Interpreting =====\n");
            Interpreter interpreter = new Interpreter();
            interpreter.visit(ast);

        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println("Error in parser or interpreter");
        }
    }
}
