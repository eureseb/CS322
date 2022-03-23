package com.pl.Nodes;

import com.pl.Token;

public class AssignNode extends Node {
    private Token identifier;
    private Token operator;
    private Node right;

    AssignNode(Token left, Token operator, Node right) {
        this.identifier = left;
        this.operator = operator;
        this.right = right;
    }

    public String toString() {
        return "(" + this.identifier + ", " + this.operator + ", " + this.right + ")";
    }
}