// package com.pl;

// import java.util.ArrayList;
// import java.util.List;

// import static com.pl.TokenType.*;
// class Parser {
//     private final List<Token> tokenList;
//     private int tokenIndex = 0;


//     public Parser(List<Token> tokenList){
//         this.tokenList = tokenList;
//     }

//     private Token getCurrentToken(){
//         Token currentToken = null;
//         if(tokenIndex < tokenList.size()){
//             currentToken = tokenList.get(tokenIndex);
//         }
//         return currentToken;
//     }
//     private TokenType getCurrentTokenType(){
//         return getCurrentToken().getType();
//     }
//     Token advanceToken(){
//         Token currentToken = null;
//         tokenIndex++;
//         if (tokenIndex < tokenList.size()){
//             currentToken = getCurrentToken();
//         }
//         return currentToken;
//     }

//     TokenType assignment(ArrayList<TokenType> tokenList){
//         final int state_table[][] = { {1,4,4,4},
//                                       {4,2,4,4},
//                                       {3,4,3,4},
//                                       {4,4,4,2},
//                                       {4,4,4,4}};
//         int state = 0;
//         int input = 0;

//         for(TokenType token: tokenList){
//             if(token == IDENTIFIER){
//                 input = 0;
//             }
//             else if (token == EQUALS) {
//                 input = 1;
//             }
//             else if (token == STRING) {
//                 input = 2;
//             }
//             else if (token == CHAR) {
//                 input = 2;
//             }
//             else if (token == INT) {
//                 input = 2;
//             }
//             else if (token == FLOAT) {
//                 input = 2;
//             }
//             else if (token == BOOL_FALSE) {
//                 input = 2;
//             }
//             else if (token == BOOL_TRUE) {
//                 input = 2;
//             }
//             else if (token == MULTIPLY) {
//                 input = 3;
//             }
//             else if (token == DIVIDE) {
//                 input = 3;
//             }
//             else if (token == PLUS) {
//                 input = 3;
//             }
//             else if (token == MINUS) {
//                 input = 3;
//             }
//             else if (token == MODULO) {
//                 input = 3;
//             }

//             state = state_table[state][input];
//             if(state == 4){
//                 break;
//             }

//         }
//         return state == 3 ? ST_ASSIGNMENT : ERROR;
//     }

// }
