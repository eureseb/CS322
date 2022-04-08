package com.pl.Nodes;
//previously: programNode
public class ProgramNode extends Node {
    Object start;
    Object stop;
    private Node varDeclarations;
    private Node stmtDeclaration;

    public ProgramNode() {};
    public ProgramNode(Node vardecl, Object start, Node statements, Object stop) {
        this.varDeclarations = vardecl;
        this.start = start;
        this.stmtDeclaration = statements;
        this.stop = stop;
        
    }

    public ProgramNode(Object start, Node statements, Object stop) {
        this.start = start;
        this.stmtDeclaration = statements;
        this.stop = stop;
    }

    public Node getVarDeclarations(){
        return this.varDeclarations;
    }
    public Node getStmtDeclaration(){
        return this.stmtDeclaration;
    }
    public String toString() {
        return "(" + this.varDeclarations + ", \n" + this.start + ", \n" + this.stmtDeclaration + ", \n" + this.stop + ")";
    }
}