package com.project.game.repo.src.Controller;

import com.project.game.repo.src.Parser.EvalError;
import com.project.game.repo.src.Parser.Statement.Statement;

import java.util.Map;

public record MoveCommand(int direction) implements Statement {
    @Override
    public void eval(Player player, Map<String, Long> bindings) throws EvalError, InterruptedException {
        player.getCrew().move(direction);
        Thread.sleep(500);
    }
}
