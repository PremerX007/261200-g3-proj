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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {
    private List<Region> ownCity = new ArrayList<>();
    private Map<String, Long> iden = new HashMap<>();
    private BigDecimal budget;
    private Tokenizer tkz;
    private Statement statement;
    private boolean done;
    private boolean status = true;
    private Crew crew;
    private String name;
    private long turn_number = 1;

    public Player(String name, Position init_pos, long init_budget, long init_center_dep) throws LexicalError, IOException, SyntaxError {
        this.name = name;
        this.crew = new Crew(this,init_pos);
        this.budget = new BigDecimal(init_budget);
        Territory.instance.setStartPlayer(this,init_pos,init_center_dep);
    }

    public boolean status() {
        return this.status;
    }

    protected void playerLose(){
        this.status = false;
        for (Region reg : ownCity){
            reg.clearPresidentLose();
        }
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
        return this.budget.longValue();
    }

    public boolean isDone(){
        return this.done;
    }

    public void run() throws EvalError, LexicalError, IOException, SyntaxError {
        initCost();
        this.done = false;
        calculateMyRegionInterest();
        statement.eval(this,iden);
        crew.backHome();
        this.turn_number = iden.get("t") + 1;
    }

    private void calculateMyRegionInterest() {
        for(Region reg : ownCity){
            reg.calculateDeposit();
        }
    }

    protected long getPlayerTurn(){
        return this.turn_number;
    }

    protected void payCost(long cost){
        this.budget = budget.subtract(new BigDecimal(cost));
    }
    protected void addBudget(long money){
        this.budget = budget.add(new BigDecimal(money));
    }

    protected void addNewRegion(Region reg) {
        ownCity.add(reg);
    }
    protected void deleteRegion(Region reg){
        ownCity.remove(reg);
    }



}
