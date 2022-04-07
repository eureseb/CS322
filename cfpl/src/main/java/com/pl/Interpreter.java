package com.pl;


import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.pl.Nodes.*;
import com.pl.Statements.*;

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
            inp = sc.nextLine();
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
}