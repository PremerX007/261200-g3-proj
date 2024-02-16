package Parser;

import Parser.Statement.Statement;
import Tokenizer.LexicalError;
import Tokenizer.PlanTokenizer;
import Tokenizer.SyntaxError;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws LexicalError, IOException, SyntaxError, EvalError {
        PlanParser plan = new PlanParser(new PlanTokenizer("src/Parser/TestConstPlaintext/testcons.txt"));
        Map<String, Long> m = new HashMap<>();
        Statement s = plan.parse();
        s.eval(m);
        System.out.println(m);
    }
}
