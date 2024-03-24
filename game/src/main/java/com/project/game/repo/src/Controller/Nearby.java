package com.project.game.repo.src.Controller;

import com.project.game.repo.src.Parser.EvalError;
import com.project.game.repo.src.Parser.Expression.Expr;

import java.util.Map;

public record Nearby(int direction) implements Expr {
    @Override
    public long eval(Player player, Map<String, Long> bindings) throws EvalError {
        return player.getCrew().nearbyCheck(direction);
    }
}
