package Tokenizer;

public class Main {
    public static void main(String[] args) throws Exception {
        ExprTokenizer exr = new ExprTokenizer("src/conp.txt");
        while (exr.hasNextToken()){
            System.out.println(exr.consume());
        }
    }
}
