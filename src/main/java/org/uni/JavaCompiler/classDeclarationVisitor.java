package org.uni.JavaCompiler;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class classDeclarationVisitor extends VoidVisitorAdapter<Void> {
    @Override
    public  void visit(ClassOrInterfaceDeclaration n, Void arg){
        super.visit(n, arg);


    }
}
