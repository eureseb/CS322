package com.pl;

import java.io.*;
import java.util.List;
import java.util.Scanner;
import com.pl.Nodes.Node;

public class App 
{
    public static void main(String[] args) throws FileNotFoundException {
        Scanner inputSc = new Scanner(System.in);
        String input = "";

        Lexer lexer;
        Parser parser;
        Interpreter interpreter;

        Node ast;
        List<Token> tokens;

        //please change depending on environment
        File file = new File("D:\\GitHubRepo\\CS322\\CFPL_TEST.txt");

        Scanner sc = new Scanner(file);

        while(sc.hasNextLine()){
            input += sc.nextLine()+( sc.hasNextLine() ? 
                                                        "\n" :
                                                        ""); // FOR EOF;
        }

        //create lexer
        lexer = new Lexer(input);
        tokens = lexer.scanTokens();
        System.out.println(tokens);
        if(lexer.hadError == true){
            System.exit(65);
        }

        System.out.println("\n===== Parsing =====\n");

        //create parser
        parser = new Parser(tokens);
        ast = parser.parse();
        System.out.println(ast);
        if(parser.hadError == true)
            System.exit(65);

        System.out.println("===== Interpreting =====");
        //create interpreter
        interpreter = new Interpreter();
        interpreter.visit(ast);

        /*while(true){
            System.out.print("cfpl> ");
            //input = scn.nextLine();

            //create lexer
            lxr = new Lexer(input);
            tokens = lxr.make_tokens();
            System.out.println(tokens);
            if(lxr.hadError == true){
                System.exit(65);
            }

            System.out.println("=================");

            //create parser
            parser = new Parser(tokens);
            ast = parser.parse();
            System.out.println(ast);
            if(parser.hadError == true){
                System.exit(65);
            }


            //create interpreter
            /*interpreter = new Interpreter();
            interpreter.visit(ast);*/
        //}
    }
}
