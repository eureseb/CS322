/*package com.pl;

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
    void interpret_var_while_start_stop_produces_error(){
        String program = "VAR WHILE AS INT\n" +
                         "START\n" +
                         "STOP ";
        Interpreter interpret = new Interpreter();
        String actual =  interpret.interpret(program);
        String expected = "WRONG CODE";
        assertEquals(expected, actual);   
    }

    @Test
    void interpret_var_float_start_stop_produces_error(){
        String program = "VAR FLOAT AS INT\n" +
                         "START\n" +
                         "STOP ";
        Interpreter interpret = new Interpreter();
        String actual =  interpret.interpret(program);
        String expected = "WRONG CODE";
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


    @Test
    void interpret_when_there_is_comment(){
        String program = "VAR\n"+
                "START\n" +
                "*xyz should have the value -60\n"+
                "*STOP\n"+
                "STOP";
        Interpreter interpret = new Interpreter();
        Comment comment = new Comment();

        String actual =  interpret.interpret(program);
        String output =  comment.Comment(program);

        String expected = "COMPLETE CODE";
        System.out.println(output);

        assertEquals(expected, actual);
    }

    // @Test
    // void interpret_all_test_case_pass(){
    //     assertAll(
    //         () -> assertEquals(expected,  interpret.interpret("STOP")),
    //         () -> assertEquals(expected,  interpret.interpret("START")),
    //         () -> assertEquals(expected,  interpret.interpret("START STOP")),
    //         () -> assertEquals(expected,  interpret.interpret("START\nSTOP"))
            
            
    //     );
    // }
}
*/