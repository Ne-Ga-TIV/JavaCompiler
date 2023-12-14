package org.uni.JavaCompiler.visitors;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import javassist.ClassPool;
import javassist.CtClass;

import javax.sound.sampled.AudioFileFormat;


public class ClassDeclarationVisitor extends VoidVisitorAdapter<String> {

    private StringBuilder s = new StringBuilder();

    @Override
    public void visit(ClassOrInterfaceDeclaration n, String arg){
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.makeClass(n.getNameAsString());
        try {

            new FieldDeclarationVisitor().visit(n, ctClass);
            new ConstructorDeclarationVisitor().visit(n, ctClass);
            new MethodDeclarationVisitor().visit(n, ctClass);

            ctClass.writeFile(arg);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
