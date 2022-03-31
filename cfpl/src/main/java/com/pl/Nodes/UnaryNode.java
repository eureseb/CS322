package com.pl.Nodes;

import com.pl.Token;

public class UnaryNode extends Node {
    Token operator;
    Node num;

    public UnaryNode(Token operator, Node num) {
        this.operator = operator;
        this.num = num;
    }

    public Token getOperator() {
        return operator;
    }

    public Node getNum(){
        return this.num;
    }
    public String toString() {
        return "(" + this.operator + ", " + this.num + ")";
    }
}