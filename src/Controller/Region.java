package Controller;

import UPBEAT.Position;

public class Region {
    private int row;
    private int col;
    private Player president;
    private double deposit;
    private double max_deposit;
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


    protected long getDeposit(){
        return (long) this.deposit;
    }
    protected Player getPresident() { return this.president; }
    protected void payBudget(long money){
        if((long) deposit == money) {
            this.deposit = 0;
            clearPresident();
        }else{
            this.deposit = deposit - money;
        }
    }

    protected boolean isCityCenter() {
        return cityCenter;
    }
}
