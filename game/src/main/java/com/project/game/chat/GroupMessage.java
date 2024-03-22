package com.project.game.chat;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class GroupMessage {
    private ArrayList<Message> user;
//    private boolean empty;
    private boolean isStart;
    private int readyPerson;
    public GroupMessage(){
        this.user = new ArrayList<>();
//        this.empty = true;
    }
    public void addMsg(Message msg){
        user.add(msg);
//        this.empty = false;
    }
}
