package org.uni.JavaCompiler.analyzer;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import org.uni.JavaCompiler.analyzer.visitors.*;

import java.io.*;

public class JavaCodeAnalyzer {
    private  static CompilationUnit cu;
    private final static PrintStream standardOut = System.out;
    public static ByteArrayOutputStream parse(String fileName) throws FileNotFoundException {
        ParserConfiguration parserConfiguration = new ParserConfiguration().setSymbolResolver(
                new JavaSymbolSolver(new ReflectionTypeSolver()));
        ByteArrayOutputStream  ErrorStr = new ByteArrayOutputStream();
        FileInputStream fileInputStream = new FileInputStream(fileName);
        StaticJavaParser.setConfiguration(parserConfiguration);
        cu = StaticJavaParser.parse(fileInputStream);
        System.setOut(new PrintStream(ErrorStr));
        new TypeCheckingVisitor().visit(cu, null);
        new TypeAndDivisionCheckingVisitor().visit(cu, null);
        new ReturnTypeCheckingVisitor().visit(cu, null);
        new NullCheckVisitor().visit(cu, null);
        System.setOut(standardOut);
        return  ErrorStr;
    }

}
