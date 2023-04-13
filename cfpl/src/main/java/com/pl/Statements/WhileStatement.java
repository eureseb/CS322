package com.pl.Statements;

import com.pl.Token;
import com.pl.Nodes.Node;

public class WhileStatement extends Statement {
    ConditionStatement condition;
    Node statement;
    Token specialType;
    ConditionStatement condition2;
    int specialCheck;

    public WhileStatement(ConditionStatement condition, Node statement) {
        this.condition = condition;
        this.statement = statement;
    }

    public WhileStatement(ConditionStatement condition, ConditionStatement condition2, Token speciaType,
            int specialCheck, Node statement) {
        this.condition = condition;
        this.statement = statement;
        this.condition2 = condition2;
        this.specialCheck = specialCheck;
        this.specialType = speciaType;
    }

    public ConditionStatement getCondition() {
        return this.condition;
    }

    public Node getStatement() {
        return this.statement;
    }

    public int getSpecialCheck() {
        return this.specialCheck;
    }

    public ConditionStatement getCondition2() {
        return this.condition2;
    }

    public Token getSpecialType() {
        return this.specialType;
    }

    @Override
    public String toString() {
        return "Condition:\n(" + condition + "\nStatement: \n" + statement + "\nCondition 2: \n" + condition2 + "\nSpecial Check: \n" + specialCheck + "\nSpecial Type: \n" + specialType +')' + "\n";
    }
}
