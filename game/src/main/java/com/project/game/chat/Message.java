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
    private String content;
    private String sender;
    private String timestamp;
    private MessageType type;
    private boolean admin;
    private boolean turn;
    public boolean getAdmin(){
        return this.admin;
    }

}
