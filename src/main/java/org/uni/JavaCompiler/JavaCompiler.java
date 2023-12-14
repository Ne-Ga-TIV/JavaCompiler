package org.uni.JavaCompiler;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import org.uni.JavaCompiler.analyzer.JavaCodeAnalyzer;
import javassist.*;
import org.uni.JavaCompiler.visitors.ClassDeclarationVisitor;

import java.io.*;

public class JavaCompiler {

    private  static CompilationUnit cu;
    private static ByteArrayOutputStream ErrorOut;
    public static byte[] getByteCode(String fileName) throws IOException {
        ParserConfiguration parserConfiguration = new ParserConfiguration().setSymbolResolver(
                new JavaSymbolSolver(new ReflectionTypeSolver()));

        FileInputStream fileInputStream = new FileInputStream(fileName);
        StaticJavaParser.setConfiguration(parserConfiguration);
        cu = StaticJavaParser.parse(fileInputStream);
        ErrorOut = JavaCodeAnalyzer.parse(fileName);
        if(!ErrorOut.toString().isEmpty())
            return ErrorOut.toByteArray();

        new ClassDeclarationVisitor().visit(cu, null);
        return ErrorOut.toByteArray();
    }

    public static void createClass(String className) {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.makeClass(className);

        try {

            ctClass.writeFile();
        } catch (CannotCompileException | NotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
}
