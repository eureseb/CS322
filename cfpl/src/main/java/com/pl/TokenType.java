package com.pl;

public enum TokenType {
    // Keywords
    KW_START("KW_START"),
    KW_STOP("KW_STOP"),
    KW_VAR("KW_VAR"),
    KW_AS("KW_AS"),
    KW_INT("KW_INTEGER"),
    KW_FLOAT("KW_FLOATING"),
    KW_STRING("KW_STRING"),
    KW_BOOLEAN("KW_BOOLEAN"),
    KW_OUTPUT("KW_OUTPUT"),
    KW_INPUT("KW_INPUT"),
    KW_CHAR("KW_CHAR"),

    //Language Specifics

    NEWLINE("NL"), // Value is (#)
    CONCATENATOR("CON"), // Value is (&)
    COMMENT_INIT("COMI"), // Value is (*)
    ESCAPE_OPEN("ESCO"), // Value is ([)
    ESCAPE_CLOSE("ESCC"), // Value is (])

    //Values

    STRING("V_STR"),
    CHAR("V_CH"),
    INT("V_INT"),
    UNARY_INT("U_INT"),
    FLOAT("V_FLOAT"),
    UNARY_FLOAT("U_FLOAT"),
    BOOL("V_BOOL"),
    BOOL_TRUE("V_TRUE"),
    BOOL_FALSE("V_FALSE"),

    //Assignment Operator

    EQUALS("EQUAL"),

    //Identifier

    IDENTIFIER("IDEN"),

    //Math Operators

    MULTIPLY("MUL"),
    DIVIDE("DIV"),
    PLUS("ADD"),
    MINUS("SUB"),
    MODULO("MODULO"),

    //Logical Operators

    AND("AND"),
    OR("OR"),
    NOT("NOT"),
    IF("IF"),
    ELSE("ELSE"),
    WHILE("WHILE"),
    GREATER_THAN("GRE"),
    GREATER_OR_EQUAL("GOE"),
    LESS_THAN("LES"),
    LESS_OR_EQUAL("LOE"),
    LOGICAL_EQUAL("LE"),
    NOT_EQUAL("NE"),

    //Special Characters
    COMMA("COMMA"),
    COLON("COLN"),

    //Grouping Symbols
    PAREN_OPEN("PO"),
    PAREN_CLOSE("PC"),

    //Error

    ERROR("ERR"),

    //Statement Type
    ST_DECLARATION("DECLARATION"),
    ST_ASSIGNMENT("ASSIGNMENT"),
    ST_OUTPUT("OUTPUT"),
    ST_INPUT("INPUT"),

    //End of file
    EOF("FILE_END"),
    DEFAULT("DEF");

    private final String value;

    TokenType(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean isIdentifier(String token){
        return token == "IDEN";
    }

    public static boolean isDataType(TokenType tokenType){
        if(tokenType == KW_INT ||
           tokenType == KW_FLOAT ||
           tokenType == KW_BOOLEAN ||
           tokenType == KW_CHAR){
               return true;
           }
        return false; 
    }
}
