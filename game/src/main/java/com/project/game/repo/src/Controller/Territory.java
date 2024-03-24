package com.project.game.repo.src.Controller;

import com.project.game.chat.GameService;
import com.project.game.repo.src.UPBEAT.Position;
import com.project.game.rest.CommandType;
import com.project.game.rest.GameState;
import com.project.game.rest.RegionRest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

public class Territory {
    public static Territory instance;
    private GameService gameService;

    public static Territory instance(int row, int col, long max_dep, long base_interest, GameService gm){
        if(instance == null){
            instance = new Territory(row, col, max_dep, base_interest, gm);
        }
        return instance;
    }

    @Getter
    private Region[][] regions;
    private int row;
    private int col;
    private long max_dep;
    private long base_interest;
    private Territory(int row, int col, long max_dep, long base_interest, GameService gm){
        this.row = row;
        this.col = col;
        this.max_dep = max_dep;
        this.regions = new Region[this.row +1][this.col +1];
        this.base_interest = base_interest;
        this.gameService = gm;
        createRegion();
    }

    private void createRegion() {
        for(int i = 1; i<= row; i++){
            for(int j = 1; j<= col; j++){
                regions[i][j] = new Region(i,j);
            }
        }
    }

    public long getBaseInterest() {
        return this.base_interest;
    }

    public long getMaxDeposit(){
        return this.max_dep;
    }

    public long getRegionInterest(Position pos) {
        return regions[pos.i][pos.j].getInterest();
    }

    private boolean canInvest(Player player, Position pos){
        if(regions[pos.i][pos.j].getPresident() == null) {
            for (int i = 1; i <= 6; i++) {
                Position tp = new Position(pos);
                tp.nextPos(i);
                try {
                    if (regions[tp.i][tp.j].getPresident().equals(player)) {
                        return true;
                    }
                } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
                    continue;
                }
            }
            return false;
        }else{
            return true;
        }

//        else if(regions[pos.i][pos.j].getPresident().equals(player)){
//            return true;
//        }
    }

    public long getRow(){
        return this.row;
    }
    public long getCol(){
        return this.col;
    }

    public void setStartPlayer(Player player, Position pos, long init_dep){
        Region reg = regions[pos.i][pos.j];
        reg.initCityCenter(player, init_dep);
        player.addNewRegion(reg);
    }

    public boolean checkCrewCanMove(Player player, Position pos) {
        try {
            if(regions[pos.i][pos.j].getPresident() == null) return true;
            return regions[pos.i][pos.j].getPresident().equals(player);
        }catch (ArrayIndexOutOfBoundsException | NullPointerException e){
            return false;
        }
    }

    public long checkDeposit(Player player, Position pos){
        Region reg = regions[pos.i][pos.j];
        if (reg.getPresident() == null) return -reg.getDeposit();
        else return reg.getDeposit();
    }

    public void relocateCitycenter(Player player, Position old_city, Position new_city) {
        if (regions[new_city.i][new_city.j].getPresident().equals(player)) {
            regions[old_city.i][old_city.j].clearCityCenter();
            regions[new_city.i][new_city.j].setCityCenter();
            player.getCrew().assignNewCityCenter(new_city);
            gameService.sendGameData();
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
            gameService.sendGameData();
        }
    }

    public void collectBudget(Player player, Position pos, long money){ // Not Completed
        Region region = regions[pos.i][pos.j];
        if(region.getDeposit() >= money){
            region.payDeposit(money);
            player.addBudget(money);
            gameService.sendGameData();
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
                player.payCost(attackMoney+1);
                target.payDepositFromShoot(attackMoney);
                gameService.sendGameData();
            }else{
                player.payCost(attackMoney+1);
            }
        } catch (ArrayIndexOutOfBoundsException e){
            player.payCost(attackMoney+1);
        }
    }

    protected void addPlayerRegion(Player player, Position pos) {
        regions[pos.i][pos.j].assignPresident(player);
    }

    public void setCrewStatus(Position old_pos, Position new_pos) {
        regions[old_pos.i][old_pos.j].clearCrewStatus();
        regions[new_pos.i][new_pos.j].setCrewStatus();
        gameService.sendGameData();
    }
}
