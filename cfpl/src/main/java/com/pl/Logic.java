package com.pl;

import java.time.chrono.IsoEra;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.print.attribute.standard.NumberOfInterveningJobs;

import com.pl.Statements.ConditionStatement;

import java.util.Map;
import java.util.HashMap;

public class Logic {

    TokenType token;
    ArrayList<Token> vars = new ArrayList<Token>();
    String str;
    Stack<Token> stack = new Stack<Token>();
    Interpreter interpreter;
    // Stupid method cause java var is shit
    int Ileft, Iright;
    String Sleft, Sright;
    Boolean Bleft, Bright;
    float Fleft, Fright;
    Character Cleft, Cright;

    Logic() {

    }

    private void Error(String err) {
        System.out.println(err);
    }

    boolean LogicHandler(ConditionStatement cond) {
        boolean check = false;
        Token leftToken = cond.getVar1(), rightToken = cond.getVar2(), condition = cond.getLogic();
        TokenType leftvar = leftToken.getType(), rightvar = rightToken.getType();
        System.out.println("Left Lexemme: " + leftToken.getLexeme() + "\nLeft TokenType: " + leftvar + "\nRight Lexemme: " + rightToken.getLexeme() + "\nRight TokenType: " + rightvar);
        // Stupid method cause java var is shit
        if (leftToken.getType() == TokenType.INT || leftToken.getType() == TokenType.KW_INT || leftToken.getType() == TokenType.IDENTIFIER) {
            if (leftToken.getType() == TokenType.IDENTIFIER) {
                var l = Integer.parseInt(getVarValue(leftToken.getLiteral().toString())); //gets caught here with "Cannot invoke "com.pl.Interpreter.getValues()" because "this.interpreter" is null
                Ileft = l;
                System.out.println("\nLeft Token: " + Ileft);
            } else {
                var l = Integer.parseInt(leftToken.getLiteral().toString());
                Ileft = l;
                System.out.println("\nLeft Token: " + Ileft);
            }
            if (rightToken.getType() == TokenType.INT || rightToken.getType() == TokenType.KW_INT || rightToken.getType() == TokenType.IDENTIFIER) {
                if (rightToken.getType() == TokenType.IDENTIFIER) {
                    var r = Integer.parseInt(getVarValue(rightToken.getLiteral().toString()));
                    Iright = r;
                    System.out.println("Right Token: " + Iright);
                } else {
                    var r = Integer.parseInt(rightToken.getLiteral().toString());
                    Iright = r;
                    System.out.println("Right Token: " + Iright);
                }

                System.out.println("Condition Token: " + condition.getLexeme());
                switch (condition.getLexeme()) {
                    case "<":
                        if (Ileft < Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "<=":
                        if (Ileft <= Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case ">":
                        if (Ileft > Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case ">=":
                        if (Ileft >= Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "<>":
                        if (Ileft != Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "NOT":
                        if (Ileft != Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "=":
                        if (Ileft == Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                }
            } else if (rightvar == TokenType.BOOL || rightvar == TokenType.KW_BOOLEAN) {
                Error("Error: can't compare INT to BOOL");
            } else if (rightvar == TokenType.FLOAT || rightvar == TokenType.KW_FLOAT) {
                if (rightToken.getType() == TokenType.IDENTIFIER) {
                    var r = Integer.parseInt(getVarValue(rightToken.getLiteral().toString()));
                    Fright = r;
                } else {
                    var r = Integer.parseInt(rightToken.getLiteral().toString());
                    Fright = r;
                }
                switch (condition.getLexeme()) {
                    case "<":
                        if (Ileft < Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "<=":
                        if (Ileft <= Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case ">":
                        if (Ileft > Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case ">=":
                        if (Ileft >= Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "<>":
                        if (Ileft != Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "NOT":
                        if (Ileft != Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "=":
                        if (Ileft == Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                }
            } else if (rightvar == TokenType.STRING || rightvar == TokenType.KW_STRING) {
                Error("Error: can't compare INT to String");
            }

        } else if (leftvar == TokenType.BOOL || leftvar == TokenType.KW_BOOLEAN) {
            if (leftToken.getType() == TokenType.IDENTIFIER) {
                var l = Boolean.parseBoolean(getVarValue(leftToken.getLiteral().toString()));
                Bleft = l;
            } else {
                var l = Boolean.parseBoolean(leftToken.getLiteral().toString());
                Bleft = l;
            }
            if (rightvar == TokenType.INT || rightvar == TokenType.KW_INT) {
                Error("Error: can't compare INT to BOOL");

            } else if (rightvar == TokenType.BOOL || rightvar == TokenType.KW_BOOLEAN) {
                if (rightToken.getType() == TokenType.IDENTIFIER) {
                    var r = Boolean.parseBoolean(getVarValue(rightToken.getLiteral().toString()));
                    Bright = r;
                } else {
                    var r = Boolean.parseBoolean(rightToken.getLiteral().toString());
                    Bright = r;
                }
                switch (condition.getLexeme()) {
                    case "<":
                        Error("The operator < is undefined for the argument type(s) boolean, boolean");
                        break;
                    case "<=":
                        Error("The operator <= is undefined for the argument type(s) boolean, boolean");
                        break;
                    case ">":
                        Error("The operator > is undefined for the argument type(s) boolean, boolean");
                        break;
                    case ">=":
                        Error("The operator >= is undefined for the argument type(s) boolean, boolean");
                        break;
                    case "<>":
                        if (Bleft != Bright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "NOT":
                        if (Bleft != Bright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "=":
                        if (Bleft == Bright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                }
            } else if (rightvar == TokenType.FLOAT || rightvar == TokenType.KW_FLOAT) {
                Error("Error: Can't compare boolean to float");
            } else if (rightvar == TokenType.STRING || rightvar == TokenType.KW_STRING) {
                Error("Error: can't compare boolean to String");
            }
        } else if (leftvar == TokenType.FLOAT || leftvar == TokenType.KW_FLOAT) {
            if (leftToken.getType() == TokenType.IDENTIFIER) {
                var l = Float.parseFloat(getVarValue(leftToken.getLiteral().toString()));
                Fleft = l;
            } else {
                var l = Float.parseFloat(leftToken.getLiteral().toString());
                Fleft = l;
            }
            if (rightvar == TokenType.INT || rightvar == TokenType.KW_INT) {
                if (rightToken.getType() == TokenType.IDENTIFIER) {
                    var r = Integer.parseInt(getVarValue(rightToken.getLiteral().toString()));
                    Iright = r;
                } else {
                    var r = Integer.parseInt(rightToken.getLiteral().toString());
                    Iright = r;
                }
                switch (condition.getLexeme()) {
                    case "<":
                        if (Fleft < Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "<=":
                        if (Fleft <= Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case ">":
                        if (Fleft > Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case ">=":
                        if (Fleft >= Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "<>":
                        if (Fleft != Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "NOT":
                        if (Fleft != Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "=":
                        if (Fleft == Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                }
            } else if (rightvar == TokenType.BOOL || rightvar == TokenType.KW_BOOLEAN) {
                Error("Error: can't compare FLOAT to BOOL");
            } else if (rightvar == TokenType.FLOAT || rightvar == TokenType.KW_FLOAT) {
                if (rightToken.getType() == TokenType.IDENTIFIER) {
                    var r = Float.parseFloat(getVarValue(rightToken.getLiteral().toString()));
                    Fright = r;
                } else {
                    var r = Float.parseFloat(rightToken.getLiteral().toString());
                    Fright = r;
                }
                switch (condition.getLexeme()) {
                    case "<":
                        if (Fleft < Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "<=":
                        if (Fleft <= Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case ">":
                        if (Fleft > Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case ">=":
                        if (Fleft >= Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "<>":
                        if (Fleft != Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "NOT":
                        if (Fleft != Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "=":
                        if (Fleft == Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                }
            } else if (rightvar == TokenType.STRING || rightvar == TokenType.KW_STRING) {
                Error("Error: can't compare FLOAT to String");
            }
        } else if (leftvar == TokenType.STRING || leftvar == TokenType.KW_STRING) {
            if (leftToken.getType() == TokenType.IDENTIFIER) {
                var l = getVarValue(leftToken.getLiteral().toString());
                Sleft = l;
            } else {
                var l = getVarValue(leftToken.getLiteral().toString());
                Sleft = l;
            }
            if (rightvar == TokenType.INT || rightvar == TokenType.KW_INT) {
                Error("Error: can't compare STRING to INT");
            } else if (rightvar == TokenType.BOOL || rightvar == TokenType.KW_BOOLEAN) {
                Error("Error: can't compare STRING to BOOL");
            } else if (rightvar == TokenType.FLOAT || rightvar == TokenType.KW_FLOAT) {
                Error("Error: can't compare STRING to FLOAT");
            } else if (rightvar == TokenType.STRING || rightvar == TokenType.KW_STRING) {
                if (rightToken.getType() == TokenType.IDENTIFIER) {
                    var r = rightToken.getLiteral().toString();
                    Sright = r;
                } else {
                    var r = rightToken.getLiteral().toString();
                    Sright = r;
                }
                switch (condition.getLexeme()) {
                    case "<":
                        Error("The operator < is undefined for the argument type(s) String, String");
                        break;
                    case "<=":
                        Error("The operator <= is undefined for the argument type(s) String, String");
                        break;
                    case ">":
                        Error("The operator ? is undefined for the argument type(s) String, String");
                        break;
                    case ">=":
                        Error("The operator >= is undefined for the argument type(s) String, String");
                        break;
                    case "<>":
                        if (Sleft != Sright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "NOT":
                        if (Sleft != Sright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case "=":
                        if (Sleft == Sright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                }
            }
        }
        return check;
    }

    private String getVarValue(String s) {
        Map<String, Object> as = new HashMap<>();
        System.out.println("Created a new hashmap");
        as = interpreter.getValues();
        System.out.println("Finished getting Values");
        return as.get(s).toString();
    }

    public boolean LogicHandler2(boolean flag1, Token logic, boolean flag2) {
        boolean check = false;
        switch (logic.getLexeme()) {
            case "AND":
                if (flag1 && flag2) {
                    check = true;
                    break;
                }
            case "OR":
                if (flag1 || flag2) {
                    check = true;
                    break;
                }
            case "NOT":
                if (flag1 != flag2) {
                    check = true;
                    break;
                }
        }
        return check;
    }

    private boolean checkTokenType(TokenType left, TokenType right) {
        boolean check = false;
        if (left == right) {
            check = true;
            return check;
        }
        if (left == TokenType.BOOL && right == TokenType.STRING) {
            if (right == TokenType.BOOL && left == TokenType.STRING) {
                check = false;
            }
        }

        if (left == TokenType.FLOAT && right == TokenType.INT) {
            if (right == TokenType.FLOAT && left == TokenType.INT) {
                check = true;
            }
        }
        return check;
    }

}
