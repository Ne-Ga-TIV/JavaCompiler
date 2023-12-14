import org.uni.JavaCompiler.analyzer.JavaCodeAnalyzer;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

public class JavaCodeAnalyzerTest {

    private String path = "src/test/resources/";

    @Test
    public void CorrectInitializeTypeTest() throws FileNotFoundException {
        ByteArrayOutputStream realErr;

        realErr = JavaCodeAnalyzer.parse(path + "SemanticTests/CorrectInitializeTypeTest.java");
        assertEquals("", realErr.toString());
        realErr.reset();
    }

    @Test
    public void IncorrectInitializeTypeTest() throws FileNotFoundException {
        ByteArrayOutputStream realErr;

        realErr = JavaCodeAnalyzer.parse(path + "SemanticTests/IncorrectInitializeTypeTest.java");
        assertEquals("Error(line 7,col 9) Type mismatch in variable 'stringValue'. Expected type: String, but found type: PrimitiveTypeUsage{name='int'}" + System.lineSeparator() +
                "Error(line 9,col 9) Type mismatch in variable 's'. Expected type: StringBuilder, but found type: ReferenceType{java.lang.String, typeParametersMap=TypeParametersMap{nameToValue={}}}" + System.lineSeparator(),
                realErr.toString());

        realErr.reset();
    }

    @Test
    public void CorrectDivisionTest() throws FileNotFoundException {
        ByteArrayOutputStream realErr;

        realErr = JavaCodeAnalyzer.parse(path + "SemanticTests/CorrectDivisionTest.java");

        assertEquals("", realErr.toString());
        realErr.reset();
    }

    @Test
    public void IncorrectDivisionTest() throws FileNotFoundException {
        ByteArrayOutputStream realErr;

        realErr = JavaCodeAnalyzer.parse(path + "SemanticTests/IncorrectDivisionTest.java");

        assertEquals("Error(line 6,col 17) Potential division by zero in expression '12 / 0'" + System.lineSeparator() +
                        "Error(line 8,col 18) Non-numeric types used in arithmetic expression '10 / b'" + System.lineSeparator(),
                realErr.toString());


        realErr.reset();
    }

    @Test
    public void IncorrectReturnTypeTypeTest() throws FileNotFoundException {
        ByteArrayOutputStream realErr;

        realErr = JavaCodeAnalyzer.parse(path + "SemanticTests/IncorrectReturnTypeTest.java");

        assertEquals("Error:(line 4,col 12) Return type mismatch in method 'foo1'. Declared return type: int, but found type: java.lang.String"+ System.lineSeparator()  +
                     "Error:(line 8,col 12) Return type mismatch in method 'foo2'. Declared return type: String, but found type: int" + System.lineSeparator(),
                realErr.toString());
        realErr.reset();
    }
    @Test
    public void CorrectReturnTypeTypeTest() throws FileNotFoundException {
        ByteArrayOutputStream realErr;
        realErr = JavaCodeAnalyzer.parse(path + "SemanticTests/CorrectReturnTypeTest.java");
        assertEquals("",  realErr.toString());
        realErr.reset();
    }

    @Test
    public void IncorrectNullCheck() throws FileNotFoundException {
        ByteArrayOutputStream realErr;

        realErr = JavaCodeAnalyzer.parse(path + "SemanticTests/NullCheckTest.java");

        assertEquals("Warning: variable \"b\" may be null" + System.lineSeparator()  +
                "Warning: variable \"str\" may be null" + System.lineSeparator(), realErr.toString());
        realErr.reset();
    }
}
