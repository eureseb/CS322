package com.pl;

class Interpreter {
    
    public Interpreter() {
    }

    String interpret(String lines){
        int state = 0;
        String line[] = lines.split("\\s+");

        final int state_table[][] = { {0,1,3,3}, 
                                      {3,3,1,2},
                                      {3,3,3,3}};
        for (String string : line) {
            int input = 0;
            if(string.equals("VAR")){
                input = 0;
            }
            else if(string.equals("START")){
                input = 1;
            }
            else if(string.equals("STOP")){
                input = 3;
            }
            else input = 2;

            state = state_table[state][input];
            
            System.out.println(state);
            if(state == 3){
                System.out.println(string);
                break;
            }
        }
        return state == 2 ? "Code is complete" : "Code has errors";
    }
}
