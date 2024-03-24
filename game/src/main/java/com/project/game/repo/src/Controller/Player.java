package com.project.game.repo.src.Controller;

import com.project.game.repo.src.Parser.EvalError;
import com.project.game.repo.src.Parser.PlanParser;
import com.project.game.repo.src.Parser.Statement.Statement;
import com.project.game.repo.src.Tokenizer.LexicalError;
import com.project.game.repo.src.Tokenizer.PlanTokenizer;
import com.project.game.repo.src.Tokenizer.SyntaxError;
import com.project.game.repo.src.Tokenizer.Tokenizer;
import com.project.game.repo.src.UPBEAT.Position;
import lombok.Getter;
import lombok.Setter;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Player {
    private List<Region> ownCity = new ArrayList<>();
    private Map<String, Long> iden = new HashMap<>();
    private long budget;
    private Tokenizer tkz;
    private Statement statement;
    private boolean done;
    private boolean status = true;
    private Position position;
    private Crew crew;
    private String name;
    private String citycolor;
    private String crewColor;
    private int turn_number = 1;
    private boolean myturn;
    private boolean constInit;
    private boolean useOldStatement;

    public Player(String name, Position init_pos, long init_budget, String citycolor, String crewColor) throws LexicalError, IOException, SyntaxError {
        this.name = name;
        this.crew = new Crew(this,init_pos);
        this.budget = init_budget;
        this.citycolor = citycolor;
        this.crewColor = crewColor;
        this.position = init_pos;

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

    public void run() throws EvalError, InterruptedException {
        this.done = false;
        calculateMyRegionInterest();
        statement.eval(this,iden);
        crew.goBackCityCenter();
        this.turn_number = turn_number + 1;
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
        this.budget = budget-cost;
    }
    protected void addBudget(long money){
        this.budget = budget+money;
    }

    protected void addNewRegion(Region reg) {
        ownCity.add(reg);
    }
    protected void deleteRegion(Region reg){
        ownCity.remove(reg);
    }

}
