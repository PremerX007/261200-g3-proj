package com.project.game.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class GameRoomController {

    private final SimpMessageSendingOperations messageSendingOperations;
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public Message addUser(Message message, SimpMessageHeaderAccessor headerAccessor){
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        Message.addUser(message);
        return message;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Message sendMessage(Message message){
        return message;
    }

    @MessageMapping("/command.group")
    public void group(Message message){
//        var chatMessage = Message.builder()
//                .sender(message.getSender())
//                .type(MessageType.LEAVE)
//                .build();

        for(Message u: Message.user){
            messageSendingOperations.convertAndSend("/topic/public/group", u);
        }
    }

}
