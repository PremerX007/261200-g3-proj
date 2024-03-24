package com.project.game.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class RestConstPlan {
    private String sender;
    private String plan;
    private String time_min;
    private String time_sec;
}
