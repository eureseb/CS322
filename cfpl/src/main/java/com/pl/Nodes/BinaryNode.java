package com.pl.Nodes;

import com.pl.Token;

public class BinaryNode extends Node {
    private Node left;
    private Token operator;
    private Node right;

    public BinaryNode(Node left, Token operator, Node right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Node getLeft(){
        return this.left;
    }
    public Node getRight(){
        return this.right;
    }

    public Token getOperator() {
        return operator;
    }

    public String toString() {
        return "(" + this.left + ", " + this.operator + ", " + this.right + ")";
    }
}