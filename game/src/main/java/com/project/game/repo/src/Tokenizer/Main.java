package com.project.game.repo.src.Tokenizer;

import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws LexicalError, IOException, SyntaxError {
        PlanTokenizer exr = new PlanTokenizer(new FileReader("src/Tokenizer/TestTokenPlaintext/unknownChar.txt"));
        while (exr.hasNextToken()){
            System.out.println(exr.consume());
        }
    }
}
