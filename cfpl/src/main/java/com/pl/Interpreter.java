package com.pl;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

import javax.naming.directory.InvalidAttributeIdentifierException;

import com.pl.Nodes.*;
import com.pl.Statements.*;
import com.pl.Logic.*;

import static com.pl.TokenType.*;

public class Interpreter {

    private final Map<String, Object> values = new HashMap<>();
    private final Map<String, TokenType> types = new HashMap<>();
    public static boolean hadError = false;

    public Interpreter() {
    }

    public Object visit(Node node) {
        if (node == null) {
            return null;
        } else if (node instanceof ProgramNode) {
            visitProgramNode((ProgramNode) node);
        } else if (node instanceof VariableDeclarationNode) {
            visitVarDeclareNode((VariableDeclarationNode) node);
        } else if (node instanceof AssignStatement) {
            visitAssignStmt((AssignStatement) node);
        } else if (node instanceof OutputStatement) {
            visitOutputStmt((OutputStatement) node);
        } else if (node instanceof InputStatement) {
            visitInputStmt((InputStatement) node);
        } else if (node instanceof StringNode) {
            return visitStringNode((StringNode) node);
        } else if (node instanceof BinaryNode) {
            return visitBinaryNode((BinaryNode) node);
        } else if (node instanceof UnaryNode) {
            return visitUnaryNode((UnaryNode) node);
        } else if (node instanceof NumberNode) {
            return visitNumberNode((NumberNode) node);
        } else if (node instanceof WhileStatement) {
            visitWhileStmt((WhileStatement) node);
        } else if (node instanceof IfStatement) {
            visitIfStmt((IfStatement) node);
        } else if (node instanceof ElseStatement) {
            visitElseStmt((ElseStatement) node);
        } else {
            illegalVisit(node);
        }
        return null;

    }

    public void illegalVisit(Node node) {
        System.out.println("You have something that I can't interpret: " + node);
    }

    public void visitProgramNode(ProgramNode progNode) {
        visit(progNode.getVarDeclarations());
        visit(progNode.getStmtDeclaration());
    }

    public void visitVarDeclareNode(VariableDeclarationNode varDecNode) {
        String nodeTokenLexeme = varDecNode.getIdentifier().getLexeme();
        if (values.containsKey(nodeTokenLexeme)) {
            System.out.println(nodeTokenLexeme + " is in conflict with other declarations. Check program");
        }
        values.put(nodeTokenLexeme, varDecNode.getValue());
        types.put(nodeTokenLexeme, varDecNode.dataType().getType());
        if (varDecNode.getNext() != null) {
            visit(varDecNode.getNext());
        }
    }

    public void visitAssignStmt(AssignStatement assignStmt) {
        Object expr = visit(assignStmt.getRight());

        if (values.containsKey(assignStmt.getIdentifier().getLexeme())) {
            values.put(assignStmt.getIdentifier().getLexeme(), expr);
        } else {
            System.out.println("Identifier '" + assignStmt.getIdentifier().getLexeme() + "' does not exist.");
            hadError = true;
        }

        if (assignStmt.getNext() != null) {
            visit(assignStmt.getNext());
        }
    }

    public void visitIfStmt(IfStatement ifStmt) {
        ConditionStatement cond = ifStmt.getCondition();
        ConditionStatement cond2 = ifStmt.getCondition2();
        Token special = ifStmt.getSpecialType();
        System.out.println(ifStmt.toString());

        System.out.print("Testing Logic");
        if (ifStmt.getSpecialCheck() == true) {
            System.out.print("Logic1: " + LogicHandler(cond));
            System.out.print("Logic2: " + LogicHandler(cond2));
            System.out.print("Special: " + special);
            boolean logicflag2 = LogicHandler2(LogicHandler(cond), special, LogicHandler(cond2));

            if (logicflag2 == true) {
                System.out.println("Special Success\n");
            } else {
                System.out.println("Something went wrong\n");
            }
            System.out.println("Test Phase\n");
            if (logicflag2 == true) {
                visit(ifStmt.getStatement());
                // System.out.println("Logic1: " + LogicHandler(cond));
                // System.out.println("Logic2: " + LogicHandler(cond2));
                // System.out.println("Logicflag " + logicflag2);
            } else {
                visit(ifStmt.getNext());
                // System.out.println("Logic1: " + LogicHandler(cond));
                // System.out.println("Logic2: " + LogicHandler(cond2));
                // System.out.println("Logicflag " + logicflag2);
            }
        } else {
            if (LogicHandler(cond) == true) {
                System.out.println("Success\n");
            } else {
                System.out.println("Something went wrong\n");
            }
            System.out.println("Test Phase\n");
            if (LogicHandler(cond) == true) {
                visit(ifStmt.getStatement());
            } else {
                visit(ifStmt.getNext());
            }
        }
        System.out.println("Exiting IF");
    }

    public void visitElseStmt(ElseStatement ElseStmt) {
        visit(ElseStmt.getStatement());
    }

    public void visitWhileStmt(WhileStatement whileStmt) {

        Logic L = new Logic();
        ConditionStatement cond = whileStmt.getCondition();
        ConditionStatement cond2 = whileStmt.getCondition2();
        Token special = whileStmt.getSpecialType();
        int flag = whileStmt.getSpecialCheck();
        System.out.println("Before getting values");
        System.out.println(cond.toString());

        Boolean checker = values.isEmpty();
        System.out.println("Is Values Empty? : " + checker);

        System.out.println("After getting values");
        System.out.println(whileStmt.toString());

        System.out.println("Testing Logic");

        if (flag == 1) {
            System.out.println("\nCondition 1 Logic is: " + LogicHandler(cond) + "\n");
            System.out.println("\nCondition 2 Logic is: " + LogicHandler(cond2) + "\n");
            System.out.println("\nSpecial: " + special + "\n");
            boolean logicflag2 = LogicHandler2(LogicHandler(cond), special, LogicHandler(cond2));

            System.out.println("\nRetrieving Condition 1 logic: " + cond.getLogic());
            System.out.println("\nRetrieving Condition 2 logic: " + cond2.getLogic() + "\n");
            if (logicflag2 == true) {
                System.out.println("Success\n");
            } else {
                System.out.println("Something went wrong\n");
            }

            System.out.println("Test Phase\n");
            int x = 0;
            while (logicflag2 == true) {

                System.out.println("Condition 1 Logic is: " + LogicHandler(cond));
                System.out.println("Condition 2 Logic is: " + LogicHandler(cond2));
                System.out.println("\nSpecial Logic is: " + special);
                System.out.println("\nIteration is: " + x + "\n");
                visit(whileStmt.getStatement());
                logicflag2 = LogicHandler2(LogicHandler(cond), special, LogicHandler(cond2));
                x++;

                // System.out.println("Logic1: " + LogicHandler(cond));
                // System.out.println("Logic2: " + LogicHandler(cond2));
                // System.out.println("Logicflag " + logicflag2);
            }

        } else {
            if (LogicHandler(cond) == true) {
                System.out.println("Success\n");
            } else {
                System.out.println("Something went wrong\n");
            }

            System.out.println("Test Phase\n");
            while (LogicHandler(cond) == true) {
                visit(whileStmt.getStatement());
            }

            System.out.println("Exiting While");
        }

    }

    public void visitOutputStmt(OutputStatement outNode) {

        if (!outNode.getFlag() && !outNode.getSpecial()) {
            Object outputStmt = visit(outNode.getHeadConcat());
            System.out.println(outputStmt);
            Node currStrNode = outNode.getHeadConcat();
            while (currStrNode.getNext() != null) {
                System.out.println(visit(currStrNode.getNext()));
                currStrNode = currStrNode.getNext();
            }
        } else if (outNode.getFlag()) {
            ConditionStatement cd = outNode.gConditionStatement();
            // System.out.println(cd.toString());
            Logic lg = new Logic();
            if (cd.getVar1().getType().equals(IDENTIFIER)) {
                Token temp = new Token(types.get(cd.getVar1().getLexeme()),
                        values.get(cd.getVar1().getLexeme()).toString(),
                        values.get(cd.getVar1().getLiteral()).toString(), cd.getVar1().getLine());
                cd.setVar1(temp);
            }

            if (cd.getVar2().getType().equals(IDENTIFIER)) {
                Token temp = new Token(types.get(cd.getVar2().getLexeme()),
                        values.get(cd.getVar2().getLexeme()).toString(),
                        values.get(cd.getVar2().getLiteral()).toString(), cd.getVar2().getLine());
                cd.setVar2(temp);
            }
            boolean flag = lg.LogicHandler(cd);
            System.out.println(flag);
        }

        if (outNode.getSpecial()) {
            ConditionStatement cd = outNode.gConditionStatement();
            ConditionStatement cd2 = outNode.g2ConditionStatement();
            Logic lg = new Logic();
            if (cd.getVar1().getType().equals(IDENTIFIER)) {
                Token temp = new Token(types.get(cd.getVar1().getLexeme()),
                        values.get(cd.getVar1().getLexeme()).toString(),
                        values.get(cd.getVar1().getLiteral()).toString(), cd.getVar1().getLine());
                cd.setVar1(temp);
            }

            if (cd.getVar2().getType().equals(IDENTIFIER)) {
                Token temp = new Token(types.get(cd.getVar2().getLexeme()),
                        values.get(cd.getVar2().getLexeme()).toString(),
                        values.get(cd.getVar2().getLiteral()).toString(), cd.getVar2().getLine());
                cd.setVar2(temp);
            }

            if (cd2.getVar1().getType().equals(IDENTIFIER)) {
                Token temp = new Token(types.get(cd2.getVar1().getLexeme()),
                        values.get(cd2.getVar1().getLexeme()).toString(),
                        values.get(cd2.getVar1().getLiteral()).toString(), cd2.getVar1().getLine());
                cd2.setVar1(temp);
            }

            if (cd2.getVar2().getType().equals(IDENTIFIER)) {
                Token temp = new Token(types.get(cd2.getVar2().getLexeme()),
                        values.get(cd2.getVar2().getLexeme()).toString(),
                        values.get(cd2.getVar2().getLiteral()).toString(), cd2.getVar2().getLine());
                cd2.setVar2(temp);
            }
            boolean flag1, flag2, flag3;
            flag1 = lg.LogicHandler(cd);
            flag2 = lg.LogicHandler(cd2);
            flag3 = lg.LogicHandler2(flag1, outNode.getSpecialLogic(), flag2);
            System.out.println(flag3);
        }
    }

    public void visitInputStmt(InputStatement node) {
        Scanner sc = new Scanner(System.in);
        String ndrLexeme = node.getIden().getLexeme();
        Token ndrIden = node.getIden();
        String inp;
        String errMsg = "";
        if (values.containsKey(ndrLexeme)) {
            System.out.print("Enter value for " + ndrIden.getLexeme() + ": ");
            inp = sc.next();
            try {
                if (types.get(ndrLexeme) == KW_INT) {
                    values.put(ndrLexeme, Integer.parseInt(inp));
                } else if (types.get(ndrLexeme) == KW_FLOAT) {
                    values.put(ndrLexeme, Float.parseFloat(inp));
                } else if (types.get(ndrLexeme) == KW_CHAR) {
                    values.put(ndrLexeme, inp.charAt(0));
                } else if (types.get(ndrLexeme) == KW_BOOLEAN) {
                    if (inp.equalsIgnoreCase("true") || inp.equalsIgnoreCase("false")) {
                        values.put(ndrLexeme, Boolean.parseBoolean(inp));
                    } else {
                        throw new IllegalStatementException();
                    }
                }
            } catch (Exception e) {
                errMsg = "Incompatible datatypes for identifier " + ndrIden + " at line " + ndrIden.getLine();
                hadError = true;
            }
        } else {
            errMsg = "Identifier " + ndrIden + " at line " + ndrIden.getLine() + " has not been defined.";
            hadError = true;
        }

        if (hadError) {
            System.out.println(errMsg);
        }

        if (node.getNext() != null) {
            visit(node.getNext());
        }
    }

    public Object visitUnaryNode(UnaryNode unaryNode) {
        Object num = visit(unaryNode.getNum());
        if (unaryNode.getOperator().getType() == MINUS) {
            return -(int) num;
        } else {
            return num;
        }
    }

    public Object visitBinaryNode(BinaryNode binNode) {
        Object left = visit(binNode.getLeft());
        Object right = visit(binNode.getRight());
        Object output = null;

        if (binNode.getOperator().getType() == PLUS) {
            output = Float.parseFloat(left.toString()) + Float.parseFloat(right.toString());
        } else if (binNode.getOperator().getType() == MINUS) {
            output = Float.parseFloat(left.toString()) - Float.parseFloat(right.toString());
        } else if (binNode.getOperator().getType() == MULTIPLY) {
            output = Float.parseFloat(left.toString()) * Float.parseFloat(right.toString());
        } else if (binNode.getOperator().getType() == DIVIDE) {
            output = Float.parseFloat(left.toString()) / Float.parseFloat(right.toString());
        } else {
            System.out.println("Syntax Error: Expected an operator but got " + binNode.getOperator());
        }

        if (left instanceof Integer && right instanceof Integer) {
            output = (int) ((float) output);
        }

        return output;
    }

    public Object visitNumberNode(NumberNode numNode) {
        TokenType nodeType = numNode.getNum().getType();
        String nodeLexeme = numNode.getNum().getLexeme();

        Object literal;

        if (nodeType == IDENTIFIER) {
            literal = values.get(nodeLexeme);
        } else {
            literal = numNode.getNum().getLiteral();
        }
        return literal;
    }

    public String visitStringNode(StringNode strNode) {
        return strNode.getString().getLexeme();
    }

    public Map<String, Object> getValues() {
        return values;
    }

    private void Error(String err) {
        System.out.println(err);
    }

    private boolean LogicHandler(ConditionStatement cond) {
        int Ileft, Iright;
        String Sleft, Sright;
        Boolean Bleft, Bright;
        float Fleft, Fright;
        Character Cleft, Cright;

        boolean check = false;
        Token leftToken = cond.getVar1(), rightToken = cond.getVar2(), condition = cond.getLogic();
        TokenType leftvar = leftToken.getType(), rightvar = rightToken.getType();
        System.out.println("Left Lexemme: " + leftToken.getLexeme() + "\nLeft TokenType: " + leftvar
                + "\nRight Lexemme: " + rightToken.getLexeme() + "\nRight TokenType: " + rightvar);
        // Stupid method cause java var is shit
        if (leftToken.getType() == TokenType.INT || leftToken.getType() == TokenType.KW_INT
                || leftToken.getType() == TokenType.IDENTIFIER) {
            if (leftToken.getType() == TokenType.IDENTIFIER) {
                var l = Integer.parseInt(values.get(leftToken.getLiteral().toString()).toString());
                Ileft = l;
                System.out.println("\nLeft Token: " + Ileft);
            } else {
                var l = Integer.parseInt(leftToken.getLiteral().toString());
                Ileft = l;
                System.out.println("\nLeft Token: " + Ileft);
            }
            if (rightToken.getType() == TokenType.INT || rightToken.getType() == TokenType.KW_INT
                    || rightToken.getType() == TokenType.IDENTIFIER) {
                if (rightToken.getType() == TokenType.IDENTIFIER) {
                    var r = Integer.parseInt(values.get(rightToken.getLiteral().toString()).toString());
                    Iright = r;
                    System.out.println("Right Token: " + Iright);
                } else {
                    var r = Integer.parseInt(rightToken.getLiteral().toString());
                    Iright = r;
                    System.out.println("Right Token: " + Iright);
                }

                System.out.println("Condition Token: " + condition.getLexeme() + ("\n"));
                switch (condition.getType()) {
                    case LESS_THAN:
                        if (Ileft < Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case LESS_OR_EQUAL:
                        if (Ileft <= Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case GREATER_THAN:
                        if (Ileft > Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case GREATER_OR_EQUAL:
                        if (Ileft >= Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case NOT_EQUAL:
                        if (Ileft != Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case NOT:
                        if (Ileft != Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case EQUALS:
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
                    var r = Integer.parseInt(values.get(rightToken.getLiteral().toString()).toString());
                    Fright = r;
                } else {
                    var r = Integer.parseInt(rightToken.getLiteral().toString());
                    Fright = r;
                }
                switch (condition.getType()) {
                    case LESS_THAN:
                        if (Ileft < Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case LESS_OR_EQUAL:
                        if (Ileft <= Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case GREATER_THAN:
                        if (Ileft > Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case GREATER_OR_EQUAL:
                        if (Ileft >= Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case NOT_EQUAL:
                        if (Ileft != Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case NOT:
                        if (Ileft != Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case EQUALS:
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
                var l = Boolean.parseBoolean(values.get(leftToken.getLiteral().toString()).toString());
                Bleft = l;
            } else {
                var l = Boolean.parseBoolean(leftToken.getLiteral().toString());
                Bleft = l;
            }
            if (rightvar == TokenType.INT || rightvar == TokenType.KW_INT) {
                Error("Error: can't compare INT to BOOL");

            } else if (rightvar == TokenType.BOOL || rightvar == TokenType.KW_BOOLEAN) {
                if (rightToken.getType() == TokenType.IDENTIFIER) {
                    var r = Boolean.parseBoolean(values.get(rightToken.getLiteral().toString()).toString());
                    Bright = r;
                } else {
                    var r = Boolean.parseBoolean(rightToken.getLiteral().toString());
                    Bright = r;
                }
                switch (condition.getType()) {
                    case LESS_THAN:
                        Error("The operator < is undefined for the argument type(s) boolean, boolean");
                        break;
                    case LESS_OR_EQUAL:
                        Error("The operator <= is undefined for the argument type(s) boolean, boolean");
                        break;
                    case GREATER_THAN:
                        Error("The operator > is undefined for the argument type(s) boolean, boolean");
                        break;
                    case GREATER_OR_EQUAL:
                        Error("The operator >= is undefined for the argument type(s) boolean, boolean");
                        break;
                    case NOT_EQUAL:
                        if (Bleft != Bright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case NOT:
                        if (Bleft != Bright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case EQUALS:
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
                var l = Float.parseFloat(values.get(leftToken.getLiteral().toString()).toString());
                Fleft = l;
            } else {
                var l = Float.parseFloat(leftToken.getLiteral().toString());
                Fleft = l;
            }
            if (rightvar == TokenType.INT || rightvar == TokenType.KW_INT) {
                if (rightToken.getType() == TokenType.IDENTIFIER) {
                    var r = Integer.parseInt(values.get(rightToken.getLiteral().toString()).toString());
                    Iright = r;
                } else {
                    var r = Integer.parseInt(rightToken.getLiteral().toString());
                    Iright = r;
                }
                switch (condition.getType()) {
                    case LESS_THAN:
                        if (Fleft < Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case LESS_OR_EQUAL:
                        if (Fleft <= Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case GREATER_THAN:
                        if (Fleft > Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case GREATER_OR_EQUAL:
                        if (Fleft >= Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case NOT_EQUAL:
                        if (Fleft != Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case NOT:
                        if (Fleft != Iright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case EQUALS:
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
                    var r = Float.parseFloat(values.get(rightToken.getLiteral().toString()).toString());
                    Fright = r;
                } else {
                    var r = Float.parseFloat(rightToken.getLiteral().toString());
                    Fright = r;
                }
                switch (condition.getType()) {
                    case LESS_THAN:
                        if (Fleft < Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case LESS_OR_EQUAL:
                        if (Fleft <= Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case GREATER_THAN:
                        if (Fleft > Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case GREATER_OR_EQUAL:
                        if (Fleft >= Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case NOT_EQUAL:
                        if (Fleft != Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case NOT:
                        if (Fleft != Fright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case EQUALS:
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
                var l = values.get(leftToken.getLiteral().toString()).toString();
                Sleft = l;
            } else {
                var l = values.get(leftToken.getLiteral().toString()).toString();
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
                switch (condition.getType()) {
                    case LESS_THAN:
                        Error("The operator < is undefined for the argument type(s) String, String");
                        break;
                    case LESS_OR_EQUAL:
                        Error("The operator <= is undefined for the argument type(s) String, String");
                        break;
                    case GREATER_THAN:
                        Error("The operator > is undefined for the argument type(s) String, String");
                        break;
                    case GREATER_OR_EQUAL:
                        Error("The operator >= is undefined for the argument type(s) String, String");
                        break;
                    case NOT_EQUAL:
                        if (Sleft != Sright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case NOT:
                        if (Sleft != Sright) {
                            check = true;
                            break;
                        } else {
                            check = false;
                            break;
                        }
                    case EQUALS:
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

    public boolean LogicHandler2(boolean flag1, Token logic, boolean flag2) {
        boolean check = false;
        switch (logic.getLexeme()) {
            case "AND":
                if (flag1 && flag2) {
                    check = true;
                    System.out.println("AND IS TRUE \n");
                    break;
                } else {
                    check = false;
                    System.out.println("AND IS FALSE \n");
                    break;
                }
            case "OR":
                if (flag1 || flag2) {
                    check = true;
                    break;
                } else {
                    check = false;
                    break;
                }
            case "NOT":
                if (flag1 != flag2) {
                    check = true;
                    break;
                } else {
                    check = false;
                    break;
                }
        }
        return check;
    }
}