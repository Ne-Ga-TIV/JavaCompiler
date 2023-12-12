import org.uni.JavaCompiler.JavaCompiler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class JavaCompilerTest {
    private String  ErrSteam;
    private final PrintStream stdOut = new PrintStream(System.out);
    private final String path = "src/test/resources/";

    @Test
    public void correctFile() throws IOException {

        ErrSteam = new String(JavaCompiler.getByteCode(path + "CorrectReturnTypeTest.java"), StandardCharsets.UTF_8);

        assertEquals("", ErrSteam);

    }
    @Test
    public void incorectFile() throws  IOException {

        ErrSteam = new String(JavaCompiler.getByteCode(path + "IncorrectInitializeTypeTest.java"), StandardCharsets.UTF_8);

        assertEquals("Error(line 7,col 9) Type mismatch in variable 'stringValue'. Expected type: String, but found type: PrimitiveTypeUsage{name='int'}\n"  +
                     "Error(line 9,col 9) Type mismatch in variable 's'. Expected type: StringBuilder, but found type: ReferenceType{java.lang.String, typeParametersMap=TypeParametersMap{nameToValue={}}}\n" ,
                      ErrSteam);
    }
}
