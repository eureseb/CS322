package com.pl.Statements;

import com.pl.Token;
import com.pl.Nodes.Node;

public class OutputStatement extends Statement{
    Token rsrvOutput;
    String string = "hello default world";
    Node right;             //expression

    public OutputStatement(Token rsrvOutput, Node right){
        this.rsrvOutput = rsrvOutput;
        this.right = right;
    }
    public OutputStatement(Token rsrvOutput, String string){
        this.rsrvOutput = rsrvOutput;
        this.string= string;
    }

    public String getString() {
        return string;
    }

    public Node getRight(){
        return this.right;
    }
    @Override
    public String toString() {
        return "(" + rsrvOutput + ", " + right + ')'+"\n"+super.getNext();
    }
}