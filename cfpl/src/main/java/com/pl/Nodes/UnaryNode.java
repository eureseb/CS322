package com.pl.Nodes;

public class UnaryNode extends Node {
    Object operator;
    Node num;

    public UnaryNode(Object operator, Node num) {
        this.operator = operator;
        this.num = num;
    }
    public Node getNum(){
        return this.num;
    }
    public String toString() {
        return "(" + this.operator + ", " + this.num + ")";
    }
}