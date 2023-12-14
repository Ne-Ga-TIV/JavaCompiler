package org.uni.JavaCompiler.visitors;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;

public class MethodDeclarationVisitor extends VoidVisitorAdapter<CtClass> {

    @Override
    public void visit(MethodDeclaration n, CtClass ctClass){

        super.visit(n, ctClass);
        String methodName = n.getNameAsString();
        Type returnType = n.getType();
        String methodBody = n.getBody().get().toString();
        try {
           CtMethod method = new CtMethod(ctClass.getClassPool().get(getType(returnType)), methodName, getParameterTypes(n, ctClass), ctClass);
           method.setBody(methodBody);
           method.setModifiers(getModificators(n));
           ctClass.addMethod(method);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(methodBody);


    }
    private CtClass[] getParameterTypes(MethodDeclaration n, CtClass ctClass) throws NotFoundException {
        return n.getParameters().stream()
                .map(parameter -> {
                    try {
                        System.out.println(parameter.getType().resolve().toDescriptor());
                        return ctClass.getClassPool().get(getType(parameter.getType()));
                    } catch (NotFoundException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .toArray(CtClass[]::new);
    }

    public static  int getModificators(MethodDeclaration n){
        return n.getModifiers().stream().mapToInt(mod -> {
            if(mod.toString().equals("public "))
                return Modifier.PUBLIC;
            if(mod.toString().equals("static "))
                return Modifier.STATIC;
            if(mod.toString().equals("private "))
                return Modifier.PRIVATE;
            if(mod.toString().equals("protected "))
                return Modifier.PROTECTED;
            return  0;
        }).sum();

    }
    public static String getType(Type type){
        String typeAsStirng = type.resolve().toDescriptor();
        if(typeAsStirng.equals("I"))
            return "int";
        if(typeAsStirng.equals("F"))
            return "float";
        if(typeAsStirng.equals("Z"))
            return "boolean";
        if(typeAsStirng.equals("D"))
            return "double";
        if(typeAsStirng.equals("V"))
            return "void";
        if(typeAsStirng.startsWith("[")) {
            return typeAsStirng.replace('/', '.').substring(2, typeAsStirng.length() - 1) + "[]";
        }
        else return typeAsStirng.replace('/', '.').substring(1, typeAsStirng.length() - 1);
    }
}
