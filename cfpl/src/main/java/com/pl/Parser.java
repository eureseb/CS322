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
        Token toPrint = null;
        Token temp = currToken;
//        String concatStr = "";
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
                    //operator = currToken;                 //COLON symbol would not be included in the AST
                    advance();
                    //should allow: expressions (literal, unary, binary) , string, concatenated string

                    if(currToken.getType().equals(STRING) ){
                        outputStatement.setHeadConcat(new StringNode(currToken));
                        advance();
                    }
                    else{                                                       //if not string, call expression to check and leave there to error

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
            else if(currToken.getType().equals(KW_INPUT)){           //for input
                advance();
                if(currToken.getType().equals(COLON)){
                    advance();
    
                    if(currToken.getType().equals(IDENTIFIER) ){          // if string
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
                //BRUTE FORCE !!!!
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

            System.out.println("it returns null1");
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


        if(currToken.getType().equals(COMMENT)){
            advance();
        }

        while(currToken.getType().equals(NEWLINE)){
            advance();
            if(currToken.getType().equals(COMMENT)){
                advance();
                System.out.println("testing");
            }
        }

        if(currToken.getType().equals(KW_VAR)){
                head_var = declareMultVars();
        }

        if(currToken.getType().equals(COMMENT)){
            advance();
        }

        while(currToken.getType().equals(NEWLINE)){
            advance();
            if(currToken.getType().equals(COMMENT)){
                advance();
                System.out.println("testing");
            }
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
                       if(currToken.getType().equals(COMMENT)){
                            advance();
                        }
                    }
                }
                
            }catch(RuntimeException e){
                errToken = new Token(TokenType.ERROR, errMsg, null, currToken.getLine());

                System.out.println("it returns null2");
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

                    System.out.println("it returns null3");
                    errToken.getLexeme();
                }
                hadError = true;

            }
         }
         else {
            if(hadError != true){
                errToken = new Token(TokenType.ERROR, errMsg, null, currToken.getLine());

                System.out.println("it returns null4");
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
