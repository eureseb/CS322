package com.pl.Statements;

import com.pl.Token;
import com.pl.TokenType;
import com.pl.Nodes.Node;

public class IfStatement extends Statement {
    ConditionStatement condition;
    Token specialType;
    ConditionStatement condition2;
    boolean specialCheck;
    Node statement;

    public IfStatement(ConditionStatement condition, Node statement) {
        this.condition = condition;
        this.statement = statement;
    }

    public IfStatement(ConditionStatement condition, ConditionStatement condition2, Token speciaType,
            Boolean specialCheck, Node statement) {
        this.condition = condition;
        this.statement = statement;
        this.condition2 = condition2;
        this.specialCheck = specialCheck;
        this.specialType = speciaType;
    }

    public boolean getSpecialCheck() {
        return this.specialCheck;
    }

    public ConditionStatement getCondition() {
        return this.condition;
    }

    public ConditionStatement getCondition2() {
        return this.condition2;
    }

    public Token getSpecialType() {
        return this.specialType;
    }

    public Node getStatement() {
        return this.statement;
    }

    @Override
    public String toString() {
        return "Condition:\n(" + condition + "\nStatement: \n" + statement + ')' + "\n";
    }
}
