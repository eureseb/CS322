package com.pl.Statements;

import com.pl.Token;
import com.pl.Nodes.Node;

public class OutputStatement extends Statement {
    Token rsrvOutput;
    Node headConcat; // expression
    ConditionStatement condition, condition2;
    boolean flag = false, Special = false;
    Token specialLogic;

    public OutputStatement(Token rsrvOutput, Node headConcat) {
        this.rsrvOutput = rsrvOutput;
        this.headConcat = headConcat;
    }

    public OutputStatement(Token rsrvOutput) {
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

    public void setSpecialLogic(Token sl) {
        this.specialLogic = sl;
        Special = true;
    }

    public void setCondition(ConditionStatement cond) {
        this.condition = cond;
        flag = true;
    }

    public void setCondition2(ConditionStatement cond) {
        this.condition2 = cond;
        Special = true;
    }

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean getSpecial() {
        return Special;
    }

    public ConditionStatement gConditionStatement() {
        return condition;
    }

    public ConditionStatement g2ConditionStatement() {
        return condition2;
    }

    public Token getSpecialLogic() {
        return specialLogic;
    }

    @Override
    public String toString() {
        return "OutputStatement{" +
                "rsrvOutput=" + rsrvOutput +
                ", headConcat=" + headConcat +
                '}';
    }
}