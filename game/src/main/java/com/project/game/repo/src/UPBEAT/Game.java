package com.project.game.repo.src.UPBEAT;

import com.project.game.chat.GameConfig;
import com.project.game.chat.GameService;
import com.project.game.chat.Message;
import com.project.game.chat.PlayerList;
import com.project.game.repo.src.Parser.EvalError;
import com.project.game.repo.src.Controller.Player;
import com.project.game.repo.src.Controller.Territory;

import com.project.game.repo.src.Tokenizer.LexicalError;
import com.project.game.repo.src.Tokenizer.SyntaxError;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.*;

@Controller
public class Game {
    public static Game instance;
    public static void instance(GameService gm) throws LexicalError, SyntaxError, EvalError, IOException {
        instance = new Game(gm);
    }

    private final Map<String, Long> config = new HashMap<>();
    private LinkedList<String> playerColor = new LinkedList<>();
    private LinkedList<String> crewColor = new LinkedList<>();
    @Getter
    private List<Player> playerList = new ArrayList<>(); //dev future
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
    private Player playerNowTurn;

    private final GameService template;

    @Autowired
    public Game(GameService template) throws LexicalError, SyntaxError, EvalError, IOException {
        this.row = GameConfig.userConfig.getM();
        this.col = GameConfig.userConfig.getN();

        this.init_plan_min = GameConfig.userConfig.getInit_plan_min();
        this.init_plan_sec = GameConfig.userConfig.getInit_plan_sec();
        this.plan_rev_min = GameConfig.userConfig.getPlan_rev_min();
        this.plan_rev_sec = GameConfig.userConfig.getPlan_rev_sec();

        this.max_dep = GameConfig.userConfig.getMax_dep();
        this.init_budget = GameConfig.userConfig.getInit_budget();
        this.init_center_dep = GameConfig.userConfig.getInit_center_dep();
        this.rev_cost = GameConfig.userConfig.getRev_cost();
        this.interest_pct = GameConfig.userConfig.getInterest_pct();
        this.template = template;
        this.playerColor.add("#00FFFF");
        this.playerColor.add("#000FFF");
        this.playerColor.add("#0D9237");
        this.playerColor.add("#E800FF");

        this.crewColor.add("#FDD200");
        this.crewColor.add("#FE0000");
        this.crewColor.add("#FC942F");
        this.crewColor.add("#449293");

        start();
    }

    public void start() throws LexicalError, SyntaxError, IOException, EvalError {
        Territory.instance(row, col, max_dep, interest_pct, template);
        for(Message msg: PlayerList.user){
            playerList.add(createPlayer(msg.getSender()));
        }
    }

    public boolean playerInitAll(){
        for(Player p: playerList){
            if(!p.isConstInit()){
                return false;
            }
        }
        return true;
    }

    private Player createPlayer(String name) throws LexicalError, SyntaxError, IOException {
        Position pos;
        do {
            pos = randomPos();
        } while (isPositionOccupied(pos));

        Player p = new Player(name, pos, init_budget, this.playerColor.poll(), this.crewColor.poll());
        Territory.instance.setStartPlayer(p, pos, init_center_dep);
        return p;
    }

    private boolean isPositionOccupied(Position pos) {
        for (Player p : this.playerList) {
            Position tmp;
            try {
                tmp = p.getPosition();
            } catch (NullPointerException e){
                continue;
            }
            if (pos.i == tmp.i && pos.j == tmp.j) {
                return true;
            }
        }
        return false;
    }

    private Position randomPos(){
        int i = (int) ((Math.random() * (row - 1)) + 1);
        int j = (int) ((Math.random() * (col - 1)) + 1);
        return new Position(i,j);
    }

    public Player findPlayer(String name){
        return this.playerList.stream()
                .filter(player -> name.equals(player.getName()))
                .findAny().orElse(null);
    }

    public Player nowTurn(){
        if (playerNowTurn == null){
            this.playerNowTurn = playerList.getFirst();
            this.playerNowTurn.setMyturn(true);
        }
        return this.playerNowTurn;
    }

    public void nextTurn(){
        int idx = playerList.indexOf(this.playerNowTurn);
        idx++;
        if(idx >= playerList.size()){
            idx = 0;
        }
        this.playerNowTurn.setMyturn(false);
        this.playerNowTurn = playerList.get(idx);
        this.playerNowTurn.setMyturn(true);
    }

}
