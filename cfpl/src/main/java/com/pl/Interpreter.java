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
    String output;
    public static boolean hadError = false;

    public Interpreter(){}
    public Object visit(Node node){
//        System.out.println(node.getClass().toString());
        if(node == null){
        }
        else if(classNameOf(node).equals("class com.pl.Nodes.ProgramNode")){
            visitProgramNode((ProgramNode)node);
        }
        else if(classNameOf(node).equals("class com.pl.Nodes.VariableDeclarationNode")){
            visitVarDeclareNode((VariableDeclarationNode)node);
        }
        else if(classNameOf(node).equals("class com.pl.Statements.AssignStatement")){
            visitAssignStmt((AssignStatement)node);
        }
        else if(classNameOf(node).equals("class com.pl.Statements.OutputStatement")){
            visitOutputStmt((OutputStatement)node);
        }
        else if(classNameOf(node).equals("class com.pl.Statements.InputStatement")){
            visitInputStmt((InputStatement)node);
        }
        else if(classNameOf(node).equals("class com.pl.Nodes.StringNode")){
            return visitStringNode((StringNode)node);
        }
        else if(classNameOf(node).equals("class com.pl.Nodes.BinaryNode")){
            return visitBinaryNode((BinaryNode)node);
        }
        else if(classNameOf(node).equals("class com.pl.Nodes.UnaryNode")){
            return visitUnaryNode((UnaryNode)node);
        }
        else if(classNameOf(node).equals("class com.pl.Nodes.NumberNode")){
            return visitNumberNode((NumberNode)node);
        }
        else{
            illegalVisit(node);
        }
        return null;
    }

    private String classNameOf(Node node){
        if(node != null){
            return node.getClass().toString();
        }
        return null;
    }
    public void illegalVisit(Node node){
        System.out.println("You have something that I can't interpret: " + node);
    }

    public void visitProgramNode(ProgramNode progNode){
        ProgramNode ndR = progNode;
        visit(ndR.getVarDeclarations());
        visit(ndR.getStmtDeclaration());
        System.out.println(values);
    }

    public void visitVarDeclareNode(VariableDeclarationNode varDecNode){
        VariableDeclarationNode node = varDecNode;
        String nodeTokenLexeme = node.getIdentifier().getLexeme();
        values.put(nodeTokenLexeme, null);
        types.put(nodeTokenLexeme, node.dataType().getType());
        if(node.getNext() != null){
            visit(node.getNext());
        }
    }

    public void visitAssignStmt(AssignStatement assignStmt){
        AssignStatement ndR = assignStmt;
        // TODO!!!! check if it is a valid value considering the datatype

        Object expr = visit(ndR.getRight());

        if(values.containsKey(ndR.getIdentifier().getLexeme())){
            values.put(ndR.getIdentifier().getLexeme(), expr);
        }
        else{
            System.out.println("Identifier '"+ndR.getIdentifier().getLexeme() + "' does not exist.");
            hadError = true;
        }

        if(ndR.getNext() != null){
            visit(ndR.getNext());
        }
    }

    public void visitOutputStmt(OutputStatement outNode){
        OutputStatement ndR = outNode;
//        System.out.println(outNode);
        Object ndrRight = visit(ndR.getRight());
        if(ndrRight != null){
            System.out.println("printed to : " + ndrRight);
        }
        else{
            System.out.println("ndr to" + ndR.getString());
        }

        if(ndR.getNext() != null){
            visit(ndR.getNext());
        }
    }

    public void visitInputStmt(InputStatement node){
        InputStatement ndR = ((InputStatement) node);
        Scanner sc = new Scanner(System.in);
        String ndrLexeme = ndR.getIden().getLexeme();
        Token ndrIden = ndR.getIden(); 
        Object inp;
        String errMsg = "";
        if(values.containsKey(ndrLexeme)){
            System.out.print("Enter value for "+ndrIden.getLexeme()+": ");
            inp = sc.nextLine();
            try{
                if(types.get(ndrLexeme) == KW_INT){
                        inp = Integer.parseInt(inp.toString());
                        values.put(ndrLexeme, inp);
                }
                else if(types.get(ndrLexeme) == KW_FLOAT){
                        inp = Float.parseFloat(inp.toString());
                        values.put(ndrLexeme, inp);
                    }
                else if(types.get(ndrLexeme) == KW_CHAR){
                        inp = inp.toString().charAt(0);
                        values.put(ndrLexeme, inp);
                    }
                else if(types.get(ndrLexeme) == KW_BOOLEAN){
                    if(((String) inp).equalsIgnoreCase(("true")) || ((String) inp).equalsIgnoreCase("false")){
                        values.put(ndrLexeme, Boolean.parseBoolean(inp.toString()));
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

        if(ndR.getNext() != null){
            visit(ndR.getNext());
        }
    }
    
    public Object visitUnaryNode(UnaryNode unaryNode){
        UnaryNode ndR = unaryNode;
        visit(ndR.getNum());
        return null;
    }

    public Object visitBinaryNode(BinaryNode binNode){
        BinaryNode ndR = binNode;
        visit(ndR.getLeft());
        visit(ndR.getRight());
        return null;
    }

    public Object visitNumberNode(NumberNode numNode){
        //NumberNode ndR = ((NumberNode) numNode);
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
        output = strNode.getString().getLexeme();
//        System.out.println("called visit string ndoe");
        return output;
    }
}