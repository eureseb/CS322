package com.pl.Statements;

import com.pl.Token;

public class InputStatement extends Statement{
    Token rsrvInput;
    Token iden;             //expression

    public InputStatement(Token rsrvInput, Token iden){
        this.rsrvInput = rsrvInput;
        this.iden = iden;
    }

    public Token getIden(){
        return iden;
    }
    public Token getRsrvInput(){
        return rsrvInput;
    }
    @Override
    public String toString() {
        return "(" + rsrvInput + ", " + iden + ')'+"\n"+((super.getNext() != null)? super.getNext() : "");
    }
}
