package com.pl;

public enum StatusTypes {

    STATUS_OK("Okay"),
    STATUS_UNIDENTIFIED_TOKEN("Invalid token"),
    STATUS_UNDEFINED_VARIABLE("Undefined variable "),
    STATUS_INVALID_OPERATION("Invalid operation "),
    STATUS_MODULO_ON_FLOAT("Operator modulo(%) cannot be operated on type float"),
    STATUS_MISSING_START("Missing START"),
    STATUS_MISSING_STOP("Missing STOP"),
    STATUS_MISSING_OPEN_PAR("Missing ("),
    STATUS_MISSING_CLOSE_PAR("Missing )"),
    STATUS_MISSING_OPEN_BRAC("Missing ["),
    STATUS_MISSING_CLOSE_BRAC("Missing ]"),
    STATUS_FLOAT_ON_INT_STATEMENT("value (float) cannot operate(%) with value (int)"),
    STATUS_ERROR_ON_STRING("Invalid operand on string assignment"),
    STATUS_ERROR_ON_FLOAT("Invalid operand on float"),
    STATUS_ERROR_ON_INT("Invalid operand on int"),
    STATUS_ERROR_ON_BOOL("Invalid operand on boolean"),
    STATUS_ERROR_ON_CHAR("Invalid operand or operator on char"),
    STATUS_INVALID_ASSIGNMENT("Invalid assignment "),
    STATUS_USED_BUT_NOT_INITIALIZED("Variable was used but not initialized"),
    STATUS_ZERO_DIVISION("Division by zero is not applicable."),
    STATUS_STRUCTURE_ERROR("Declarations or statements cannot be after STOP statement");

    private final String value;

    StatusTypes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}