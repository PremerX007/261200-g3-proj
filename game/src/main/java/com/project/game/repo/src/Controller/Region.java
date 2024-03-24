package com.project.game.repo.src.Controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.game.repo.src.UPBEAT.Position;

public class Region {
    @JsonProperty("row")
    private int row;
    @JsonProperty("col")
    private int col;
    private Player president;
    @JsonProperty("president")
    private String president_name;
    @JsonProperty("deposit")
    private double deposit;
    @JsonProperty("interest")
    private double interest;
    @JsonProperty("cityCenter")
    private boolean cityCenter;
    @JsonProperty("crew")
    private boolean crew;

    public Region(int row, int col){
        this.row = row;
        this.col = col;
    }

    protected void initCityCenter(Player player, long init_dep) {
        this.cityCenter = true;
        this.crew = true;
        this.president = player;
        this.president_name = player.getName();
        deposit = init_dep;
    }

    private void calculateInterest(long t){
        long b = Territory.instance.getBaseInterest();
        this.interest = b*Math.log10(deposit)*Math.log(t);
    }

    protected long getInterest(){ // not clear
        calculateInterest(president.getPlayerTurn());
        return (long) this.interest;
    }

    protected void calculateDeposit(){
        calculateInterest(president.getPlayerTurn());
        this.deposit = (deposit + (deposit*interest/100));
    }

    protected long getDeposit(){
        return (long) this.deposit;
    }

    protected Position getPosition(){
        return new Position(row, col);
    }

    protected Player getPresident() { return this.president; }

    protected void clearPresidentLose(){
        this.president = null;
        this.president_name = null;
    }

    private void clearPresident() {
        this.president.deleteRegion(this);
        this.president = null;
        this.president_name = null;
    }

    protected void payDepositFromShoot(long cost){
        this.deposit = Math.max(0, deposit - cost);
        if(deposit < 1){
            if(cityCenter){
                president.playerLose();
            }else{
                clearPresident();
            }
        }
    }

    protected void payDeposit(long money){
        if((long) deposit == money) {
            this.deposit = 0;
            clearPresident();
        }else{
            this.deposit = deposit - money;
        }
    }

    protected void clearCityCenter(){
        this.cityCenter = false;
    }

    protected void setCityCenter(){
        this.cityCenter = true;
    }

    protected void investRegion(Player player, long money){
        if(president == null) {
            this.president = player;
            this.president_name = player.getName();
            player.addNewRegion(this);
        }
        this.deposit = deposit + money;
    }

    protected void assignPresident(Player player) {
        this.president = player;
        this.president_name = player.getName();
        player.addNewRegion(this);
    }

    protected void clearCrewStatus(){
        this.crew = false;
    }

    protected void setCrewStatus(){
        this.crew = true;
    }
}
