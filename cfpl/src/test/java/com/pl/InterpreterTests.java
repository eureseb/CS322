package com.pl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class InterpreterTests {
    
    @Test
    void interpret_code_is_complete_check(){
        
        // cant put 'VAR num1 as INT' because we haven't implemented the DFA logic
        String program = "VAR " + 
                        "START " +
                        "INPUT: num1, num2 " +
                        "WHILE(num1 < num2) " +
                        "num1 = num1 + 1 " +
                        "OUTPUT: 'HEY' & '#' " +
                        "STOP";
        Interpreter interpret = new Interpreter();
        String actual =  interpret.interpret(program);
        String expected = "COMPLETE CODE";
        assertEquals(expected, actual);   
    }

    @Test
    void interpret_code_is_complete_start_stop(){
        String program = "START\n" +
                        "STOP ";
        Interpreter interpret = new Interpreter();
        String actual =  interpret.interpret(program);
        String expected = "COMPLETE CODE";
        assertEquals(expected, actual);   
    }
    @Test
    void interpret_error_when_wrong_syntax_check(){
        String program = "START\n" +
                         "VAR\n" + 
                         "STOP";
        Interpreter interpret = new Interpreter();
        String actual =  interpret.interpret(program);
        String expected = "WRONG CODE";
        assertEquals(expected, actual);   
    }

    @Test
    void interpret_error_when_no_stop(){
        String program = "VAR num1 as INT"+
                         "START" +
                         "INPUT: num1" +
                         "OUTPUT: 'EURESE AKO'";
        Interpreter interpret = new Interpreter();
        String actual =  interpret.interpret(program);
        String expected = "WRONG CODE";
        assertEquals(expected, actual);   
    }
}
