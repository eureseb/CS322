package com.pl.Statements;

import com.pl.Token;
import com.pl.Nodes.Node;

public class ConditionStatement extends Statement {
    Token var1;       //Variable 1
    Token logic;      //Logical Operator
    Token var2;       //Variable 2

    public ConditionStatement(Token var1, Token logic, Token var2){
        this.var1 = var1;
        this.logic = logic;
        this.var2 = var2;
    }

    public Token getVar2(){
        return this.var2;
    }
    public Token getLogic() { 
        return this.logic;
    }

    public Token getVar1() {
        return this.var1;
    }

    @Override
    public String toString() {
        return "(" + var1 + ", " + logic + ", " + var2 + ')'+"\n"+ super.getNext();
    }
}