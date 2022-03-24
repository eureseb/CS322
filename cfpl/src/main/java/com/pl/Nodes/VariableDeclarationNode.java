package com.pl.Nodes;

//previously: varDeclareNode
public class VariableDeclarationNode extends Node {
    private final Object reserveword;
    private final Object identifier;
    private final Object datatype;
    private VariableDeclarationNode next = null;

    public VariableDeclarationNode(VariableDeclarationNode node) {
        this.reserveword = node.reserveword;
        this.identifier = node.identifier;
        this.datatype = node.datatype;
    }

    public VariableDeclarationNode(Object reserveword, Object identifier, Object datatype) {
        this.reserveword = reserveword;
        this.identifier = identifier;
        this.datatype = datatype;
    }

    public void setNext(VariableDeclarationNode next){
        this.next = next;
    }
    public VariableDeclarationNode getNext(){
        return this.next;
    }
    public String toString() {
        return "(" + this.reserveword + ", " + this.identifier + ", " + this.datatype + "\n" + this.next + ")";
    }
}