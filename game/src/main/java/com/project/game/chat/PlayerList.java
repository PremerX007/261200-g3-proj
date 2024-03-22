package com.project.game.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Setter
public class PlayerList {
    public static List<Message> user = new ArrayList<>();
    public static void addUser(Message msg){
        user.add(msg);
    }
    public static void removeUser(String msg){
        user.removeIf(m -> m.getSender().equals(msg));
    }
    public static Message findUser(String us) {
        return user.stream().filter(message -> us.equals(message.getSender()))
                .findAny().orElse(null);
    }
}
