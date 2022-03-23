//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.pl;

import com.pl.Nodes.*;
import com.pl.Statements.*;
import static com.pl.TokenType.*;
import java.util.List;


public class Parser {
    List<Token> tokens;
    int ctr;
    Token currToken, temp;
    public boolean hadError = false;

    public Parser(List<Token> tokens){
        this.tokens = tokens;
        ctr = -1;
        advance();
    }
   
    private Token advance(){
        this.ctr += 1;
        if(ctr < tokens.size()){
            this.currToken = this.tokens.get(ctr);
            return this.currToken;
        }else System.out.println("Can't advance further");
        
        return null;
    }

    Node parse(){
        //Node ast = expression();
        ProgramNode ast = program();
        return ast;
    }

    Node factor(){                             //for constants
        temp = currToken;
        Node nodeExpr;
        if(currToken.isPlusOrMinus()){            //for UNARY
            advance();
            if(currToken.isIntOrFloat()){
                return new UnaryNode(temp, factor());
            }
        }
        else if(currToken.isIntOrFloat()){
            advance();
            return new NumberNode(temp);
        }
        else if(currToken.getType().equals(PAREN_OPEN)){
            advance();
            nodeExpr = expression();
            if(currToken.getType().equals(PAREN_CLOSE)){
                advance();
                return nodeExpr;
            }
        }
        return null;
    }

    Node term(){

        Node left = this.factor(), right;
        Token operator;

        while(currToken.isMulOrDiv()){
            operator = currToken;
            advance();
            right = factor();
            left = new BinaryNode(left, operator,right);
        }
        return left;
    }

    Node expression(){
        Node left = this.term(), right;
        Token operator;

        while(currToken.isPlusOrMinus()){
            operator = currToken;
            advance();
            right = term();
            left = new BinaryNode(left, operator, right);
        }
        return left;
    }

    Statement declareStmt(){
        Token temp = currToken, operator;
        Node nodeExpr;

        if(currToken.getType().equals(IDENTIFIER)){

            advance();
            if(currToken.getType().equals(EQUALS)){
                operator = currToken;
                advance();
                nodeExpr = expression();
                //return new Node.AssignNode(temp, operator, nodeExpr);
                return new AssignStatement(temp, operator, nodeExpr);
            }
            else{
                System.out.println("Expected '=' after identifier");
                hadError = true;
            }
        }
        else if(currToken.getType().equals(KW_OUTPUT)){
            advance();
            if(currToken.getType().equals(COLON)){
                //operator = currToken;                 //COLON symbol would not be included in the AST
                advance();
                //should allow: expressions (literal, unary, binary) , string, concatenated string

                //TO DO!!!!! allow printing of values of an identifier

                if(currToken.getType().equals(STRING) ){
                    return new OutputStatement(temp, new StringNode(currToken));
                }
                else{                                                       //if not string, call expression to check and leave there to error
                    nodeExpr = expression();
                    return new OutputStatement(temp, nodeExpr);
                }
            }
            else{
                System.out.println("Missing ':' after OUTPUT keyword");
                hadError = true;
            }
        }/*
        else if(currToken.type.equals(Token.TokenType.INPUT)){             //for input

        }*/
        else{
            //BRUTE FORCE !!!!
            if(currToken.getType().equals(NEWLINE)){
                advance();
            }
            else{
                System.out.println(currToken+" "+currToken.getLine());
                hadError = true;
            }
        }
        return null;
    }


    Statement declareMultStmts(){
        Statement stmtHead = declareStmt();
        Statement curr = stmtHead;

        advance();

        while(!(currToken.isEofOrStop())){
            curr.setNext(declareStmt());
            curr = curr.getNext();
            if(!currToken.getType().equals(KW_STOP)){
                advance();
            }
        }
        return stmtHead;
    }

    VariableDeclarationNode declareVar(){
        Object var, datatype;
        Token identifier;

        if(isValidVarDeclaration()){
            var = currToken;
            identifier = advance();
            advance();

            TokenType currentDataType = advance().getType();

            if(isDataType(currentDataType)){
                datatype = currToken;
                return new VariableDeclarationNode(var, identifier, datatype);
            }
            else{
                System.out.println("Invalid data type.");
                hadError = true;
            }
        }
        else{
            System.out.println("Invalid syntax");
            hadError = true;
        }
        return null;
    }


    Node declareMultVars(){

        VariableDeclarationNode head = declareVar();
        VariableDeclarationNode curr= head;
        // assuming currently \n;
        advance();      
        // assuming currently VAR or START;
        while (currToken.getType().equals(KW_VAR)){
            curr.setNext(declareVar());
            curr = curr.getNext();
            advance();
            
        }
        return head;
    }


    ProgramNode program() {
        Node head_var = null;
        Object start = null, stop = null;
        Node head_statement = null;
        if(currToken.getType().equals(KW_VAR)){ // Checking if start of program has var declaration
            head_var = declareMultVars();
        }
        if(currToken.getType().equals(KW_START)){ //Continue with START keyword
             start = currToken; // KW_START
             advance(); // currTokenType = STOP
             //add error handling here that does not allow anything after start
            if(!(currToken.isEofOrStop())){
                head_statement = declareMultStmts();
            }

            if(currToken.getType().equals(KW_STOP)){
                stop = currToken;
            }
            else{
                if(hadError != true){
                    System.out.println("Missing 'STOP' statement.");
                }
                hadError = true;
            }
         }
         else{
            if(hadError != true){
                System.out.println("Missing 'START' statement.");
            }
             hadError=true;
         }
         ProgramNode progNode = new ProgramNode(head_var, start, head_statement, stop);
         ProgramNode pNode = new ProgramNode(head_var, start, head_statement, stop);
         return pNode;
    }

    private boolean isValidVarDeclaration(){
        boolean firstIsVar = currToken.getType().equals(KW_VAR);
        boolean secondIsIden = tokens.get(this.ctr+1).getType().equals(IDENTIFIER);
        boolean thirdIsAS = tokens.get(this.ctr+2).getType().equals(KW_AS);
        return firstIsVar && secondIsIden && thirdIsAS;
    }
    public List<Token> getTokens(){
        return this.tokens;
    }

}
