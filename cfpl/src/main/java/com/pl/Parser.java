package com.pl;

import com.pl.Nodes.*;
import com.pl.Statements.*;
import static com.pl.TokenType.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Currency;

public class Parser {
    List<Token> tokens;
    int ctr;
    Token currToken, temp;
    public boolean hadError = false;
    Token errToken;
    List<Token> stmt;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        ctr = -1;
        advance();
    }

    private void advance() {
        this.ctr += 1;
        if (ctr < tokens.size()) {
            this.currToken = this.tokens.get(ctr);
        } else
            System.out.println("Can't advance further");
    }

    Node parse() {
        return program();
    }

    Node factor() {
        temp = currToken;
        Node nodeExpr;
        if (currToken.isPlusOrMinus()) {
            advance();
            if (currToken.isIntOrFloat()) {
                return new UnaryNode(temp, (NumberNode) factor());
            }
        } else if (currToken.isIntOrFloat()) {
            advance();
            return new NumberNode(temp);
        } else if (currToken.getType().equals(IDENTIFIER)) {
            advance();
            return new NumberNode(temp);
        } else if (currToken.getType().equals(PAREN_OPEN)) {
            advance();
            nodeExpr = expression();
            if (currToken.getType().equals(PAREN_CLOSE)) {
                advance();
                return nodeExpr;
            }
        }
        return null;
    }

    Node term() {

        Node left = this.factor(), right;
        Token operator;

        while (currToken.isMulOrDiv()) {

            operator = currToken;
            advance();
            right = factor();
            left = new BinaryNode(left, operator, right);
        }
        return left;
    }

    Node expression() {
        Node left = this.term(), right;
        Token operator;
        while (currToken.isPlusOrMinus()) {
            operator = currToken;
            advance();
            right = term();
            left = new BinaryNode(left, operator, right);
        }
        return left;
    }

    public Node concatNode(Node curr, Node nextNode) {
        curr.setNext(nextNode);
        curr = nextNode;
        return curr;
    }

    Statement declareStmt() {
        Token identifier, operator, var1 = null, var2, logic;
        Token v1, v2, lg2;
        Token temp = currToken;
        Node nodeExpr;

        try {
            if (currToken.getType().equals(KW_VAR)) {
                throw new IllegalStatementException(
                        "Syntax Error: Can't declare variable after START at line " + currToken.getLine());
            } else if (currToken.getType().equals(IDENTIFIER)) {

                advance();
                if (currToken.getType().equals(EQUALS)) {
                    operator = currToken;
                    advance();
                    nodeExpr = expression();

                    if (currToken.getType() == NEWLINE) {
                        return new AssignStatement(temp, operator, nodeExpr);
                    } else {
                        System.out.println("expected newline, but got a something else needs implementation");
                    }
                } else {
                    throw new IllegalStatementException("Expected '=' after identifier at line" + currToken.getLine());
                }
            } else if (currToken.getType().equals(KW_OUTPUT)) {
                advance();
                OutputStatement outputStatement = new OutputStatement(temp);
                Node currNode;
                if (currToken.getType().equals(COLON)) {
                    advance();
                    if (currToken.getType().equals(PAREN_OPEN)) {
                        advance();
                        if (currToken.getType().equals(IDENTIFIER) || currToken.isDataType()) {
                            var1 = currToken;
                            advance();
                            if (isCondition(currToken.getType())) {
                                logic = currToken;
                                advance();
                                if (currToken.getType().equals(IDENTIFIER) || currToken.isDataType()) {
                                    var2 = currToken;
                                    advance();
                                    if (currToken.getType().equals(AND) || currToken.getType().equals(OR)
                                            || currToken.getType().equals(NOT)) {
                                        OutputStatement ot = new OutputStatement(temp);
                                        ot.setSpecialLogic(currToken);
                                        advance();
                                        if (currToken.getType().equals(IDENTIFIER) || currToken.isDataType()) {
                                            v1 = currToken;
                                            advance();
                                            if (isCondition(currToken.getType())) {
                                                lg2 = currToken;
                                                advance();
                                                if (currToken.getType().equals(IDENTIFIER) || currToken.isDataType()) {
                                                    v2 = currToken;
                                                    advance();
                                                    if (currToken.getType().equals(PAREN_CLOSE)) {
                                                        ConditionStatement cd2 = new ConditionStatement(v1, lg2, v2);
                                                        ConditionStatement cd1 = new ConditionStatement(var1, logic,
                                                                var2);
                                                        ot.setCondition(cd1);
                                                        ot.setCondition2(cd2);
                                                        ot.setFlag(false);
                                                        return ot;
                                                    } else {
                                                        throw new IllegalStatementException("Missing ')'");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (currToken.getType().equals(PAREN_CLOSE)) {
                                        ConditionStatement cd = new ConditionStatement(var1, logic, var2);
                                        OutputStatement ot = new OutputStatement(temp);
                                        ot.setCondition(cd);
                                        return ot;
                                    } else {
                                        throw new IllegalStatementException("Missing ')'");
                                    }
                                }

                            }
                        }

                    }
                    if (currToken.getType().equals(STRING)) {
                        outputStatement.setHeadConcat(new StringNode(currToken));
                        advance();
                    } else {
                        nodeExpr = expression();
                        outputStatement.setHeadConcat(nodeExpr);
                    }
                    currNode = outputStatement.getHeadConcat();

                    while (currToken.getType().equals(AND)) {

                        advance();
                        if (currToken.getType().equals(STRING)) {
                            currNode = concatNode(currNode, new StringNode(currToken));
                            advance();
                        } else {
                            currNode = concatNode(currNode, expression());
                        }
                    }

                    return outputStatement;
                }

                else {
                    throw new IllegalStatementException("Missing ':' after OUTPUT keyword");

                }
            } else if (currToken.getType().equals(KW_INPUT)) {
                InputStatement topInputNode = null;
                InputStatement currInputNode = null;
                InputStatement inputStatement = null;
                advance();

                if (currToken.getType().equals(COLON)) {
                    advance();

                    while (currToken.getType().equals(IDENTIFIER)) {
                        inputStatement = new InputStatement(currToken);
                        if (topInputNode == null) {
                            topInputNode = currInputNode = inputStatement;
                        } else {
                            currInputNode.setNext(inputStatement);
                            currInputNode = (InputStatement) currInputNode.getNext();
                        }
                        advance();
                        if (currToken.getType().equals(COMMA)) {
                            advance();
                        } else if (currToken.getType().equals(NEWLINE)) {

                        } else {
                            throw new IllegalStatementException(
                                    "Syntax Error: Invalid INPUT syntax at line " + currToken.getLine());
                        }
                    }

                    if (topInputNode == null) {
                        throw new IllegalStatementException("Syntax Error: Expecting identifier " + currToken.getLine()
                                + " but got " + currToken.getType());
                    }

                    return topInputNode;
                } else {
                    throw new IllegalStatementException(
                            "Missing ':' after INPUT keyword at line " + currToken.getLine());
                }
            } else if (currToken.getType().equals(IF)) { // FLAG1
                IfELseStmt IFElse = new IfELseStmt();
                if (currToken.getType().equals(IF)) {
                    advance();
                    ConditionStatement IE = new ConditionStatement();
                    if (currToken.getType().equals(PAREN_OPEN)) {
                        System.out.println("A1" + currToken.getType());
                        advance();
                        if (currToken.getType().equals(IDENTIFIER)) {
                            IE.setVar1(currToken);
                            System.out.println("A2" + currToken.getType());
                            advance();
                            if (currToken.getType().equals(GREATER_THAN) ||
                                    currToken.getType().equals(GREATER_OR_EQUAL)
                                    || currToken.getType().equals(LESS_THAN) ||
                                    currToken.getType().equals(LESS_OR_EQUAL)
                                    || currToken.getType().equals(EQUALS) ||
                                    currToken.getType().equals(NOT_EQUAL)
                                    || currToken.getType().equals(NOT) || currToken.getType().equals(AND)
                                    || currToken.getType().equals(OR)) {
                                System.out.println("A3" + currToken.getType());
                                IE.setLogic(currToken);
                                advance();
                                if (currToken.getType().equals(IDENTIFIER)) {
                                    System.out.println("A4" + currToken.getType());
                                    IE.setVar2(currToken);

                                    advance();
                                    if (currToken.getType().equals(PAREN_CLOSE)) {
                                        advance();
                                        advance();
                                        System.out.println(peekNextTokenType().toString());
                                        if (currToken.getType().equals(KW_START)) {
                                            // stmt.add(currToken);
                                            // System.out.println("A5" + currToken.getType());
                                            // advance();

                                            // while (!currToken.getType().equals(KW_STOP)) {
                                            // stmt.add(currToken);
                                            // advance();
                                            // if (currToken.getType().equals(KW_STOP)) {
                                            // stmt.add(currToken);
                                            // IFElse = new IfELseStmt(IE, tokens);
                                            // return IFElse;
                                            // }
                                            // if (currToken.getType().equals(EOF)) {
                                            // throw new IllegalStatementException(
                                            // "Missing 'STOP' at line " + currToken.getLine());
                                            // }
                                            // }
                                        }

                                    }

                                } else {
                                    throw new IllegalStatementException(
                                            "Expected 'START' after ')' at line " + currToken.getLine());
                                }
                            } else {
                                throw new IllegalStatementException("Expected ')' at line " +
                                        currToken.getLine());
                            }
                        } else if (currToken.getType().equals(NEWLINE)) {
                            throw new IllegalStatementException("IF/ELSE Statement must be in one line");
                        }
                    } else {
                        throw new IllegalStatementException("Expected '(' after IF at line" +
                                currToken.getLine());
                    }
                }
            } else {
                if (currToken.getType().equals(NEWLINE) || currToken.getType().equals(COMMENT)) {
                    advance();
                } else {
                    throw new IllegalStatementException(
                            "Error: Current token: " + currToken + " at line " + currToken.getLine());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    Statement declareMultStmts() {
        Statement stmtHead = declareStmt();
        Statement curr = stmtHead;
        advance();

        while (curr.getNext() != null) {
            curr = curr.getNext();
        }
        while (currToken.getType().equals(NEWLINE)) {
            advance();
        }
        while (!(currToken.isEofOrStop())) {
            curr.setNext(declareStmt());
            curr = curr.getNext();
            if (!currToken.getType().equals(KW_STOP)) {
                advance();
            }

        }

        return stmtHead;
    }

    private boolean itHasAnotherVariableBeside() {
        return currToken.getType() == COMMA && peekNextTokenType().equals(IDENTIFIER);
    }

    VariableDeclarationNode declareVar() {

        VariableDeclarationNode topVarDeclr = null;
        VariableDeclarationNode currVarDeclr = null;
        VariableDeclarationNode node;
        Token identifier;
        Node factor;

        if (currToken.getType().equals(KW_VAR)) {
            advance();
            Map<Token, Object> variables = new HashMap<>();

            while (currToken.getType() == IDENTIFIER) {
                Object value = null;

                identifier = currToken;

                advance();
                switch (currToken.getType()) {
                    case COMMA:

                        variables.put(identifier, null);

                        advance();
                        break;

                    case EQUALS:
                        advance();
                        if (currToken.hasLiteral()) {

                            variables.put(identifier, currToken.getLiteral());
                            advance();

                            if (!(currToken.getType() == KW_AS || itHasAnotherVariableBeside())) {
                                System.out.println("Error at Token:" + currToken.toString());
                                System.out.println("Syntax error at token " + currToken.getType() + " at line "
                                        + currToken.getLine());
                                hadError = true;
                                return null;
                            }

                            advance();

                        } else if (currToken.getType().equals(STRING)) {
                            variables.put(identifier, currToken.getLiteral());
                            advance();

                            if (!(currToken.getType() == KW_AS || itHasAnotherVariableBeside())) {
                                System.out.println("Error at Token:" + currToken.toString());
                                System.out.println("Syntax error at token " + currToken.getType() + " at line "
                                        + currToken.getLine());
                                hadError = true;
                                return null;
                            }
                            advance();
                        } else {

                            factor = factor();

                            if (factor instanceof UnaryNode) {
                                UnaryNode currNode = (UnaryNode) factor;
                                NumberNode currNumNode = currNode.getNum();
                                TokenType numNodeType = currNumNode.getNum().getType();

                                value = (currNode.getOperator().getLexeme() + currNumNode.getNum().getLexeme());

                                if (numNodeType.equals(INT)) {
                                    value = Integer.parseInt(value.toString());
                                } else if (numNodeType.equals(FLOAT)) {
                                    value = Float.parseFloat(value.toString());
                                } else {
                                    System.out.println("Invalid value declaration at line " + currToken.getLine()
                                            + " for identifier " + identifier);
                                    hadError = true;
                                }

                            } else if (factor instanceof NumberNode) {
                                NumberNode currNode = (NumberNode) factor;
                                value = (currNode.getNum().getLiteral());
                            }

                            variables.put(identifier, value);

                            if (!(currToken.getType() == KW_AS || itHasAnotherVariableBeside())) {
                                System.out.println("Error at Token:" + currToken.toString());
                                System.out.println("Syntax error at token " + currToken.getType() + " at line "
                                        + currToken.getLine());
                                hadError = true;
                                return null;
                            }
                            advance();
                        }
                        break;

                    case KW_AS:
                        if (peekPrevTokenType().equals(IDENTIFIER)) {
                            variables.put(identifier, null);
                        }
                        advance();
                        break;

                    default:
                        System.out.println("Invalid variable declaration syntax at line " + currToken.getLine());
                        hadError = true;
                        return null;
                }
            }

            if (!currToken.isDataType()) {
                System.out.println("Invalid variable declaration syntax at line " + currToken.getLine());
                hadError = true;
                return null;
            }

            Token dataType = currToken;

            switch (dataType.getType()) {
                case KW_INT:
                    for (Map.Entry<Token, Object> var : variables.entrySet()) {
                        Object variableValue = var.getValue();

                        if (variableValue == null || variableValue instanceof Integer) {

                            node = new VariableDeclarationNode(var.getKey(), dataType, variableValue);
                            if (topVarDeclr == null) {
                                topVarDeclr = currVarDeclr = node;
                            } else {
                                currVarDeclr.setNext(node);
                                currVarDeclr = currVarDeclr.getNext();
                            }
                        } else {
                            System.out.println("Incompatible datatypes for identifier " + var.getKey() + " at line "
                                    + var.getKey().getLine() + ". Expected an INTEGER.");
                            hadError = true;
                        }
                    }
                    break;

                case KW_FLOAT: // int can be assigned as float
                    for (Map.Entry<Token, Object> var : variables.entrySet()) {
                        Object variableValue = var.getValue();

                        if (variableValue == null || variableValue instanceof Float) {

                            node = new VariableDeclarationNode(var.getKey(), dataType, variableValue);

                            if (topVarDeclr == null) {
                                topVarDeclr = currVarDeclr = node;
                            } else {
                                currVarDeclr.setNext(node);
                                currVarDeclr = currVarDeclr.getNext();
                            }

                        } else if (var.getValue() instanceof Integer) { // if value is an int, convert to float
                            node = new VariableDeclarationNode(var.getKey(), dataType,
                                    Float.parseFloat(var.getValue().toString()));

                            if (topVarDeclr == null) {
                                topVarDeclr = currVarDeclr = node;
                            } else {
                                currVarDeclr.setNext(node);
                                currVarDeclr = currVarDeclr.getNext();
                            }
                        } else {
                            System.out.println("Incompatible datatypes for identifier " + var.getKey() + " at line "
                                    + var.getKey().getLine() + ". Expected a FLOAT.");
                            hadError = true;
                        }
                    }
                    break;

                case KW_BOOLEAN:
                    for (Map.Entry<Token, Object> var : variables.entrySet()) {
                        Object variableValue = var.getValue();

                        if (var.getValue() == null || var.getValue() instanceof String
                                && (((String) var.getValue()).equalsIgnoreCase("true")
                                        || ((String) var.getValue()).equalsIgnoreCase("false"))) {
                            node = new VariableDeclarationNode(var.getKey(), dataType, variableValue);

                            if (topVarDeclr == null) {
                                topVarDeclr = currVarDeclr = node;
                            } else {
                                currVarDeclr.setNext(node);
                                currVarDeclr = currVarDeclr.getNext();
                            }
                        } else {
                            System.out.println();
                            System.out.println("Incompatible datatypes for identifier " + var.getKey() + " at line "
                                    + var.getKey().getLine() + ". Expected a BOOLEAN.");
                            hadError = true;
                        }
                    }
                    break;

                case KW_CHAR:
                    for (Map.Entry<Token, Object> var : variables.entrySet()) {
                        Object variableValue = var.getValue();
                        if (var.getValue() == null || var.getValue() instanceof Character) {

                            node = new VariableDeclarationNode(var.getKey(), dataType, variableValue);

                            if (topVarDeclr == null) {
                                topVarDeclr = currVarDeclr = node;
                            } else {
                                currVarDeclr.setNext(node);
                                currVarDeclr = currVarDeclr.getNext();
                            }
                        } else {
                            System.out.println("Incompatible datatypes for identifier " + var.getKey() + " at line "
                                    + var.getKey().getLine() + ". Expected a CHARACTER.");
                            hadError = true;
                        }
                    }
                    break;
            }

            return topVarDeclr;
        }
        return null;
    }

    Node declareMultVars() {
        VariableDeclarationNode head = declareVar();
        VariableDeclarationNode curr = head;

        advance();

        while (currToken.getType().equals(NEWLINE)) {
            advance();
        }
        while (currToken.getType().equals(KW_VAR)) {
            curr.setNext(declareVar());
            while (curr.getNext() != null) {
                curr = curr.getNext();
            }
            advance();
            advance();
        }

        return head;
    }

    void goToEof() {
        while (!currToken.getType().equals(EOF)) {
            advance();
        }
    }

    TokenType peekNextTokenType() {
        return tokens.get(ctr + 1).getType();
    }

    TokenType peekPrevTokenType() {
        return tokens.get(ctr - 1).getType();
    }

    private List<Token> getTokensUntilClose() {
        List<Token> result = new ArrayList<Token>();
        boolean foundClose = false;
        for (int i = this.ctr; i < this.tokens.size(); i++) {
            System.out.println("This is: " + this.tokens.get(i).toString());
            if (this.tokens.get(i).getType().equals(PAREN_CLOSE)) {
                foundClose = true;
                break;
            }
            result.add(this.tokens.get(i));
        }
        if (foundClose) {
            return result;
        } else {
            return new ArrayList<Token>();
        }

    }

    WhileNode looped() {
        Object whileStart = null, whileStop = null;
        Node whileHead_statement = null;
        String errMsg = "";

        System.out.println("Inside looped function, Current Token is:" + currToken.toString());

        advance(); // To check for Open Parenthesis
        if (!(currToken.getType().equals(PAREN_OPEN))) {
            errMsg = "No Open Parenthesis Found";
            throw new RuntimeException();
        }

        List<Token> conditionTokenList = getTokensUntilClose();

        if (conditionTokenList.size() == 0) {
            errMsg = "No Close Parenthesis Found";
            throw new RuntimeException();
        }

        // TODO: Check if Condition is Valid

        System.out.println(
                "Checkpoint! After executing getTokensUntilClose Function, Current Token is:" + currToken.toString());

        while (!(currToken.getType().equals(PAREN_CLOSE))) {
            advance();
        }

        System.out
                .println("Checkpoint! After advancing token to PAREN_CLOSE, Current Token is:" + currToken.toString());

        // Checking the token list for errors
        for (int i = 0; i < conditionTokenList.size(); i++) {
            System.out.println(conditionTokenList.get(i).toString());
        }

        System.out.println(
                "Checkpoint! After checking the token list for errors, Current Token is:" + currToken.toString());

        while (currToken.getType().equals(NEWLINE) || currToken.getType().equals(COMMENT)) {
            advance();
            System.out.println("Checking for Comments or Newline, Current Token is:" + currToken.toString());
        }

        // Brute Force, for some reason, it properly advance to the next token.
        for (int i = 0; i < 2; i++) {
            advance();
        }

        if (currToken.getType().equals(KW_START)) { // Continue with START keyword
            whileStart = currToken; // KW_START
            System.out.println("Checkpoint! Within the [WHILE] START, Current Token is:" + currToken.toString());
            try {
                if (peekNextTokenType().equals(EOF)) {
                    errMsg = "No STOP found";
                    throw new RuntimeException();
                } else if (peekNextTokenType().equals(KW_STOP)) {
                    errMsg = "START and STOP cannot be 1 line";
                    throw new RuntimeException();
                } else if (!peekNextTokenType().equals(NEWLINE)) {
                    errMsg = peekNextTokenType() + " must be in a newline";
                    throw new RuntimeException();
                } else if (peekNextTokenType().equals(NEWLINE)) {
                    advance();
                    while (currToken.getType().equals(NEWLINE)) {
                        advance();
                    }
                }

            } catch (RuntimeException e) {
                errToken = new Token(TokenType.ERROR, errMsg, null, currToken.getLine());

                System.out.println("it returns null2");
                System.out.println(errToken.getLexeme());
                goToEof();

            }

            System.out.println("Checkpoint! After [WHILE] Try-Catch, Current Token is:" + currToken.toString());

            if (!(currToken.isEofOrStop())) {
                whileHead_statement = declareMultStmts();
            }

            System.out
                    .println("Checkpoint! After [WHILE] isEofOrStop Checker, Current Token is:" + currToken.toString());

            if (currToken.getType().equals(KW_STOP)) {
                whileStop = currToken;
                advance();
                System.out.println(
                        "Checkpoint! After [WHILE] Token Value is STOP, Current Token is:" + currToken.toString());
            } else {

                if (hadError != true) {
                    System.out.println("[WHILE] Had Error on Token: " + currToken.toString());
                    System.out.println("[WHILE] it returns null3");
                    errToken.getLexeme();
                }
                hadError = true;

            }
        } else {
            if (hadError != true) {
                errToken = new Token(TokenType.ERROR, errMsg, null, currToken.getLine());

                System.out.println("[WHILE] it returns null4");
                System.out.println(errToken.getLexeme());
                System.out.println("[WHILE] No START found");
            }
            hadError = true;
        }

        if (hadError) {
            return null;
        } else {
            System.out.println("\n== WhileNode Program Complete. No Errors ==\n");
            return new WhileNode(whileStart, whileHead_statement, whileStop);
        }
    }

    // public IfELseStmt If() {
    // IfELseStmt IFElse = new IfELseStmt();
    // System.out.println("AA" + currToken.getType());
    // try {
    // if (currToken.getType().equals(IF)) {
    // advance();
    // ConditionStatement IE = new ConditionStatement();
    // if (currToken.getType().equals(PAREN_OPEN)) {
    // System.out.println("A1" + currToken.getType());
    // advance();
    // if (currToken.getType().equals(IDENTIFIER)) {
    // IE.setVar1(currToken);
    // System.out.println("A2" + currToken.getType());
    // advance();
    // if (currToken.getType().equals(GREATER_THAN) ||
    // currToken.getType().equals(GREATER_OR_EQUAL)
    // || currToken.getType().equals(LESS_THAN) ||
    // currToken.getType().equals(LESS_OR_EQUAL)
    // || currToken.getType().equals(EQUALS) ||
    // currToken.getType().equals(NOT_EQUAL)
    // || currToken.getType().equals(NOT) || currToken.getType().equals(AND)
    // || currToken.getType().equals(OR)) {
    // System.out.println("A3" + currToken.getType());
    // IE.setLogic(currToken);
    // advance();
    // if (currToken.getType().equals(IDENTIFIER)) {
    // System.out.println("A4");
    // IE.setVar2(currToken);
    // advance();
    // if (currToken.getType().equals(PAREN_CLOSE)) {
    // advance();
    // if (currToken.getType().equals(KW_START)) {
    // stmt.add(currToken);
    // advance();
    // while (!currToken.getType().equals(KW_STOP)) {
    // stmt.add(currToken);
    // advance();
    // if (currToken.getType().equals(KW_STOP)) {
    // stmt.add(currToken);
    // IFElse = new IfELseStmt(IE, tokens);
    // return IFElse;
    // }
    // if (currToken.getType().equals(EOF)) {
    // throw new IllegalStatementException(
    // "Missing 'STOP' at line " + currToken.getLine());
    // }
    // }
    // }

    // }

    // } else {
    // throw new IllegalStatementException(
    // "Expected 'START' after ')' at line " + currToken.getLine());
    // }
    // } else {
    // throw new IllegalStatementException("Expected ')' at line " +
    // currToken.getLine());
    // }
    // } else if (currToken.getType().equals(NEWLINE)) {
    // throw new IllegalStatementException("IF/ELSE Statement must be in one line");
    // }
    // } else {
    // throw new IllegalStatementException("Expected '(' after IF at line" +
    // currToken.getLine());
    // }
    // }
    // } catch (Exception e) {
    // System.out.println(e);
    // }
    // return IFElse;
    // }

    ProgramNode program() {
        Node head_var = null;
        Object start = null, stop = null;
        Node head_statement = null;
        String errMsg = "";

        if (currToken.getType().equals(COMMENT)) {
            advance();
        }

        while (currToken.getType().equals(NEWLINE)) {
            advance();
            if (currToken.getType().equals(COMMENT)) {
                advance();
            }
        }

        if (currToken.getType().equals(KW_VAR)) {
            head_var = declareMultVars();
        }

        if (currToken.getType().equals(COMMENT)) {
            advance();
        }

        while (currToken.getType().equals(NEWLINE)) {
            advance();
            if (currToken.getType().equals(COMMENT)) {
                advance();
            }
        }

        if (currToken.getType().equals(KW_START)) {
            start = currToken;

            try {
                if (peekNextTokenType().equals(EOF)) {
                    errMsg = "No STOP found";
                    throw new RuntimeException();
                } else if (peekNextTokenType().equals(KW_STOP)) {
                    errMsg = "START and STOP cannot be 1 line";
                    throw new RuntimeException();
                } else if (!peekNextTokenType().equals(NEWLINE)) {
                    errMsg = peekNextTokenType() + " must be in a newline";
                    throw new RuntimeException();
                } else if (peekNextTokenType().equals(NEWLINE)) {
                    advance();
                    while (currToken.getType().equals(NEWLINE)) {
                        advance();
                        if (currToken.getType().equals(COMMENT)) {
                            advance();
                        }
                    }
                }

            } catch (RuntimeException e) {
                errToken = new Token(TokenType.ERROR, errMsg, null, currToken.getLine());

                System.out.println("it returns null2");
                System.out.println(errToken.getLexeme());
                goToEof();
            }

            if (!(currToken.isEofOrStop() || currToken.getType().equals(WHILE))) {
                head_statement = declareMultStmts();
            } else if (peekNextTokenType().equals(NEWLINE)) {
                advance();
                while (currToken.getType().equals(NEWLINE)) {
                    advance();
                    if (currToken.getType().equals(COMMENT)) {
                        advance();
                    }
                }
            }

            if (currToken.getType().equals(WHILE)) {
                looped();
            }
            // if (currToken.getType().equals(IF)) {
            // If();
            // }
            // Brute Force, it needs to advance twice before it sees another STOP
            while (!(currToken.getType().equals(KW_STOP))) {
                advance();
            }

            if (currToken.getType().equals(KW_STOP)) {
                stop = currToken;
                advance();
            } else {
                if (!hadError) {
                    errMsg = "Unexpected error at " + currToken.getLine() + " with token " + currToken.getLexeme();
                }

            }
        }

        else {
            if (!hadError) {
                errMsg = "Syntax Error: No START found";
                hadError = true;
            }
        }

        while (currToken.getType() == NEWLINE) {
            advance();
        }

        if (!hadError && currToken.getType() != EOF) {
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

}
