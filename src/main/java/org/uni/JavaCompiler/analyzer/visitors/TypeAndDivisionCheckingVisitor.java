package org.uni.JavaCompiler.analyzer.visitors;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.types.ResolvedType;

public class TypeAndDivisionCheckingVisitor extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(BinaryExpr n, Void arg) {

        super.visit(n, arg);

        if (n.isBinaryExpr()) {
            Expression rightExpr = n.getRight();
            if(n.getOperator() == BinaryExpr.Operator.DIVIDE) {
                if (!rightExpr.calculateResolvedType().isReferenceType() && rightExpr.toString().equals("0")) {
                    System.out.println("Error" + n.getBegin().get() + " Potential division by zero in expression '" + n + "'");
                }
            }
        }

        ResolvedType leftType = n.getLeft().calculateResolvedType();
        ResolvedType rightType = n.getRight().calculateResolvedType();

        if((!leftType.isNumericType() || !rightType.isNumericType()) && (!leftType.describe().equals("java.lang.String") || !rightType.describe().equals("java.lang.String"))) {
            System.out.println("Error" + n.getBegin().get() + " Non-numeric types used in arithmetic expression '" + n + "'");
        }

        if (leftType.describe().equals("java.lang.String") && rightType.describe().equals("java.lang.String") && (n.getOperator() != BinaryExpr.Operator.PLUS)) {
            System.out.println("Error" + n.getBegin().get() + " Non-correct operator used in arithmetic expression '" + n + "'");
        }
    }
}
