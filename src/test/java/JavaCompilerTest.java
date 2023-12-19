import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.uni.JavaCompiler.JavaCompiler;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class JavaCompilerTest {
    private byte[]  byteCode;
    private final PrintStream stdOut = new PrintStream(System.out);
    private final String path = "src\\test\\resources\\CompilerTests\\";

    @Test
    public void MethodDeclaration() throws IOException, NotFoundException, CannotCompileException {

       JavaCompiler.getByteCode(path + "MethodDeclaration\\MethodDeclaration.java");
    }
    @Test
    public void ClassDeclaration() throws IOException, NotFoundException, CannotCompileException {

        JavaCompiler.getByteCode(path + "ClassDeclaration\\ClassDeclaration.java");
    }
    @Test
    public void ConstructorDeclaration() throws IOException, NotFoundException, CannotCompileException {

        JavaCompiler.getByteCode(path + "ConstructorDeclaration\\ConstructorDeclaration.java");
    }
    @Test
    public void FieldsDeclaration() throws IOException, NotFoundException, CannotCompileException {

        JavaCompiler.getByteCode(path + "FieldsDeclaration\\FieldsDeclaration.java");
    }
    @Test
    public void HelloWorld() throws IOException, NotFoundException, CannotCompileException {

        JavaCompiler.getByteCode(path + "HelloWorld\\HelloWorld.java");
    }
}
