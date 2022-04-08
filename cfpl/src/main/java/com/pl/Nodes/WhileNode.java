package com.pl.Nodes;

public class WhileNode extends Node {
    Object whileStart;
    Object whileStop;
    private Node whileStmtDeclaration;

    public WhileNode(Object whileStart, Node whileStatements, Object whileStop) {
        this.whileStart = whileStart;
        this.whileStmtDeclaration = whileStatements;
        this.whileStop = whileStop;
    }

    public Node getWhileStmtDeclaration(){
        return this.whileStmtDeclaration;
    }
    public String toString() {
        return "(" + this.whileStart + ", \n" + this.whileStmtDeclaration + ", \n" + this.whileStop + ")";
    }
}