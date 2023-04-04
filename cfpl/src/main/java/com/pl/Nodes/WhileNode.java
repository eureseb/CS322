package com.pl.Nodes;

public class WhileNode extends Node {
    private Node statement;
    private Node condition;

    public WhileNode(Node condition, Node statement) {
        this.statement = statement;
        this.condition = condition;
    }

    public Node getWhileStatement(){
        return this.statement;
    }

    public Node getWhileCondition(){
        return this.condition;
    }

    public String toString() {
        return "(" + this.condition + ", \n" + this.statement + ")";
    }
}