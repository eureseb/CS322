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
            System.out.println(input);
            
            
            if(state == 3){
                break;
            }
        }
        if(state==2){
            System.out.println("COMPLETE CODE");
        }
        else System.out.println("WRONG CODE");
        return state == 2 ? "COMPLETE CODE" : "WRONG CODE";
    }
}
