package com.pl.Statements;

import com.pl.Token;
import com.pl.Nodes.Node;

public class OutputStatement extends Statement{
    Token rsrvOutput;
    Node headConcat;             //expression

    public OutputStatement(Token rsrvOutput, Node headConcat){
        this.rsrvOutput = rsrvOutput;
        this.headConcat = headConcat;
    }
    public OutputStatement(Token rsrvOutput){
        this.rsrvOutput = rsrvOutput;
    }

    public Token getRsrvOutput() {
        return rsrvOutput;
    }

    public void setRsrvOutput(Token rsrvOutput) {
        this.rsrvOutput = rsrvOutput;
    }

    public Node getHeadConcat() {
        return headConcat;
    }

    public void setHeadConcat(Node headConcat) {
        this.headConcat = headConcat;
    }

    @Override
    public String toString() {
        return "OutputStatement{" +
                "rsrvOutput=" + rsrvOutput +
                ", headConcat=" + headConcat +
                '}';
    }
}
