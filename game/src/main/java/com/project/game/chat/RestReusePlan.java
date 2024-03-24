package com.project.game.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class RestReusePlan {
    private String sender;
    private boolean reuse;
    private String status;
}
