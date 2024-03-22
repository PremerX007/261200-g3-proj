package com.project.game.chat;

import com.project.game.rest.GameData;
import com.project.game.rest.GameState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    public GameService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendtoGroup(final GroupMessage groupMessage){
        messagingTemplate.convertAndSend("/topic/public/group", groupMessage);
    }

    public void sendGameData(){
        GameData gameData = new GameData();
        messagingTemplate.convertAndSend("/topic/public/game", gameData);
    }

    public void sendGameState(final GameState gameState){
        messagingTemplate.convertAndSend("/topic/public/game/state", gameState);
    }
}
