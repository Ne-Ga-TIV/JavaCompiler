package org.uni.JavaCompiler.analyzer.visitors;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.types.ResolvedType;

import java.util.ArrayList;
import java.util.List;

public class ReturnTypeCheckingVisitor extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(MethodDeclaration n, Void arg) {
        super.visit(n, arg);
        if(n.isMethodDeclaration()){

            Type declaredReturnType =  n.getType();
            if (n.getBody().isPresent()) {
                boolean returntAvailable = false;
                List<ReturnStmt> stmts = new ArrayList<>();
                for (Statement s : n.getBody().get().getStatements()) {
                    if (s.isReturnStmt()) {
                        returntAvailable = true;
                        ReturnStmt returnStmt = (ReturnStmt) s;
                        if(returnStmt.getExpression().isPresent()){
                            Expression returnExp  = returnStmt.getExpression().get();
                            ResolvedType actualReturnType = returnExp.calculateResolvedType();
                            if(!TypeCheckingVisitor.isAssignable(actualReturnType, declaredReturnType)) {
                                System.out.println("Error:"+declaredReturnType.getBegin().get() + " Return type mismatch in method '" + n.getNameAsString() +
                                        "'. Declared return type: " + declaredReturnType + ", but found type: " + actualReturnType.describe());
                            }

                        }
                    }
                }
                if(!declaredReturnType.isVoidType() && !returntAvailable){
                    System.out.println("Error:" + declaredReturnType.getBegin().get() +" Return type mismatch in method '" + n.getNameAsString() +
                            "'. Declared return type: " + declaredReturnType + ", but found type: " + "void");
                }
            }
        }
    }

}
