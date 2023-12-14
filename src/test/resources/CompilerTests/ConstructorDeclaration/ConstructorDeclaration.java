public class ConstructorDeclaration {

    private int c;
    public ConstructorDeclaration(){

    }

    public ConstructorDeclaration(int a, int b){
        this.c = a + b;

    }

    public ConstructorDeclaration(String a, String b){
        System.out.println(a + b);
    }
}