

public class IncorrectReturnTypeTest {
    public int foo1(){
        return "erw";
    }

    public String foo2(){
        int a  = 1;
        int b = 122;
        return a * b;
    }
}