package com.pl;


import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

import javax.naming.directory.InvalidAttributeIdentifierException;

import com.pl.Nodes.*;
import com.pl.Statements.*;
import com.pl.Logic.*;

import static com.pl.TokenType.*;
public class Interpreter {
    
    private final Map<String, Object> values = new HashMap<>();
    private final Map<String, TokenType> types = new HashMap<>();
    public static boolean hadError = false;

    public Interpreter(){}
    public Object visit(Node node){
        if(node == null){
            return null;
        }
        else if(node instanceof ProgramNode){
            visitProgramNode((ProgramNode)node);
        }
        else if(node instanceof VariableDeclarationNode){
            visitVarDeclareNode((VariableDeclarationNode)node);
        }
        else if(node instanceof AssignStatement){
            visitAssignStmt((AssignStatement)node);
        }
        else if(node instanceof OutputStatement){
            visitOutputStmt((OutputStatement)node);
        }
        else if(node instanceof InputStatement){
            visitInputStmt((InputStatement)node);
        }
        else if(node instanceof StringNode){
            return visitStringNode((StringNode)node);
        }
        else if(node instanceof BinaryNode){
            return visitBinaryNode((BinaryNode)node);
        }
        else if(node instanceof UnaryNode){
            return visitUnaryNode((UnaryNode)node);
        }
        else if(node instanceof NumberNode){
            return visitNumberNode((NumberNode)node);
        }
        else if(node instanceof WhileStatement){
            visitWhileStmt((WhileStatement)node);
        }
        else{
            illegalVisit(node);
        }
        return null;

    }

    public void illegalVisit(Node node){
        System.out.println("You have something that I can't interpret: " + node);
    }

    public void visitProgramNode(ProgramNode progNode){
        visit(progNode.getVarDeclarations());
        visit(progNode.getStmtDeclaration());
    }

    public void visitVarDeclareNode(VariableDeclarationNode varDecNode){
        String nodeTokenLexeme = varDecNode.getIdentifier().getLexeme();
        if(values.containsKey(nodeTokenLexeme)){
            System.out.println(nodeTokenLexeme + " is in conflict with other declarations. Check program");
        }
        values.put(nodeTokenLexeme, varDecNode.getValue());
        types.put(nodeTokenLexeme, varDecNode.dataType().getType());
        if(varDecNode.getNext() != null){
            visit(varDecNode.getNext());
        }
    }

    public void visitAssignStmt(AssignStatement assignStmt){
        Object expr = visit(assignStmt.getRight());

        if(values.containsKey(assignStmt.getIdentifier().getLexeme())){
            values.put(assignStmt.getIdentifier().getLexeme(), expr);
        }
        else{
            System.out.println("Identifier '"+assignStmt.getIdentifier().getLexeme() + "' does not exist.");
            hadError = true;
        }

        if(assignStmt.getNext() != null){
            visit(assignStmt.getNext());
        }
    }

    public void visitWhileStmt(WhileStatement whileStmt){

       Logic L = new Logic();
       System.out.println("Copied Values to Values2");
       ConditionStatement cond = whileStmt.getCondition();
       System.out.println("Before getting values");
       System.out.println(cond.toString());

       Boolean checker = values.isEmpty();
       System.out.println("Is Values Empty? : " + checker);

        // if(condition.getVar1().getType().equals(IDENTIFIER)){
        //     Token temp = new Token(types.get(condition.getVar1().getLexeme()),
        //                             values.get(condition.getVar1().getLexeme()).toString(),
        //                             values.get(condition.getVar1().getLiteral()).toString(),
        //                             condition.getVar1().getLine());
        //     condition.setVar1(temp);
        // }

        // if(condition.getVar2().getType().equals(IDENTIFIER)){
        //     Token temp = new Token(types.get(condition.getVar2().getLexeme()),
        //                             values.get(condition.getVar2().getLexeme()).toString(),
        //                             values.get(condition.getVar2().getLiteral()).toString(),
        //                             condition.getVar2().getLine());
        //     condition.setVar2(temp);
        // }

        System.out.println("After getting values");
        System.out.println(whileStmt.toString());

        System.out.println("Testing Logic");
        if(LogicHandler(cond) == true){
            System.out.println("Success\n");
        }
        else{
            System.out.println("Something went wrong\n");
        }

        System.out.println("Test Phase\n");
        while(LogicHandler(cond) == true){
            visit(whileStmt.getStatement());
        }

    //    System.out.println("Testing Logic====="); 
    //    if(L.LogicHandler(condition) == true){
    //     System.out.println("Success\n");
    //    }
    //    else{
    //     System.out.println("Something went wrong\n");
    //    }

    //    System.out.println("Test Phase=====");
        
    //     if(L.LogicHandler(condition) == true){
    //         visit(whileStmt.getStatement());
    //         System.out.println("" + values.get("X").toString());
    //         System.out.println(condition.toString());
    //         if(L.LogicHandler(condition) == true){
    //             visit(whileStmt.getStatement());
    //             System.out.println("" + values.get("X").toString());
    //             System.out.println(condition.toString());
    //         }
    //         else{
    //             System.out.println("Something went wrong\n");
    //         }
    //     }
    //     else{
    //         System.out.println("Something went wrong\n");
    //     }

       // System.out.println("" + values.get("X").toString());

       System.out.println("Exiting While");
    }

    public void visitOutputStmt(OutputStatement outNode){
        Object outputStmt = visit(outNode.getHeadConcat());
        System.out.println(outputStmt);
        Node currStrNode = outNode.getHeadConcat();
        while(currStrNode.getNext() != null){
            System.out.println(visit(currStrNode.getNext()));
            currStrNode = currStrNode.getNext();
        }
    }

    public void visitInputStmt(InputStatement node){
        Scanner sc = new Scanner(System.in);
        String ndrLexeme = node.getIden().getLexeme();
        Token ndrIden = node.getIden();
        String inp;
        String errMsg = "";
        if(values.containsKey(ndrLexeme)){
            System.out.print("Enter value for "+ndrIden.getLexeme()+": ");
            inp = sc.next();
            try{
                if(types.get(ndrLexeme) == KW_INT){
                        values.put(ndrLexeme, Integer.parseInt(inp));
                }
                else if(types.get(ndrLexeme) == KW_FLOAT){
                        values.put(ndrLexeme, Float.parseFloat(inp));
                    }
                else if(types.get(ndrLexeme) == KW_CHAR){
                        values.put(ndrLexeme, inp.charAt(0));
                    }
                else if(types.get(ndrLexeme) == KW_BOOLEAN){
                    if(inp.equalsIgnoreCase("true") || inp.equalsIgnoreCase("false")){
                        values.put(ndrLexeme, Boolean.parseBoolean(inp));
                    }
                    else{
                        throw new IllegalStatementException();
                    }
                }
            } catch(Exception e){
                errMsg = "Incompatible datatypes for identifier "+ndrIden+ " at line "+ndrIden.getLine();
                hadError = true;
            }
        }
        else{
            errMsg = "Identifier "+ndrIden+" at line "+ndrIden.getLine()+" has not been defined.";
            hadError = true;
        }

        if(hadError){
            System.out.println(errMsg);
        }

        if(node.getNext() != null){
            visit(node.getNext());
        }
    }
    
    public Object visitUnaryNode(UnaryNode unaryNode){
        Object num = visit(unaryNode.getNum());
        if (unaryNode.getOperator().getType() == MINUS) {
            return -(int)num;
        }
        else {
            return num;
        }
    }

    public Object visitBinaryNode(BinaryNode binNode){
        Object left = visit(binNode.getLeft());
        Object right = visit(binNode.getRight());
        Object output = null;

        if(binNode.getOperator().getType() == PLUS){
            output = Float.parseFloat(left.toString()) + Float.parseFloat(right.toString());
        }
        else if(binNode.getOperator().getType() == MINUS){
            output = Float.parseFloat(left.toString()) - Float.parseFloat(right.toString());
        }
        else if(binNode.getOperator().getType() == MULTIPLY){
            output = Float.parseFloat(left.toString()) * Float.parseFloat(right.toString());
        }
        else if(binNode.getOperator().getType() == DIVIDE){
            output = Float.parseFloat(left.toString()) / Float.parseFloat(right.toString());
        }
        else{
            System.out.println("Syntax Error: Expected an operator but got " + binNode.getOperator());
        }

        if(left instanceof Integer && right instanceof Integer){
            output = (int)((float)output);
        }

        return output;
    }

    public Object visitNumberNode(NumberNode numNode){
        TokenType nodeType = numNode.getNum().getType();
        String nodeLexeme = numNode.getNum().getLexeme();

        Object literal;

        if(nodeType == IDENTIFIER){
            literal = values.get(nodeLexeme);
        }
        else{
            literal = numNode.getNum().getLiteral();
        }
        return literal;
    }

    public String visitStringNode(StringNode strNode){
        return strNode.getString().getLexeme();
    }

    public Map<String, Object> getValues() {
        return values;
    }

    private void Error(String err) {
        System.out.println(err);
    }

    private boolean LogicHandler(ConditionStatement cond){
        int Ileft, Iright;
        String Sleft, Sright;
        Boolean Bleft, Bright;
        float Fleft, Fright;
        Character Cleft, Cright;

        boolean check = false;
        Token leftToken = cond.getVar1(), rightToken = cond.getVar2(), condition = cond.getLogic();
        TokenType leftvar = leftToken.getType(), rightvar = rightToken.getType();
        System.out.println("Left Lexemme: " + leftToken.getLexeme() + "\nLeft TokenType: " + leftvar + "\nRight Lexemme: " + rightToken.getLexeme() + "\nRight TokenType: " + rightvar);
        // Stupid method cause java var is shit
        if (leftToken.getType() == TokenType.INT || leftToken.getType() == TokenType.KW_INT || leftToken.getType() == TokenType.IDENTIFIER) {
            if (leftToken.getType() == TokenType.IDENTIFIER) {
                var l = Integer.parseInt(values.get(leftToken.getLiteral().toString()).toString()); 
                Ileft = l;
                System.out.println("\nLeft Token: " + Ileft);
            } else {
                var l = Integer.parseInt(leftToken.getLiteral().toString());
                Ileft = l;
                System.out.println("\nLeft Token: " + Ileft);
            }
            if (rightToken.getType() == TokenType.INT || rightToken.getType() == TokenType.KW_INT || rightToken.getType() == TokenType.IDENTIFIER) {
                if (rightToken.getType() == TokenType.IDENTIFIER) {
                    var r = Integer.parseInt(values.get(rightToken.getLiteral().toString()).toString());
                    Iright = r;
                    System.out.println("Right Token: " + Iright);
                } else {
                    var r = Integer.parseInt(rightToken.getLiteral().toString());
                    Iright = r;
                    System.out.println("Right Token: " + Iright);
                }
 
                System.out.println("Condition Token: " + condition.getLexeme());
                switch (condition.getLexeme()) {
                    case "<":
                        if (Ileft < Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "<=":
                        if (Ileft <= Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case ">":
                        if (Ileft > Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case ">=":
                        if (Ileft >= Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "<>":
                        if (Ileft != Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "NOT":
                        if (Ileft != Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "=":
                        if (Ileft == Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                }
            } else if (rightvar == TokenType.BOOL || rightvar == TokenType.KW_BOOLEAN) {
                Error("Error: can't compare INT to BOOL");
            } else if (rightvar == TokenType.FLOAT || rightvar == TokenType.KW_FLOAT) {
                if (rightToken.getType() == TokenType.IDENTIFIER) {
                    var r = Integer.parseInt(values.get(rightToken.getLiteral().toString()).toString());
                    Fright = r;
                } else {
                    var r = Integer.parseInt(rightToken.getLiteral().toString());
                    Fright = r;
                }
                switch (condition.getLexeme()) {
                    case "<":
                        if (Ileft < Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "<=":
                        if (Ileft <= Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case ">":
                        if (Ileft > Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case ">=":
                        if (Ileft >= Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "<>":
                        if (Ileft != Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "NOT":
                        if (Ileft != Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "=":
                        if (Ileft == Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                }
            } else if (rightvar == TokenType.STRING || rightvar == TokenType.KW_STRING) {
                Error("Error: can't compare INT to String");
            }
 
        } else if (leftvar == TokenType.BOOL || leftvar == TokenType.KW_BOOLEAN) {
            if (leftToken.getType() == TokenType.IDENTIFIER) {
                var l = Boolean.parseBoolean(values.get(leftToken.getLiteral().toString()).toString());
                Bleft = l;
            } else {
                var l = Boolean.parseBoolean(leftToken.getLiteral().toString());
                Bleft = l;
            }
            if (rightvar == TokenType.INT || rightvar == TokenType.KW_INT) {
                Error("Error: can't compare INT to BOOL");
 
            } else if (rightvar == TokenType.BOOL || rightvar == TokenType.KW_BOOLEAN) {
                if (rightToken.getType() == TokenType.IDENTIFIER) {
                    var r = Boolean.parseBoolean(values.get(rightToken.getLiteral().toString()).toString());
                    Bright = r;
                } else {
                    var r = Boolean.parseBoolean(rightToken.getLiteral().toString());
                    Bright = r;
                }
                switch (condition.getLexeme()) {
                    case "<":
                        Error("The operator < is undefined for the argument type(s) boolean, boolean");
                        break;
                    case "<=":
                        Error("The operator <= is undefined for the argument type(s) boolean, boolean");
                        break;
                    case ">":
                        Error("The operator > is undefined for the argument type(s) boolean, boolean");
                        break;
                    case ">=":
                        Error("The operator >= is undefined for the argument type(s) boolean, boolean");
                        break;
                    case "<>":
                        if (Bleft != Bright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "NOT":
                        if (Bleft != Bright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "=":
                        if (Bleft == Bright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                }
            } else if (rightvar == TokenType.FLOAT || rightvar == TokenType.KW_FLOAT) {
                Error("Error: Can't compare boolean to float");
            } else if (rightvar == TokenType.STRING || rightvar == TokenType.KW_STRING) {
                Error("Error: can't compare boolean to String");
            }
        } else if (leftvar == TokenType.FLOAT || leftvar == TokenType.KW_FLOAT) {
            if (leftToken.getType() == TokenType.IDENTIFIER) {
                var l = Float.parseFloat(values.get(leftToken.getLiteral().toString()).toString());
                Fleft = l;
            } else {
                var l = Float.parseFloat(leftToken.getLiteral().toString());
                Fleft = l;
            }
            if (rightvar == TokenType.INT || rightvar == TokenType.KW_INT) {
                if (rightToken.getType() == TokenType.IDENTIFIER) {
                    var r = Integer.parseInt(values.get(rightToken.getLiteral().toString()).toString());
                    Iright = r;
                } else {
                    var r = Integer.parseInt(rightToken.getLiteral().toString());
                    Iright = r;
                }
                switch (condition.getLexeme()) {
                    case "<":
                        if (Fleft < Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "<=":
                        if (Fleft <= Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case ">":
                        if (Fleft > Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case ">=":
                        if (Fleft >= Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "<>":
                        if (Fleft != Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "NOT":
                        if (Fleft != Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "=":
                        if (Fleft == Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                }
            } else if (rightvar == TokenType.BOOL || rightvar == TokenType.KW_BOOLEAN) {
                Error("Error: can't compare FLOAT to BOOL");
            } else if (rightvar == TokenType.FLOAT || rightvar == TokenType.KW_FLOAT) {
                if (rightToken.getType() == TokenType.IDENTIFIER) {
                    var r = Float.parseFloat(values.get(rightToken.getLiteral().toString()).toString());
                    Fright = r;
                } else {
                    var r = Float.parseFloat(rightToken.getLiteral().toString());
                    Fright = r;
                }
                switch (condition.getLexeme()) {
                    case "<":
                        if (Fleft < Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "<=":
                        if (Fleft <= Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case ">":
                        if (Fleft > Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case ">=":
                        if (Fleft >= Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "<>":
                        if (Fleft != Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "NOT":
                        if (Fleft != Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "=":
                        if (Fleft == Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                }
            } else if (rightvar == TokenType.STRING || rightvar == TokenType.KW_STRING) {
                Error("Error: can't compare FLOAT to String");
            }
        } else if (leftvar == TokenType.STRING || leftvar == TokenType.KW_STRING) {
            if (leftToken.getType() == TokenType.IDENTIFIER) {
                var l = values.get(leftToken.getLiteral().toString()).toString();
                Sleft = l;
            } else {
                var l = values.get(leftToken.getLiteral().toString()).toString();
                Sleft = l;
            }
            if (rightvar == TokenType.INT || rightvar == TokenType.KW_INT) {
                Error("Error: can't compare STRING to INT");
            } else if (rightvar == TokenType.BOOL || rightvar == TokenType.KW_BOOLEAN) {
                Error("Error: can't compare STRING to BOOL");
            } else if (rightvar == TokenType.FLOAT || rightvar == TokenType.KW_FLOAT) {
                Error("Error: can't compare STRING to FLOAT");
            } else if (rightvar == TokenType.STRING || rightvar == TokenType.KW_STRING) {
                if (rightToken.getType() == TokenType.IDENTIFIER) {
                    var r = rightToken.getLiteral().toString();
                    Sright = r;
                } else {
                    var r = rightToken.getLiteral().toString();
                    Sright = r;
                }
                switch (condition.getLexeme()) {
                    case "<":
                        Error("The operator < is undefined for the argument type(s) String, String");
                        break;
                    case "<=":
                        Error("The operator <= is undefined for the argument type(s) String, String");
                        break;
                    case ">":
                        Error("The operator ? is undefined for the argument type(s) String, String");
                        break;
                    case ">=":
                        Error("The operator >= is undefined for the argument type(s) String, String");
                        break;
                    case "<>":
                        if (Sleft != Sright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "NOT":
                        if (Sleft != Sright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "=":
                        if (Sleft == Sright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                }
            }
        }
        return check;
     }
}