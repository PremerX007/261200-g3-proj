package com.project.game.rest;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class RegionRest {
    private String president;
    private long deposit;
    private boolean isCityCenter;
    private boolean isCrew;
}
