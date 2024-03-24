package com.project.game.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.game.repo.src.Controller.Player;
import com.project.game.repo.src.Controller.Region;
import com.project.game.repo.src.Controller.Territory;
import com.project.game.repo.src.UPBEAT.Game;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameData {

    private List<PlayerRest> playerlist;
    private Region[][] territory;

    public GameData(){
        buildRestPlayerList(Game.instance.getPlayerList());
        this.territory = Territory.instance.getRegions();
    }

    public void buildRestPlayerList(List<Player> arr){
        this.playerlist = new ArrayList<>();
        for(Player player: arr){
            playerlist.add(PlayerRest.builder()
                    .name(player.getName())
                    .ownCity(player.getOwnCity().size())
                    .budget(player.getBudget())
                    .lose(!player.status())
                    .playerColor(player.getCitycolor())
                    .crewColor(player.getCrewColor())
                    .constInit(player.isConstInit())
                    .turnNum(player.getTurn_number())
                    .myTurn(player.isMyturn()).build());
        }
    }
}
