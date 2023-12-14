package org.uni.JavaCompiler.analyzer.visitors;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;

public class TypeCheckingVisitor extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(VariableDeclarationExpr n, Void arg) {
        super.visit(n, arg);

        for (VariableDeclarator variable : n.getVariables()) {
            Type declaredType = variable.getType();

            Expression initializer = variable.getInitializer().orElse(null);

            if (initializer != null) {
                ResolvedType assignedType = initializer.calculateResolvedType();
                if (!isAssignable(assignedType, declaredType)) {
                    System.out.println("Error" + declaredType.getBegin().get() + " Type mismatch in variable '" + variable.getNameAsString() +
                            "'. Expected type: " + declaredType + ", but found type: " + assignedType);
                }
            }
        }
    }
    public static boolean isAssignable(ResolvedType assignedType, Type declaredType) {

        if (assignedType.isAssignableBy(declaredType.resolve())) {
            return true;
        } else if (assignedType instanceof ResolvedReferenceType && declaredType instanceof com.github.javaparser.ast.type.ClassOrInterfaceType) {
            String assignedTypeName = assignedType.describe();
            String declaredTypeName = declaredType.toString();
            return assignedTypeName.equals(declaredTypeName);
        } else {
            return false;
        }
    }

}
