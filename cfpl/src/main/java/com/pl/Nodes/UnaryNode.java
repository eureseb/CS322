package com.pl.Nodes;

import com.pl.Token;

public class UnaryNode extends Node {
    Token operator;
    NumberNode num;

    public UnaryNode(Token operator, NumberNode num) {
        this.operator = operator;
        this.num = num;
    }

    public Token getOperator() {
        return operator;
    }

    public NumberNode getNum(){
        return this.num;
    }
    public String toString() {
        return "(" + this.operator + ", " + this.num + ")";
    }
}