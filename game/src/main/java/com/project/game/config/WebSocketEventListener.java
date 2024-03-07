package com.project.game.config;

import com.project.game.chat.GameRoomController;
import com.project.game.chat.Message;
import com.project.game.chat.MessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messageSendingOperations;
    @EventListener
    public void disconnect(SessionDisconnectEvent event){
        SimpMessageHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        Message.removeUser(username);
        var chatMessage = Message.builder()
                .sender(username)
                .type(MessageType.LEAVE)
                .build();
        messageSendingOperations.convertAndSend("/topic/public", chatMessage);
        messageSendingOperations.convertAndSend("/app/command.group", chatMessage);
    }
}
