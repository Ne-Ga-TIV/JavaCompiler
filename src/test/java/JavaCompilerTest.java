import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.uni.JavaCompiler.JavaCompiler;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class JavaCompilerTest {
    private byte[]  byteCode;
    private final PrintStream stdOut = new PrintStream(System.out);
    private final String path = "src/test/resources/CompilerTests/";

    @Test
    public void correctFile() throws IOException, NotFoundException, CannotCompileException {

       JavaCompiler.getByteCode(path + "MethodDeclaration/MethodDeclaration.java");
    }
}
