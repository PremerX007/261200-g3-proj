package Tokenizer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws LexicalError, IOException, SyntaxError {
        PlanTokenizer exr = new PlanTokenizer("src/Tokenizer/TestTokenPlaintext/sampleCons.txt");
        while (exr.hasNextToken()){
            System.out.println(exr.consume());
        }
    }
}
