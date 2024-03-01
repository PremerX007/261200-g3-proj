package Controller;

import UPBEAT.Position;

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

    protected long getNowDeposit(){
        return Territory.instance.checkDeposit(president, new Position(row, col));
    }

    protected void assignNewCityCenter(Position pos){
        this.citycenter = pos;
    }

    protected void goBackCityCenter(){
        this.row = citycenter.i;
        this.col = citycenter.j;
    }

    protected void relocate(){
        long distance = Territory.instance.calculateMinDistance(citycenter, new Position(row,col));
        long cost = 5*distance+10;
        if(president.getBudget() >= cost){
            president.payCost(cost);
            Territory.instance.relocateCitycenter(president, citycenter, new Position(row,col));
        }else{
            System.out.println("player does not have enough budget to relocate city center");
        }
        president.playerDone();
    }

    protected void invest(long money){
        if(president.getBudget() >= money+1){
            Territory.instance.investRegions(president, new Position(row,col), money);
        }
        president.payCost(1);
    }

    protected void collect(long money){
        if(president.getBudget() >= 1){
            president.payCost(1);
            Territory.instance.collectBudget(president, new Position(row,col), money);
        }else{
            president.playerDone();
        }
    }

    protected void move(int direction){
        Position target = new Position(this.row,this.col);
        target.nextPos(direction);
        if(Territory.instance.checkCrewCanMove(president,target)){
            if(president.getBudget() >= 1){
                this.row = target.i;
                this.col = target.j;
                president.payCost(1);
            } else {
                System.out.println("player does not have enough budget to move");
                president.playerDone();
            }
        }
    }

    protected long opponentCheck(){
        return Territory.instance.opponentCheck(president, new Position(row, col));
    }

    protected long getInterest() {
        return Territory.instance.getRegionInterest(new Position(row, col));
    }

    protected long nearbyCheck(int distance) {
        return Territory.instance.nearbyCheck(president, new Position(row, col), distance);
    }

    protected void shoot(int direction, long attack_money){
        if(president.getBudget() >= attack_money+1){
            Position target = new Position(row,col);
            target.nextPos(direction);
            Territory.instance.shootRegion(president, target, attack_money);
        }
    }
}
