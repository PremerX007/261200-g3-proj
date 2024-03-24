package com.project.game.repo.src;

import com.project.game.repo.src.Parser.EvalError;
import com.project.game.repo.src.Parser.PlanParser;
import com.project.game.repo.src.Parser.Statement.Statement;
import com.project.game.repo.src.Tokenizer.LexicalError;
import com.project.game.repo.src.Tokenizer.PlanTokenizer;
import com.project.game.repo.src.Tokenizer.SyntaxError;
import com.project.game.repo.src.UPBEAT.Position;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class Main {
    public static void main(String[] args) throws LexicalError, IOException, SyntaxError, EvalError, InterruptedException {
        String s =  "t = t + 1     # t = 1\n" +
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

//        Position pos = new Position(2,3);
//        pos.nextPos(1);
//        System.out.println("["+pos.i+","+pos.j+"]");
//        pos = new Position(2,3);

        for(int i=1; i<=6; i++){
            Position pos = new Position(2,3);
            pos.nextPos(i);
            System.out.println("["+pos.i+","+pos.j+"]");
        }

    }
    public static int getRandomNumber(int min, int max) {
        return (int) (Math.random() * 1000);
    }
}