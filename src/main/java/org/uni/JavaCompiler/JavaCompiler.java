package org.uni.JavaCompiler;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import org.uni.JavaCompiler.analyzer.JavaCodeAnalyzer;
import org.uni.JavaCompiler.visitors.ClassDeclarationVisitor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class JavaCompiler {

    public static byte[] getByteCode(String fileName) throws IOException, NotFoundException, CannotCompileException {
        ParserConfiguration parserConfiguration = new ParserConfiguration().setSymbolResolver(
                new JavaSymbolSolver(new ReflectionTypeSolver()));

        FileInputStream fileInputStream = new FileInputStream(fileName);
        StaticJavaParser.setConfiguration(parserConfiguration);
        CompilationUnit cu = StaticJavaParser.parse(fileInputStream);
        ByteArrayOutputStream errorOut = JavaCodeAnalyzer.parse(fileName);
        if(!errorOut.toString().isEmpty()) {
            System.out.println(errorOut);
        }
        new ClassDeclarationVisitor().visit(cu,  fileName.substring(0, fileName.lastIndexOf("\\")));

        return errorOut.toByteArray();
    }




}
