import org.uni.JavaCompiler.JavaCompiler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.PublicKey;

import static org.junit.Assert.assertEquals;

public class JavaCompilerTest {
    private ByteArrayOutputStream ErrSteam = new ByteArrayOutputStream();
    private final PrintStream stdOut = new PrintStream(System.out);
    private final String path = "src/test/resources/";
    @Before
    public void setErrSteam(){
        System.setOut(new PrintStream(ErrSteam));
    }
    @After
    public  void restoreSteam(){
        System.setOut(stdOut);
    }

    @Test
    public void correctFile() throws IOException {

        JavaCompiler.getByteCode(path + "CorrectInitializeTypeTest.java");

        assertEquals("", ErrSteam.toString());

    }
    @Test
    public void incorectFile() throws  IOException {

        JavaCompiler.getByteCode(path + "IncorrectInitializeTypeTest.java");

        assertEquals("File compile failed!" + System.lineSeparator() +
                     "Error(line 7,col 9) Type mismatch in variable 'stringValue'. Expected type: String, but found type: PrimitiveTypeUsage{name='int'}" + System.lineSeparator() +
                     "Error(line 9,col 9) Type mismatch in variable 's'. Expected type: StringBuilder, but found type: ReferenceType{java.lang.String, typeParametersMap=TypeParametersMap{nameToValue={}}}" + System.lineSeparator(),
                ErrSteam.toString());
    }
}
