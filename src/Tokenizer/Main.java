package Tokenizer;

public class Main {
    public static void main(String[] args) throws Exception {
        ExprTokenizer exr = new ExprTokenizer();
        while (exr.hasNextToken()){
            System.out.println(exr.consume());
        }
    }
}
