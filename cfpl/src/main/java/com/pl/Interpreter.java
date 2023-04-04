package com.pl;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.pl.Nodes.*;
import com.pl.Statements.*;

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
        } else if (node instanceof WhileNode) {
            visitWhileNode((WhileNode) node);
        } else if (node instanceof IfELseStmt) {
            visitIfStmt((IfELseStmt) node);
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

    public void visitWhileNode(WhileNode wNode) {
        visit(wNode.getWhileStmtDeclaration());
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

    private void visitIfStmt(IfELseStmt IE) {
        System.out.println(IE.toString());
    }
}