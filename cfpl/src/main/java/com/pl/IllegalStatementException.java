package com.pl;

public class IllegalStatementException extends Exception{
    
    public IllegalStatementException(){
        super();
    }
    public IllegalStatementException(String errMsg){
        super(errMsg);
    }
}
