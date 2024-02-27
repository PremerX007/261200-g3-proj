package Controller;

import UPBEAT.Position;

import java.util.Map;

public class Crew {
    private final Player president;
    private Position citycenter;
    private int row;
    private int col;

    public Crew(Player player, Position init_pos){
        this.president = player;
        this.row = init_pos.i;
        this.col = init_pos.j;
        this.citycenter = init_pos;
    }

    protected long getRow(){
        return this.row;
    }

    protected long getCol(){
        return this.col;
    }

    protected void relocate(){
        long distance = Territory.instance.calculateMinDistance(citycenter, new Position(row,col));
        long cost = 5*distance+10;
        if(president.getBudget() >= cost){
            Territory.instance.relocateCitycenter(president, citycenter, new Position(row,col));
            this.citycenter = new Position(row,col);
            president.payCost(cost);
        }else{
            System.out.println("player does not have enough budget to relocate city center");
        }
        president.playerDone();
    }

}
