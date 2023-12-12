package org.uni.JavaCompiler.visitors;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.objectweb.asm.*;

public class MethodDeclarationVisitor extends VoidVisitorAdapter<ClassVisitor> {
    @Override
    public void visit(MethodDeclaration methodDeclaration,  ClassVisitor classVisitor){
        int access = Opcodes.ACC_PUBLIC;

        // Обработка модификаторов доступа
        if (methodDeclaration.isPrivate()) {
            access = Opcodes.ACC_PRIVATE;
        } else if (methodDeclaration.isProtected()) {
            access = Opcodes.ACC_PROTECTED;
        }

        String methodName = methodDeclaration.getNameAsString();
        Type returnType = Type.getType(methodDeclaration.getType().toDescriptor());

       /* List<String> parameterTypes = methodDeclaration.getParameters()
                .stream()
                .map(t -> {
                    return t.getType().toDescriptor();
                }).toList();
        System.out.println(parameterTypes.toString());*/
        MethodVisitor methodVisitor = classVisitor.visitMethod(
                access,
                methodName,
                methodDeclaration.toDescriptor(),
                null, // ???
                null
        );

        methodVisitor.visitLocalVariable("variableName", "LType;", null, new Label(), new Label(), 0);
        methodVisitor.visitLocalVariable("intValue", "I", "intValue = 1", new Label(), new Label(), 1);
        methodVisitor.visitCode();

        methodVisitor.visitMaxs(1, 1);

        new StmtDeclarationVisitor().visit(methodDeclaration, methodVisitor);
        methodVisitor.visitEnd();

        super.visit(methodDeclaration, classVisitor);


    }
}
