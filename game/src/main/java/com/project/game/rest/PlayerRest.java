package com.project.game.rest;

import com.project.game.repo.src.Controller.Player;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class PlayerRest {
    private String name;
    private int ownCity;
    private long budget;
    private boolean lose;
    private String color;
    private boolean constInit;
    private boolean myTurn;
}
