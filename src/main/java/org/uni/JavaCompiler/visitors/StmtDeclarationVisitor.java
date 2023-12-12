package org.uni.JavaCompiler.visitors;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.types.ResolvedType;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class StmtDeclarationVisitor extends VoidVisitorAdapter<MethodVisitor> {
    private static final Map<String, Integer> variables = new HashMap<>();
    @Override
    public void visit(MethodDeclaration methodDeclaration, MethodVisitor methodVisitor){

        methodVisitor.visitCode();
        BlockStmt methodBody = methodDeclaration.getBody().orElseThrow();
        for (Statement stmt : methodBody.getStatements()) {
            if(stmt.isExpressionStmt()) {
                Expression e = stmt.asExpressionStmt().getExpression();
                if(e.isVariableDeclarationExpr()){
                    variableDecl(e, methodVisitor, variables);
                }
            }

        }

        methodVisitor.visitMaxs(variables.size(), variables.size());
        super.visit(methodDeclaration, methodVisitor);
        methodVisitor.visitEnd();

    }

    private String returnType(String variable) {
        switch (variable) {
            case "int":
                return "I";
            case "long":
                return "J";
            case "float":
                return "F";
            case "double":
                return "D";
            case "byte":
                return "B";
            case "short":
                return "S";
            case "char":
                return "C";
            case "boolean":
                return "Z";
            default:
                return "L" + variable.replace('.', '/') + ";";
        }
    }

    private void variableDecl(Expression e, MethodVisitor methodVisitor, Map<String, Integer> variables){
        VariableDeclarationExpr varExp = e.asVariableDeclarationExpr();
        VariableDeclarator var = varExp.getVariables().get(0);
        variables.put(var.getNameAsString(), variables.size()+1);
        methodVisitor.visitLocalVariable(var.getNameAsString(),
                returnType(var.getType().toDescriptor()),
                null,
                new Label(),
                new Label(),
                variables.size());
        Expression exp = var.getInitializer().orElse(null);
        if(exp.isLiteralExpr()) {
            if ("int".equals(var.getTypeAsString()) || "byte".equals(var.getTypeAsString()) || "short".equals(var.getTypeAsString()) || "char".equals(var.getTypeAsString()) || "boolean".equals(var.getTypeAsString())) {
                methodVisitor.visitLdcInsn(Integer.valueOf(exp.asIntegerLiteralExpr().getValue()).intValue());
            } else if ("long".equals(var.getTypeAsString())) {
                methodVisitor.visitLdcInsn(Long.valueOf(exp.asLongLiteralExpr().getValue()).longValue());
            } else if ("float".equals(var.getTypeAsString())) {
                methodVisitor.visitLdcInsn(Float.valueOf(exp.asDoubleLiteralExpr().getValue()).floatValue());
            } else if ("double".equals(var.getTypeAsString())) {
                methodVisitor.visitLdcInsn(Double.valueOf(exp.asDoubleLiteralExpr().getValue()).doubleValue());

            } else if ("String".equals(var.getTypeAsString())) {
                methodVisitor.visitLdcInsn(exp.asStringLiteralExpr().getValue());

            }
            methodVisitor.visitVarInsn(Opcodes.ASTORE, variables.size());
        }
        else if(exp.isBinaryExpr()) {
            BinaryExpr biExp = exp.asBinaryExpr();
            switch(getEvent(biExp.getRight(), biExp.getLeft())) {
                case 1:
                    handleBothLiterals(biExp.getRight(), biExp.getLeft());
                    break;
                case 2:
                    handleLiteralAndVariable(biExp.getRight(), biExp.getLeft());
                    break;
                case 3:
                    handleVariableAndLiteral(biExp.getRight(), biExp.getLeft());
                    break;
                default:
                    handleTwoVariables(biExp.getRight(), biExp.getLeft(), methodVisitor);
                    break;
            }
            if(biExp.getRight().isLiteralExpr()){
                String type = getLiteralType(biExp.getRight().asLiteralExpr());
                if ("int".equals(type) || "byte".equals(var.getTypeAsString()) || "short".equals(var.getTypeAsString()) || "char".equals(var.getTypeAsString()) || "boolean".equals(var.getTypeAsString())) {
                } else if ("long".equals(type)) {
                } else if ("float".equals(type)) {
                } else if ("double".equals(type)) {
                } else if ("String".equals(type)) {

                }

            }

        }

    }
    private static void generateAdditionBytecode(String varType, MethodVisitor mv) {
        switch (varType) {
            case "int":
            case "byte":
            case "short":
            case "char":
                mv.visitInsn(Opcodes.IADD);
                break;
            case "long":
                mv.visitInsn(Opcodes.LADD);
                break;
            case "float":
                mv.visitInsn(Opcodes.FADD);
                break;
            case "double":
                mv.visitInsn(Opcodes.DADD);
                break;
            default:
                break;
        }
    }
    private static void handleTwoVariables(Expression right, Expression left, MethodVisitor mv) {
        String rightVarName = right.asNameExpr().getNameAsString();
        String leftVarName = left.asNameExpr().getNameAsString();


        loadVariableValue(rightVarName, getVariableType(right.asNameExpr()), mv);
        loadVariableValue(leftVarName,  getVariableType(left.asNameExpr()), mv);

        generateAdditionBytecode(getVariableType(right.asNameExpr()), mv);

    }

    private static void loadVariableValue(String varName, String varType, MethodVisitor mv) {
        switch (varType) {
            case "int":
            case "byte":
            case "short":
            case "char":
            case "boolean":
                mv.visitVarInsn(Opcodes.ILOAD, getVariableIndex(varName));
                break;
            case "long":
                mv.visitVarInsn(Opcodes.LLOAD, getVariableIndex(varName));
                break;
            case "float":
                mv.visitVarInsn(Opcodes.FLOAD, getVariableIndex(varName));
                break;
            case "double":
                mv.visitVarInsn(Opcodes.DLOAD, getVariableIndex(varName));
                break;
            default:
                mv.visitVarInsn(Opcodes.ALOAD, getVariableIndex(varName));
                break;
        }
    }
    private static int getVariableIndex(String varName) {
        return variables.get(varName);
    }
    private static Object add(Object left, Object right) {
        if (left instanceof Number && right instanceof Number) {
            if (left instanceof Integer || right instanceof Integer) {
                return ((Number) left).intValue() + ((Number) right).intValue();
            } else if (left instanceof Double || right instanceof Double) {
                return ((Number) left).doubleValue() + ((Number) right).doubleValue();
            } else {
                throw new UnsupportedOperationException("Unsupported number type in addition");
            }
        } else if (left instanceof String || right instanceof String) {
            return String.valueOf(left) + String.valueOf(right);
        } else {
            throw new UnsupportedOperationException("Unsupported types in addition");
        }
    }
    private static Object evaluateLiteralExpression(LiteralExpr literalExpr) {
        if (literalExpr.isIntegerLiteralExpr()) {
            return Integer.parseInt(literalExpr.asIntegerLiteralExpr().getValue());
        } else if (literalExpr.isDoubleLiteralExpr()) {
            return Double.parseDouble(literalExpr.asDoubleLiteralExpr().getValue());
        } else if (literalExpr.isStringLiteralExpr()) {
            return literalExpr.asStringLiteralExpr().getValue();
        } else {
            throw new UnsupportedOperationException("Unsupported literal expression type: " + literalExpr.getClass());
        }
    }
    private static int getEvent(Expression right, Expression left){
        if(right.isLiteralExpr() && left.isLiteralExpr())
            return 1;
        if(right.isLiteralExpr() && left.isNameExpr())
            return 2;
        if(right.isNameExpr() && left.isLiteralExpr())
            return 3;
        return 4;
    }
    private static String getLiteralType(LiteralExpr literalExpr) {
        if (literalExpr.isIntegerLiteralExpr()) {
            return "int";
        } else if (literalExpr.isDoubleLiteralExpr()) {
            return "double";
        } else if (literalExpr.isStringLiteralExpr()) {
            return "String";
        } else if (literalExpr.isBooleanLiteralExpr()) {
            return "boolean";
        } else {
            return "unknown";
        }
    }
    private static String getVariableType(NameExpr nameExpr) {
        ResolvedType resolvedType = nameExpr.calculateResolvedType();

        return resolvedType != null ? resolvedType.asReferenceType().getQualifiedName() : "unknown";
    }

    private static void handleBothLiterals(Expression right, Expression left) {
        if (right.isLiteralExpr() && left.isLiteralExpr()) {
        }
    }

    private static void handleLiteralAndVariable(Expression right, Expression left) {
        if (right.isLiteralExpr() && left.isNameExpr()) {
            String type = getLiteralType(right.asLiteralExpr());
            if ("int".equals(type) || "byte".equals(type) || "short".equals(type) || "char".equals(type) || "boolean".equals(type)) {
                handleLiteralOfType(right, type);
            } else if ("long".equals(type)) {
                handleLiteralOfType(right, type);
            } else if ("float".equals(type)) {
                handleLiteralOfType(right, type);
            } else if ("double".equals(type)) {
                handleLiteralOfType(right, type);
            } else if ("String".equals(type)) {
                handleLiteralOfType(right, type);
            }
        }
    }

    private static void handleVariableAndLiteral(Expression right, Expression left) {
        if (right.isNameExpr() && left.isLiteralExpr()) {
            String type = getLiteralType(left.asLiteralExpr());
            if ("int".equals(type) || "byte".equals(type) || "short".equals(type) || "char".equals(type) || "boolean".equals(type)) {
                handleLiteralOfType(left, type);
            } else if ("long".equals(type)) {
                handleLiteralOfType(left, type);
            } else if ("float".equals(type)) {
                handleLiteralOfType(left, type);
            } else if ("double".equals(type)) {
                handleLiteralOfType(left, type);
            } else if ("String".equals(type)) {
                handleLiteralOfType(left, type);
            }
        }
    }

    private static void handleLiteralOfType(Expression literalExpr, String type) {
        Object value = evaluateLiteralExpression(literalExpr.asLiteralExpr());
    }

    private static void handleLiteralExpression(LiteralExpr literalExpr, MethodVisitor mv) {
        if (literalExpr.isIntegerLiteralExpr()) {
            mv.visitLdcInsn(Integer.parseInt(literalExpr.asIntegerLiteralExpr().getValue()));
        } else if (literalExpr.isDoubleLiteralExpr()) {
            mv.visitLdcInsn(Double.parseDouble(literalExpr.asDoubleLiteralExpr().getValue()));
        } else if (literalExpr.isStringLiteralExpr()) {
            mv.visitLdcInsn(literalExpr.asStringLiteralExpr().getValue());
        }
    }


}
