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

}
