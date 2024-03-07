package com.project.game.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class GameRoomController {

    private final SimpMessageSendingOperations messageSendingOperations;
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public Message addUser(Message message, SimpMessageHeaderAccessor headerAccessor){
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        if(Message.user.isEmpty()) message.setAdmin(true);
        Message.addUser(message);
        GroupMessage g = new GroupMessage();
        for(Message u: Message.user){
            g.addMsg(u);
        }
        messageSendingOperations.convertAndSend("/topic/public/group", g);
        log.info("Player \"" + message.getSender() + "\" connect to server");
        return message;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Message sendMessage(Message message){
        return message;
    }

    @GetMapping("/player")
    public GroupMessage returnPlayer(){
        GroupMessage g = new GroupMessage();
        for(Message u: Message.user){
            g.addMsg(u);
        }
        return g;
    }

}
