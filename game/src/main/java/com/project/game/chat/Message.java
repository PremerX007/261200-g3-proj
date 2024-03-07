package com.project.game.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Setter
public class Message {
    public static List<Message> user = new ArrayList<>();
    private String content;
    private String sender;
    private String timestamp;
    private MessageType type;
    private boolean isAdmin;
    public static int count = 0;
    public static void addCount(){
        count++;
    }
    public static void addUser(Message msg){
        user.add(msg);
    }
    public static void removeUser(String msg){
        user.removeIf(m -> m.getSender().equals(msg));
    }

    public static void reduceCount(){
        count--;
    }

}
