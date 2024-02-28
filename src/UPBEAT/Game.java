package UPBEAT;

import Parser.EvalError;
import Parser.PlanParser;
import Parser.Statement.Statement;
import Controller.Player;
import Controller.Territory;

import Tokenizer.LexicalError;
import Tokenizer.PlanTokenizer;
import Tokenizer.SyntaxError;
import Tokenizer.Tokenizer;


import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Game {
    public static void main(String[] args) throws LexicalError, SyntaxError, EvalError, IOException {
        Game game = new Game();
        game.start();
    }

    private final Map<String, Long> config = new HashMap<>();
    private List<Player> playerList = new ArrayList<>(); //dev future
    private Territory territory;
    private long init_plan_min;
    private long init_plan_sec;
    private long plan_rev_min;
    private long plan_rev_sec;
    private int row;
    private int col;
    private long max_dep;
    private long init_budget;
    private long init_center_dep;
    private long rev_cost;
    private long interest_pct;


    public Game() throws LexicalError, SyntaxError, EvalError, IOException {
        Tokenizer tkz = new PlanTokenizer(new FileReader("src/config.txt"));
        PlanParser parseConfig = new PlanParser(tkz);
        Statement s = parseConfig.parse();
        s.eval(this.config);
        checkConfig();
        this.row = Math.toIntExact(config.get("m"));
        this.col = Math.toIntExact(config.get("n"));

        this.init_plan_min = config.get("init_plan_min");
        this.init_plan_sec = config.get("init_plan_sec");
        this.plan_rev_min = config.get("plan_rev_min");
        this.plan_rev_sec = config.get("plan_rev_sec");

        this.max_dep = config.get("max_dep");
        this.init_budget = config.get("init_budget");
        this.init_center_dep = config.get("init_center_dep");
        this.rev_cost = config.get("rev_cost");
        this.interest_pct = config.get("interest_pct");

    }

    private void checkConfig(){
        String[] s = {"m", "n", "max_dep", "init_budget", "plan_rev_sec", "init_plan_sec", "init_plan_min", "init_center_dep", "rev_cost", "plan_rev_min", "interest_pct"};
        for(String con: s){
            if (!config.containsKey(con)) {
                throw new NoSuchElementException("Config file dont have " + con + " value, please check your config file");
            }
        }
    }

    public void start() throws LexicalError, SyntaxError, IOException, EvalError {
        this.territory = Territory.instance(row, col, max_dep, interest_pct);
        // sandbox test
        playerList.add(createPlayer("Alex"));
        playerList.add(createPlayer("Bob"));

        while(true){
            for(Player player : playerList){
                if(!player.status()) return; // if player lose
                player.run();
            }
        }
    }

    private Player createPlayer(String name) throws LexicalError, SyntaxError, IOException {
        Position pos = randomPos();
        Player p = new Player(name, pos, init_budget);
        this.territory.setStartRegions(p,pos,init_center_dep);
        return p;
    }

    private Position randomPos(){
        int i = (int) ((Math.random() * (row - 1)) + 1);
        int j = (int) ((Math.random() * (col - 1)) + 1);
        return new Position(i,j);
    }

}
