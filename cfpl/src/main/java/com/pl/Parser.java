//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.pl;

import com.pl.Nodes.*;
import com.pl.Statements.*;
import static com.pl.TokenType.*;
import java.util.List;

import javax.xml.transform.Source;


public class Parser {
    List<Token> tokens;
    int ctr;
    Token currToken, temp;
    public boolean hadError = false;
    Token errToken;

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
        Object var, datatype, as;
        Token identifier;

        if(isValidVarDeclaration()){
            var = currToken;
            identifier = advance();
            advance(); // assuming AS
            TokenType currentDataType = advance().getType();
            if(isDataType(currentDataType)){
                datatype = currToken;
                advance(); // currToken = dataType -> '\n'
                return new VariableDeclarationNode(var, identifier, datatype);
            }
            else{
                System.out.println("Invalid data type: incorrect data type");
                hadError = true;
            }
        }
        else{
            Token iden = tokens.get(ctr+1);// provided iden
            Token dt = tokens.get(ctr+4); // provided data type
            System.out.println("Invalid variable declaration syntax: Got "+ iden.getLexeme() + " as identifier," + "Got " + dt.getType() + " as data type");
            hadError = true;
        }
        return null;
    }


    Node declareMultVars(){

        VariableDeclarationNode head = declareVar();
        VariableDeclarationNode curr= head;
        // assuming currently \n;
        while(currToken.getType().equals(NEWLINE)){
            advance();
        }     
        // assuming currently VAR or START;
        while (currToken.getType().equals(KW_VAR)){
                curr.setNext(declareVar());
                curr = curr.getNext();
                advance();
            
        }
        return head;
    }
    
    void goToEof(){
        while(!currToken.getType().equals(EOF)){
            advance();
        }
    }
    TokenType peekNextTokenType(){
        return tokens.get(ctr+1).getType();
    }

    ProgramNode program() {
        Node head_var = null;
        Object start = null, stop = null;
        Node head_statement = null;
        String errMsg = "";

        if(currToken.getType().equals(KW_VAR)){
                head_var = declareMultVars();
        }
        while(currToken.getType().equals(NEWLINE)){
            advance();
        }
        if(currToken.getType().equals(KW_START)){ //Continue with START keyword
             start = currToken; // KW_START
            try{
                if(peekNextTokenType().equals(EOF)){
                    errMsg = "No STOP found";
                    throw new RuntimeException();
                }
                else if(peekNextTokenType().equals(KW_STOP)){
                    errMsg = "START and STOP cannot be 1 line";
                    throw new RuntimeException();
                }else if(!peekNextTokenType().equals(NEWLINE)){
                    errMsg = peekNextTokenType()+" must be in a newline";
                    throw new RuntimeException();
                }else if(peekNextTokenType().equals(NEWLINE)){
                    advance();
                    while(currToken.getType().equals(NEWLINE)){
                        advance();
                    }
                }
                
            }catch(RuntimeException e){
                errToken = new Token(TokenType.ERROR, errMsg, null, currToken.getLine());
                System.out.println(errToken.getLexeme());
                goToEof();

            }
             // currTokenType = STOP
             //add error handling here that does not allow anything after start
            if(!(currToken.isEofOrStop())){
                
                head_statement = declareMultStmts();
            }

            if(currToken.getType().equals(KW_STOP)){
                stop = currToken;
            }
            else{
                if(hadError != true){
                    errToken.getLexeme();
                }
                hadError = true;
            }
         }
         else {
            if(hadError != true){
                errToken = new Token(TokenType.ERROR, errMsg, null, currToken.getLine());
                System.out.println(errToken.getLexeme());
                System.out.println("No START found");
            }
             hadError=true;
         }

         if(hadError){
             return null;
         }else{
             System.out.println("\n== Program Complete. No Errors ==\n");
             return new ProgramNode(head_var, start, head_statement, stop);
         }
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
