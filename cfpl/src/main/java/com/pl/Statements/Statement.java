package com.pl.Statements;

import com.pl.Nodes.Node;

public abstract class Statement extends Node{
    private Statement next;
    
    public void setNext(Statement next){
        this.next = next;
    }
    public Statement getNext(){
        return this.next;
    }
}
