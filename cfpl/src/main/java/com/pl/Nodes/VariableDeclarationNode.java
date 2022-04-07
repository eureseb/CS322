package com.pl.Nodes;

import com.pl.Token;

public class VariableDeclarationNode extends Node {
    private final Token identifier;
    private final Token dataType;
    private final Object value;
    private VariableDeclarationNode next = null;

    public VariableDeclarationNode(VariableDeclarationNode node) {
        this.value = node.value;
        this.identifier = node.identifier;
        this.dataType = node.dataType;
    }

    public VariableDeclarationNode(Token identifier, Token datatype, Object value) {
        this.value = value;
        this.identifier = identifier;
        this.dataType = datatype;
    }


    public Token dataType(){
        return dataType;
    }
    public Token getIdentifier(){
        return identifier;
    }
    public Object getValue() { return value; }

    public void setNext(VariableDeclarationNode next){
        this.next = next;
    }
    public VariableDeclarationNode getNext(){
        return this.next;
    }
    public String toString() {
        return "(" + this.value + ", " + this.identifier + ", " + this.dataType + "\n" + this.next + ")";
    }
}