package com.project.game.repo.src.Controller;

import com.project.game.repo.src.Parser.EvalError;
import com.project.game.repo.src.Parser.Expression.Expr;
import com.project.game.repo.src.Parser.Statement.Statement;

import java.util.Map;

public record ShootCommand(int direction, Expr expr) implements Statement {
    @Override
    public void eval(Player player, Map<String, Long> bindings) throws EvalError, InterruptedException {
        player.getCrew().shoot(direction, expr.eval(player, bindings));
        Thread.sleep(400);
    }
}
