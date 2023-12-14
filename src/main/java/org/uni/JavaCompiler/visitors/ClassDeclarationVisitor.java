package org.uni.JavaCompiler.visitors;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import javassist.ClassPool;
import javassist.CtClass;


public class ClassDeclarationVisitor extends VoidVisitorAdapter<Void> {

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void arg){
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.makeClass(n.getNameAsString());
        try {
            new MethodDeclarationVisitor().visit(n, ctClass);

            ctClass.writeFile();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
