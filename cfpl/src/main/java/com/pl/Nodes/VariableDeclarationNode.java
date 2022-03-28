package com.pl.Nodes;

import com.pl.Token;

public class VariableDeclarationNode extends Node {
    private Token reserveword;
    private Token identifier;
    private Token dataType;
    private VariableDeclarationNode next = null;

    public VariableDeclarationNode(VariableDeclarationNode node) {
        this.reserveword = node.reserveword;
        this.identifier = node.identifier;
        this.dataType = node.dataType;
    }

    public VariableDeclarationNode(Token reserveword, Token identifier, Token datatype) {
        this.reserveword = reserveword;
        this.identifier = identifier;
        this.dataType = datatype;
    }


    public Token dataType(){
        return dataType;
    }
    public Token getIdentifier(){
        return identifier;
    }
    public Token getReserveWord(){
        return reserveword;
    }

    public void setNext(VariableDeclarationNode next){
        this.next = next;
    }
    public VariableDeclarationNode getNext(){
        return this.next;
    }
    public String toString() {
        return "(" + this.reserveword + ", " + this.identifier + ", " + this.dataType + "\n" + this.next + ")";
    }
}