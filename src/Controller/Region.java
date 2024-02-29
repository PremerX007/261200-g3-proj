package Controller;

import UPBEAT.Position;

public class Region {
    private int row;
    private int col;
    private Player president;
    private double deposit;
    private double interest;
    private boolean cityCenter;

    public Region(int row, int col){
        this.row = row;
        this.col = col;
    }

    protected void initCityCenter(Player player, long init_dep) {
        this.cityCenter = true;
        this.president = player;
        deposit = init_dep;
    }

    private void caculateInterest(long t){
        long b = Territory.instance.getBaseInterest();
        this.interest = b*Math.log10(deposit)*Math.log(t);
    }

    protected long getInterest(){ // not clear
        caculateInterest(president.getPlayerTurn());
        return (long) this.interest;
    }

    protected void calculateDeposit(){
        caculateInterest(president.getPlayerTurn());
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
    }

    private void clearPresident() {
        this.president.deleteRegion(this);
        this.president = null;
    }

    protected void payDepositFromShoot(Player player, long cost){
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
            player.addNewRegion(this);
        }
        this.deposit = deposit + money;
    }

    protected void assignPresident(Player player) {
        this.president = player;
        player.addNewRegion(this);
    }
}
