package com.pl.Nodes;

import com.pl.Token;

public class NumberNode extends Node {
    private Token num;

    public NumberNode(Token num) {
        this.num = num;
    }

    public Token getNum(){
        return this.num;
    }
    
    @Override
    public String toString() {
        return num.toString();
    }
}