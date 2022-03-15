package com.pl;

public class App 
{
    public static void main( String[] args )
    {
        String sample = "VAR START num1, num2 AS INT" +
                        // "START " +
                        "INPUT: num1, num2 " +
                        "WHILE(num1 < num2) " +
                        // "START " +
                        "num1 = num1 + 1 " +
                        "OUTPUT: 'HEY' & '#' " +
                        "STOP ";// +
                        // "STOP";


  
        Interpreter interpret = new Interpreter();
        String string =  interpret.interpret(sample);
        System.out.println(string);
    }
}
