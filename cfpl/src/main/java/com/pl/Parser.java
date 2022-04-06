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

    Node factor(){
        temp = currToken;
        Node nodeExpr;
        if(currToken.isPlusOrMinus()){
            advance();
            if(currToken.isIntOrFloat()){
                return new UnaryNode(temp, factor());
            }
        }
        else if(currToken.isIntOrFloat()){
            advance();
            return new NumberNode(temp);
        }
        else if(currToken.getType().equals(IDENTIFIER)){
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

    public Node concatenator(Node curr, Node nextNode){
        curr.next = nextNode;
        curr = nextNode;

        return curr;
    }


    Statement declareStmt(){
        Token iden, operator;
        Token temp = currToken;
        String errMsg = "";
        Node nodeExpr;

        try{
            if(currToken.getType().equals(IDENTIFIER)){

                advance();
                if(currToken.getType().equals(EQUALS)){
                    operator = currToken;
                    advance();
                    nodeExpr = expression();

                    if(currToken.getType() == NEWLINE){
                        return new AssignStatement(temp, operator, nodeExpr);
                    }
                    else {
                        System.out.println("expected newline, but got a something else needs implementation");
                    }
                }
                else{
                    throw new IllegalStatementException("Expected '=' after identifier at line" + currToken.getLine());
                }
            }
            else if(currToken.getType().equals(KW_OUTPUT)){
                advance();
                OutputStatement outputStatement = new OutputStatement(temp);
                Node currNode;
                if(currToken.getType().equals(COLON)){
                    advance();

                    if(currToken.getType().equals(STRING) ){
                        outputStatement.setHeadConcat(new StringNode(currToken));
                        advance();
                    }
                    else{
                        nodeExpr = expression();
                        outputStatement.setHeadConcat(nodeExpr);
                    }
                    currNode = outputStatement.getHeadConcat();

                    while (currToken.getType().equals(AND)){
                        advance();
                        if(currToken.getType().equals(STRING)){
                            currNode = concatenator(currNode, new StringNode(currToken));
                            advance();
                        }
                        else{
                            currNode = concatenator(currNode, expression());
                        }
                    }
                    return outputStatement;
                }

                else{
                    throw new IllegalStatementException("Missing ':' after OUTPUT keyword");

                }
            }
            else if(currToken.getType().equals(KW_INPUT)){

                advance();

                if(currToken.getType().equals(COLON)){
                    advance();

                    if(currToken.getType().equals(IDENTIFIER) ){
                        iden = currToken;
                        advance();
                        return new InputStatement(temp, iden);
                    }
                    else{
                        throw new IllegalStatementException("Expecting identifier at line "+currToken.getLine());
                    }
                }
                else{
                    throw new IllegalStatementException("Missing ':' after INPUT keyword at line "+currToken.getLine());
                }
            }
            else if(currToken.getType().equals(COMMENT)) {

            }
            else{
                if(currToken.getType().equals(NEWLINE)){
                    advance();
                }
                else{
                    throw new IllegalStatementException("Error: Current token: " + currToken + " at line " + currToken.getLine());
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }


    Statement declareMultStmts(){
        Statement stmtHead = declareStmt();
        Statement curr = stmtHead;

        advance();

        while(currToken.getType().equals(NEWLINE)){
            advance();
        }
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
        Token var, datatype, identifier;

        if(isValidVarDeclaration()){
            var = currToken;
            identifier = advance();
            advance();
            TokenType currentDataType = advance().getType();
            if(isDataType(currentDataType)){
                datatype = currToken;
                advance();
                return new VariableDeclarationNode(var, identifier, datatype);
            }
            else{
                System.out.println("Invalid data type: incorrect data type");
                hadError = true;
            }
        }
        else{
            Token iden = tokens.get(ctr+1);
            Token dt = tokens.get(ctr+4);

            System.out.println("it returns null1");
            System.out.println("Invalid variable declaration syntax: Got "+ iden.getLexeme() + " as identifier," + "Got " + dt.getType() + " as data type");
            hadError = true;
        }
        return null;
    }


    Node declareMultVars(){
        VariableDeclarationNode head = declareVar();
        VariableDeclarationNode curr= head;
        while(currToken.getType().equals(NEWLINE)){
            advance();
        }
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


        if(currToken.getType().equals(COMMENT)){
            advance();
        }

        if(currToken.getType().equals(NEWLINE)) {//
            advance();
        }

        if(currToken.getType().equals(KW_VAR)){
                head_var = declareMultVars();
        }

        if(currToken.getType().equals(COMMENT)){
            advance();
        }

        while(currToken.getType().equals(NEWLINE)){
            advance();
        }

        if(currToken.getType().equals(KW_START)){
            start = currToken;

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

                System.out.println("it returns null2");
                System.out.println(errToken.getLexeme());
                goToEof();

            }

            if (!(currToken.isEofOrStop())) {
                head_statement = declareMultStmts();
            }

            if (currToken.getType().equals(KW_STOP)) {
                stop = currToken;
                advance();
            }
            else{
                if(!hadError){
                    errMsg = "Enexpected error at " + currToken.getLine() + " with token " + currToken.getLexeme();
                }

            }
         }
         else {
            if(!hadError){
                errMsg = "Syntax Error: No START found";
                hadError = true;
            }
         }

         while(currToken.getType() == NEWLINE){
             advance();
         }

         if(!hadError && currToken.getType() != EOF){
             errMsg = "Found " + currToken.getType() + " token after STOP. Check syntax";
             hadError = true;
         }

         if (hadError) {
             errToken = new Token(TokenType.ERROR, errMsg, null, currToken.getLine());
             System.out.println(errMsg);
             return null;
         }
         else {
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
