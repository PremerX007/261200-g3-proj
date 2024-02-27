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



    protected boolean isCityCenter() {
        return cityCenter;
    }
}
