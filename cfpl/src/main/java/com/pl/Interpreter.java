package com.pl;


import com.pl.Nodes.*;
import com.pl.Statements.*;
import com.pl.Statements.OutputStatement;

public class Interpreter {
    String output;


    public void visit(Node node){
        System.out.println(node.getClass().toString());
        if(node == null){
            System.out.println("oops null");
        }
        else if(node.getClass().toString().equals("class com.pl.Nodes.ProgramNode")){
            visitProgramNode((ProgramNode)node);
        }
        else if(node.getClass().toString().equals("class com.pl.Nodes.VariableDeclarationNode")){
            visitVarDeclareNode((VariableDeclarationNode)node);
        }
        else if(node.getClass().toString().equals("class com.pl.Statements.AssignStatement")){
            visitAssignStmt((AssignStatement)node);
        }
        else if(node.getClass().toString().equals("class com.pl.Statements.OutputStatement")){
            visitOutputStmt((OutputStatement)node);
        }
        else if(node.getClass().toString().equals("class com.pl.Nodes.StringNode")){
            visitStringNode((StringNode)node);
        }
        else if(node.getClass().toString().equals("class com.pl.Nodes.BinaryNode")){
            visitBinaryNode((BinaryNode)node);
        }
        else if(node.getClass().toString().equals("class com.pl.Nodes.UnaryNode")){
            visitUnaryNode((UnaryNode)node);
        }
        else if(node.getClass().toString().equals("class com.pl.Nodes.NumberNode")){
            visitNumberNode((NumberNode)node);
        }
        else{
            illegalVisit(node);
        }
    }

    public void illegalVisit(Node node){
        System.out.println("You have something that I can't interpret: " + node);
    }

    public void visitProgramNode(ProgramNode progNode){
        ProgramNode ndR = progNode;
        visit(ndR.getVarDeclarations());
        visit(ndR.getStmtDeclaration());
    }

    public void visitVarDeclareNode(VariableDeclarationNode varDecNode){
        VariableDeclarationNode ndR = varDecNode;
        // TODO!!!!! initialize variable
        if(ndR.getNext() != null){
            visit(ndR.getNext());
        }
    }

    public void visitAssignStmt(AssignStatement assignStmt){
        AssignStatement ndR = assignStmt;

        visit(ndR.getRight());
        if(ndR.getNext() != null){
            visit(ndR.getNext());
        }
    }

    public void visitOutputStmt(OutputStatement outNode){
        OutputStatement ndR = outNode;

        visit(ndR.getRight());
        if(ndR.getNext() != null){
            visit(ndR.getNext());
        }
    }

    public void visitUnaryNode(UnaryNode unaryNode){
        UnaryNode ndR = unaryNode;
        visit(ndR.getNum());
    }

    public void visitBinaryNode(BinaryNode binNode){
        BinaryNode ndR = binNode;
        visit(ndR.getLeft());
        visit(ndR.getRight());
    }

    

    public void visitNumberNode(NumberNode numNode){
        //System.out.println("Found string node");
        NumberNode ndR = ((NumberNode) numNode);
        Object obj = numNode.getNum().getLiteral();
        System.out.println(obj);
    }
    public void visitStringNode(StringNode strNode){
        StringNode ndR = strNode;
        output = strNode.getString().getLexeme();
        System.out.println(output);
    }
}
