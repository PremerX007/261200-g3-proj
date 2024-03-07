package com.project.game.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class GroupMessage {
    private ArrayList<Message> arr;
    private boolean empty;
    public GroupMessage(){
        this.arr = new ArrayList<>();
        this.empty = true;
    }
    public void addMsg(Message msg){
        arr.add(msg);
        this.empty = false;
    }
}
