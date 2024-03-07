package com.project.game.config;

import com.project.game.chat.GameRoomController;
import com.project.game.chat.GroupMessage;
import com.project.game.chat.Message;
import com.project.game.chat.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.NoSuchElementException;
import java.util.Objects;

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
            if(Message.findUser(username).getAdmin()){
                Message.removeUser(username);
                try{
                    Message.user.getFirst().setAdmin(true);
                }catch (NoSuchElementException e){
                    log.info("Every player disconnect from game");
                }
            }else{
                Message.removeUser(username);
            }
        }catch (NullPointerException e){
            Message.user.clear();
            log.info("Set player list to empty");
        }

        var chatMessage = Message.builder()
                .sender(username)
                .type(MessageType.LEAVE)
                .build();
        messageSendingOperations.convertAndSend("/topic/public", chatMessage);
        GroupMessage g = new GroupMessage();
        for(Message u: Message.user){
            g.addMsg(u);
        }
        messageSendingOperations.convertAndSend("/topic/public/group", g);
    }
}
