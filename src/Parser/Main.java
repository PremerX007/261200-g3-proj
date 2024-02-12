package Parser;

import Tokenizer.LexicalError;
import Tokenizer.PlanTokenizer;
import Tokenizer.SyntaxError;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws LexicalError, IOException, SyntaxError, EvalError {
        PlanParser plan = new PlanParser(new PlanTokenizer("src/Parser/TestConstPlaintext/testcons.txt"));
        plan.parse();
        System.out.println(plan.identifier);
    }
}
