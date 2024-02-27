package Controller;

import Parser.EvalError;
import Parser.PlanParser;
import Parser.Statement.Statement;
import Tokenizer.LexicalError;
import Tokenizer.PlanTokenizer;
import Tokenizer.SyntaxError;
import Tokenizer.Tokenizer;
import UPBEAT.Position;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {
    private List<Position> ownCity = new ArrayList<>();
    private Map<String, Long> iden = new HashMap<>();
    private long budget;
    private Tokenizer tkz;
    private Statement statement;
    private boolean done;
    private boolean status = true;
    private Crew crew;
    private String name;
    private long turn_number = 1;

    public Player(String name, Position init_pos, long init_budget) throws LexicalError, IOException, SyntaxError {
        this.name = name;
        this.crew = new Crew(this,init_pos);
        this.budget = init_budget;
        ownCity.add(init_pos);

    }

    public boolean status() {
        return this.status;
    }

    private void initCost() throws LexicalError, IOException, SyntaxError {
        // time for user to change or not change construction plan
        this.tkz = new PlanTokenizer(new FileReader("src/Parser/TestConstPlaintext/sampleCons.txt"));
        PlanParser plan = new PlanParser(this.tkz);
        this.statement = plan.parse();
        // operate something (plan correction?)
    }

    protected void playerDone(){
        this.done = true;
    }

    protected Crew getCrew(){
        return this.crew;
    }

    protected long getBudget(){
        return this.budget;
    }

    public boolean isDone(){
        return this.done;
    }

    public void run() throws EvalError, LexicalError, IOException, SyntaxError {
        initCost();
        this.done = false;
        statement.eval(this,iden);
        this.turn_number = iden.get("t") + 1;
    }




}
