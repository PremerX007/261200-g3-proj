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

    public boolean checkCrewCanMove(Player player, Position pos) {
        try {
            if(regions[pos.i][pos.j].getPresident() == null) return true;
            return regions[pos.i][pos.j].getPresident().equals(player);
        }catch (ArrayIndexOutOfBoundsException | NullPointerException e){
            return false;
        }
    }

    public void relocateCitycenter(Player player, Position old_city, Position new_city) {
        if (regions[new_city.i][new_city.j].getPresident().equals(player)) {
            regions[old_city.i][old_city.j].clearCityCenter();
            regions[new_city.i][new_city.j].setCityCenter();
        } else {
            System.out.println("the current region does not belong to the player, the relocation fails");
        }
    }

    public void investRegions(Player player, Position pos, long money){  // ฺNot Completed
        if(canInvest(player,pos)){
            if(money <= max_dep){
                regions[pos.i][pos.j].investRegion(player, money);
                player.payCost(money);
            }else{
                regions[pos.i][pos.j].investRegion(player, max_dep);
                player.payCost(max_dep);
            }
        }
    }

    public void collectBudget(Player player, Position pos, long money){ // Not Completed
        Region region = regions[pos.i][pos.j];
        if(region.getDeposit() >= money){
            region.payBudget(money);
            player.addBudget(money);
        }
    }

    public long calculateMinDistance(Position old_city, Position new_city) {
        if(old_city.j == new_city.j || Math.abs(old_city.j - new_city.j) == 1){
            return Math.abs(old_city.i - new_city.i);
        } else if (old_city.i == new_city.i || Math.abs(old_city.i - new_city.i) == 1) {
            return Math.abs(old_city.j - new_city.j);
        } else {
            long pos_fork;
            long block = Math.abs(old_city.j - new_city.j);
            if(old_city.j%2==0){
                pos_fork = old_city.i + (long) Math.floor(block/2.0);
            }else{
                pos_fork = old_city.i + (long) Math.ceil(block/2.0);
            }
            return block + Math.abs(new_city.i - pos_fork);
        }
    }

    public void calculateRegionInterest(Position pos) {
        regions[pos.i][pos.j].calculateDeposit();
    }

    public long opponentCheck(Player player, Position pos){
        int bestPos = Integer.MAX_VALUE;

        for(int i=1; i<=6; i++){
            Position p = new Position(pos.i, pos.j);
            for (int counter = 1; counter < 10000; counter++){ // protect infinite loop
                p.nextPos(i);
                try {
                    Region target = regions[p.i][p.j];
                    if(target.getPresident() != null){
                        int targetPos = Integer.parseInt(Integer.toString(counter)+ i);
                        if(!target.getPresident().equals(player) && targetPos < bestPos) {
                            bestPos = targetPos;
                            break;
                        }
                    }
                }catch (ArrayIndexOutOfBoundsException | NullPointerException e){
                    break;
                }
            }
        }

        return (bestPos != Integer.MAX_VALUE ? bestPos : 0);
    }

    public long nearbyCheck(Player player, Position pos, int distance) {
        int result = 0;
        for (int counter = 1; counter < 10000; counter++){ // protect infinite loop
            pos.nextPos(distance);
            try {
                Region target = regions[pos.i][pos.j];
                if(target.getPresident() != null){
                    if(!target.getPresident().equals(player)) {
                        result = 100*counter+((int) Math.log10(target.getDeposit())+1);
                        break;
                    }
                }
            }catch (ArrayIndexOutOfBoundsException | NullPointerException e){
                break;
            }
        }

        return result;
    }

    public void shootRegion(Player player, Position targetPos, long attackMoney) {
        try {
            Region target = regions[targetPos.i][targetPos.j];
            if(target.getPresident() != null){
                player.payCost(attackMoney);
                target.payBudget(attackMoney);
                if(target.isCityCenter() && target.getDeposit() == 0){
                    player.playerLose();
                    player.playerDone();
                }
            }
            player.payCost(1);
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e){
            player.payCost(1);
        }
    }

}
