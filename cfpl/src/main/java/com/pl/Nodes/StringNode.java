package com.pl.Nodes;

import com.pl.Token;

public class StringNode extends Node {
    private Token string;

    public StringNode(Token string) {
        this.string = string;
    }

    public Token getString(){
        return this.string;
    }
    @Override
    public String toString() {
        return string.toString();
    }
}