package com.pl.Statements;

import com.pl.Token;
import com.pl.Nodes.Node;

public class AssignStatement extends Statement {
    Token identifier;       //identifier
    Token operator;         // = operator
    Node right;             //expression

    public AssignStatement(Token left, Token operator, Node right){
        this.identifier = left;
        this.operator = operator;
        this.right = right;
    }

    public Node getRight(){
        return this.right;
    }
    public Token getIdentifier() { return this.identifier; }
    @Override
    public String toString() {
        return "(" + identifier + ", " + operator + ", " + right + ')'+"\n"+ super.getNext();
    }
}