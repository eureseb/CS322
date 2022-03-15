package com.pl;

public class Interpreter {
    
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

// def interpret(lines):

//     state = 0

//     state_table = [
//         (0, 1, 3, 3),
//         (3, 3, 1, 2),
//         (3, 3, 3, 3)
//     ]

//     for line in lines:
//         if not line:
//             continue
//         if re.match('\AVAR', line):
//             input = 0
//         elif line == 'START':
//             input = 1
//         elif line == 'STOP':
//             input = 3
//         else:               # line is a statement
//             input = 2

//         state = state_table[state][input]
//         if state == 3:
//             break

//     return 'Code is complete' if state == 2 else 'Code has errors'