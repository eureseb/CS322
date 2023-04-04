package com.pl.Statements;

import com.pl.Nodes.Node;

public class ElseStatement extends Statement {
    Node statement;

    public ElseStatement(Node statement){
        this.statement = statement;
    }

    public Node getStatement(){
        return this.statement;
    }

    @Override
    public String toString() {
        return "\nStatement: \n" + statement + ')'+"\n";
    }
}
