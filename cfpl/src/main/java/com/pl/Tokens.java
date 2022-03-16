package com.pl;

enum Tokens {
    KW_START("START");

    private final String value;

    Tokens(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
