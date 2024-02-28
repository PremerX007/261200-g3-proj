package Parser;

import Parser.Statement.Statement;
import Tokenizer.LexicalError;
import Tokenizer.PlanTokenizer;
import Tokenizer.SyntaxError;
import UPBEAT.Position;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class Main {
    public static void main(String[] args) throws LexicalError, IOException, SyntaxError, EvalError {
//        PlanParser plan = new PlanParser(new PlanTokenizer("src/Parser/TestConstPlaintext/testcons.txt"));
//        Map<String, Long> m = new HashMap<>();
//        Statement s = plan.parse();
//        s.eval(m);
//        System.out.println(m);
        List<int[]> pos = new LinkedList<>();
        pos.add(new int[]{5,4});
        pos.add(new int[]{4,3});
        System.out.println(pos);
//        pos.forEach(System.out::println);
        pos.remove(new int[]{5,4});
        System.out.println(pos);
//        pos.forEach(System.out::println);
    }
}
