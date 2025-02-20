package com.project.game.config;

import com.project.game.chat.GroupMessage;
import com.project.game.chat.Message;
import com.project.game.chat.MessageType;
import com.project.game.chat.PlayerList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.NoSuchElementException;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messageSendingOperations;
    @EventListener
    public void disconnect(SessionDisconnectEvent event){
        SimpMessageHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        try {
            log.info("Player \"" + username + "\" disconnect from server");
            if(PlayerList.findUser(username).getAdmin()){
                PlayerList.removeUser(username);
                try{
                    PlayerList.user.getFirst().setAdmin(true);
                }catch (NoSuchElementException e){
                    log.info("Every player disconnect from game");
                }
            }else{
                PlayerList.removeUser(username);
            }
        }catch (NullPointerException e){
            PlayerList.user.clear();
            log.info("Set player list to empty");
        }

        var chatMessage = Message.builder()
                .sender(username)
                .type(MessageType.LEAVE)
                .build();
        messageSendingOperations.convertAndSend("/topic/public", chatMessage);
        GroupMessage g = new GroupMessage();
        for(Message u: PlayerList.user){
            g.addMsg(u);
        }
        messageSendingOperations.convertAndSend("/topic/public/group", g);
    }
}
