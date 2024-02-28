import Parser.EvalError;
import Parser.PlanParser;
import Parser.Statement.Statement;
import Tokenizer.LexicalError;
import Tokenizer.PlanTokenizer;
import Tokenizer.SyntaxError;
import UPBEAT.Position;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class Main {
    public static void main(String[] args) throws LexicalError, IOException, SyntaxError, EvalError {
        String s =  "t = 5t = t + 1     # t = 1\n" +
                    "m = 0              # m = 0\n" +
                    "while (t) {        # t = 1\n" +
                    "    m = 35 + t     # m = 36\n" +
                    "    t = t - 1      # t = 0\n" +
                    "}";
        PlanParser plan = new PlanParser(new PlanTokenizer(new StringReader(s)));
        Statement statement = plan.parse();
        Map<String, Long> b = new HashMap<>();
        statement.eval(b);
        System.out.println(b);

    }
    public static int getRandomNumber(int min, int max) {
        return (int) (Math.random() * 1000);
    }
}