package com.pl.Statements;

import com.pl.Nodes.Node;

public class WhileStatement extends Statement{
    ConditionStatement condition;
    Node statement;

    public WhileStatement(ConditionStatement condition, Node statement){
        this.condition = condition;
        this.statement = statement;
    }

    public ConditionStatement getCondition(){
        return this.condition;
    }

    public Node getStatement(){
        return this.statement;
    }

    @Override
    public String toString() {
        return "Condition:\n(" + condition + "\nStatement: \n" + statement + ')'+"\n";
    }
}