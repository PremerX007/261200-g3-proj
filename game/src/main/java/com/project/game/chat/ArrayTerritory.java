package com.project.game.chat;

import com.project.game.repo.src.Controller.Region;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class ArrayTerritory {
    private Region[][] arr;
}
