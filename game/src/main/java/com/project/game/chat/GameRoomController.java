package com.project.game.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/game/config/reset")
    public GameConfig returnInitConfig(){
        return GameConfig.builder()
                .m(9)
                .n(9)
                .init_plan_min(5)
                .init_plan_sec(0)
                .init_budget(10000)
                .init_center_dep(100)
                .plan_rev_min(30)
                .plan_rev_sec(0)
                .rev_cost(100)
                .max_dep(1000000)
                .interest_pct(5)
                .build();
    }

    @PutMapping("/game/config/set")
    public GameConfig putPlayerConfig(@RequestBody GameConfig config){
        return GameConfig.userConfig = config;
    }

    @GetMapping("/game/config")
    public GameConfig returnUserConfig(){
        return GameConfig.userConfig;
    }

    @MessageMapping("/chat.status")
    public void status(Message message){
        Message tmp = Message.findUser(message.getSender());
        tmp.setType(message.getType());
        GroupMessage g = new GroupMessage();
        for(Message u: Message.user){
            g.addMsg(u);
        }
        messageSendingOperations.convertAndSend("/topic/public/group", g);
    }

}
