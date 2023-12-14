package org.uni.JavaCompiler.visitors;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import javassist.*;

public class FieldDeclarationVisitor extends VoidVisitorAdapter<CtClass> {

    @Override
    public void visit(FieldDeclaration n, CtClass ctClass){
            n.getVariables().forEach(variableDeclarator -> {
                try {
                    CtField ctField = new CtField(ctClass.getClassPool().get(getType(variableDeclarator.getType())), variableDeclarator.getNameAsString(), ctClass);
                    ctField.setModifiers(getModificators(n));
                    if(variableDeclarator.getInitializer().isPresent())
                        ctClass.addField(ctField, CtField.Initializer.byExpr(variableDeclarator.getInitializer().get().toString()));
                    else
                        ctClass.addField(ctField);
                } catch (CannotCompileException | NotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
    }
    public static  int getModificators(FieldDeclaration n){

        return n.getModifiers().stream().mapToInt(mod -> {
            if(mod.toString().equals("public "))
                return Modifier.PUBLIC;
            if(mod.toString().equals("static "))
                return Modifier.STATIC;
            if(mod.toString().equals("private "))
                return Modifier.PRIVATE;
            if(mod.toString().equals("protected "))
                return Modifier.PROTECTED;
            if(mod.toString().equals("final "))
                return Modifier.FINAL;
            return  0;
        }).sum();

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
}
