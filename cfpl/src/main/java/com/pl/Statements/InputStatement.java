package com.pl.Statements;

import com.pl.Token;

public class InputStatement extends Statement{
    Token iden;             //expression

    public InputStatement(Token iden){
        this.iden = iden;
    }

    public Token getIden(){
        return iden;
    }

    @Override
    public String toString() {
        return "(" + iden + ')'+"\n"+((super.getNext() != null)? super.getNext() : "");
    }
}
