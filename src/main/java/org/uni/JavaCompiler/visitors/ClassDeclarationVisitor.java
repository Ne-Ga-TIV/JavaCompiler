package org.uni.JavaCompiler.visitors;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.ClassVisitor;

import java.io.FileOutputStream;
import java.io.IOException;

public class ClassDeclarationVisitor extends VoidVisitorAdapter<Void> {
    @Override
    public  void visit(ClassOrInterfaceDeclaration n, Void arg){
        super.visit(n, arg);
        String className = n.getNameAsString();

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM8, cw) {
        };

        cv.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, className, null, "java/lang/Object", null);

        // Генерируем конструктор
        MethodVisitor constructorMv = cv.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        constructorMv.visitCode();
        constructorMv.visitVarInsn(Opcodes.ALOAD, 0);
        constructorMv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        constructorMv.visitInsn(Opcodes.RETURN);
        constructorMv.visitMaxs(1, 1);
        constructorMv.visitEnd();
        n.accept(new MethodDeclarationVisitor(), cv);
        cv.visitEnd();
        byte[] bytecode = cw.toByteArray();

    }
}
