package org.uni.JavaCompiler.visitors;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import javassist.*;
import org.checkerframework.checker.units.qual.C;

public class ConstructorDeclarationVisitor extends VoidVisitorAdapter<CtClass> {

    @Override
    public void visit(ConstructorDeclaration n, CtClass ctClass){
        try {
            CtConstructor ctConstructor = CtNewConstructor.make(n.toString(), ctClass);
            ctClass.addConstructor(ctConstructor);
        } catch (CannotCompileException e) {
            throw new RuntimeException(e);
        }
        super.visit(n, ctClass);

    }
    public static String getType(Type type){

        String typeAsStirng = type.resolve().toDescriptor();

        if(typeAsStirng.equals("I"))
            return "int";
        if(typeAsStirng.equals("V"))
            return "void";
        if(typeAsStirng.startsWith("[")) {
            return typeAsStirng.replace('/', '.').substring(2, typeAsStirng.length() - 1) + "[]";
        }
        else return typeAsStirng.replace('/', '.').substring(1, typeAsStirng.length() - 1);
    }

    private CtClass[] getParameterTypes(ConstructorDeclaration n, CtClass ctClass) throws NotFoundException {
        return n.getParameters().stream()
                .map(parameter -> {
                    try {
                        return ctClass.getClassPool().get(getType(parameter.getType()));
                    } catch (NotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toArray(CtClass[]::new);
    }

}
