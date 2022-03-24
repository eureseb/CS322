package com.pl.Nodes;

public class BinaryNode extends Node {
    private Node left;
    private Object operator;
    private Node right;

    public BinaryNode(Node left, Object operator, Node right) {
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
    public String toString() {
        return "(" + this.left + ", " + this.operator + ", " + this.right + ")";
    }
}