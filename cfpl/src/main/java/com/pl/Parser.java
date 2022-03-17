package com.pl;

import java.util.ArrayList;

import static com.pl.Tokens.*;
class Parser {
    

    Tokens assignment(ArrayList<Tokens> tokenList){
        final int state_table[][] = { {1,4,4,4}, 
                                      {4,2,4,4},
                                      {3,4,3,4},
                                      {4,4,4,2},
                                      {4,4,4,4}};
        int state = 0;
        int input = 0;

        for(Tokens token: tokenList){
            if(token == IDENTIFIER){
                input = 0;
            }
            else if (token == EQUALS) {
                input = 1;
            }
            else if (token == STRING) {
                input = 2;
            }
            else if (token == CHAR) {
                input = 2;
            }
            else if (token == INT) {
                input = 2;
            }
            else if (token == FLOAT) {
                input = 2;
            }
            else if (token == BOOL_FALSE) {
                input = 2;
            }
            else if (token == BOOL_TRUE) {
                input = 2;
            }
            else if (token == MULTIPLY) {
                input = 3;                
            }
            else if (token == DIVIDE) {
                input = 3;
            }
            else if (token == PLUS) {
                input = 3;
            }
            else if (token == MINUS) {
                input = 3;
            }
            else if (token == MODULO) {
                input = 3;
            }

            state = state_table[state][input];
            if(state == 4){
                break;
            }

        }
        return state == 3 ? ST_ASSIGNMENT : ERROR;
    }
    /*
    def assignment(self,token_stream):
        stateTable = [[1,4,4,4],
                      [4,2,4,4],
                      [3,4,3,4],
                      [4,4,4,2],
                      [4,4,4,4]]
        state = 0
        infut = 0

        for token in token_stream:
            if token[0] == Tokens.IDENTIFIER:
                infut = 0
            elif token[0] == Tokens.EQUALS:
                infut = 1
            elif token[0] == Tokens.STRING:
                infut = 2
            elif token[0] == Tokens.CHAR:
                infut = 2
            elif token[0] == Tokens.INT:
                infut = 2
            elif token[0] == Tokens.FLOAT:
                infut = 2
            elif token[0] == Tokens.BOOL_FALSE:
                infut = 2
            elif token[0] == Tokens.BOOL_TRUE:
                infut = 2
            elif token[0] == Tokens.MULTIPLY:
                infut = 3
            elif token[0] == Tokens.DIVIDE:
                infut = 3
            elif token[0] == Tokens.PLUS:
                infut = 3
            elif token[0] == Tokens.MINUS:
                infut = 3
            elif token[0] == Tokens.MODULO:
                infut = 3

            state = stateTable[state][infut]
            if state == 4:
                break
                */
}
