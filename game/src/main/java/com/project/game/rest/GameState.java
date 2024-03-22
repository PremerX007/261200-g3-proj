package com.project.game.rest;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Builder
@Getter
public class GameState {
    private CommandType command;
    private String nowturn;
}
