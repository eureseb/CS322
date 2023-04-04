package com.pl.Statements;

import java.util.List;

import com.pl.Token;

public class IfELseStmt extends Statement {
    ConditionStatement cd;
    List<Token> tokens;

    public IfELseStmt(ConditionStatement cd, List<Token> tokens) {
        this.cd = cd;
        this.tokens = tokens;
    }

    public IfELseStmt() {

    }

    public List<Token> getTokens() {
        return tokens;
    }

    @Override
    public String toString() {
        return "(" + cd.var1 + ", " + cd.logic + ", " + cd.var2 + ')' + "\n" + tokens.toString() + super.getNext();
    }
}
