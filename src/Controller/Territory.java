package Controller;

import UPBEAT.Position;

import java.util.List;

public class Territory {

    public static void main(String[] args) {
//        Territory t = new Territory(20,15,1000000);
//        System.out.println(t.calculateMinDistance(new Position(3,4), new Position(8,7)));
//        System.out.println(t.isHaveMyRegionNearby(null, new Position(4, 1)));
    }
    public static Territory instance;

    public static Territory instance(int row, int col, long max_dep, long base_interest){
        if(instance == null){
            instance = new Territory(row, col, max_dep, base_interest);
        }
        return instance;
    }

    private Region[][] regions;
    private int row;
    private int col;
    private long max_dep;
    private long base_interest;
    private Territory(int row, int col, long max_dep, long base_interest){
        this.row = row;
        this.col = col;
        this.max_dep = max_dep;
        this.regions = new Region[this.row +1][this.col +1];
        this.base_interest = base_interest;
        createRegion();
    }

    private void createRegion() {
        for(int i = 1; i<= row; i++){
            for(int j = 1; j<= col; j++){
                regions[i][j] = new Region(i,j);
            }
        }
    }


}
