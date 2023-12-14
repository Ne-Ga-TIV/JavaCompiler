

public class CorrectReturnTypeTest {

    public final static StringBuilder s = new StringBuilder("23lkj432lk");
    public  int field2 = 5;

    public CorrectReturnTypeTest(int a, int b){
        field2 = 1 + b;
    }
    public  static int foo2(){
        int a  = 111111111;
        String b = s.toString();
        return 0;
    }
    public static void main(String[] args) {
        int value = 1;
        int c = value + 2;
        String stringValue = "ds32423";
        System.out.println(s);
    }

}