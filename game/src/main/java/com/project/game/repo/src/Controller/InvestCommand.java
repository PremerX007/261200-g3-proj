package com.project.game.repo.src.Controller;

import com.project.game.repo.src.Parser.EvalError;
import com.project.game.repo.src.Parser.Expression.Expr;
import com.project.game.repo.src.Parser.Statement.Statement;

import java.util.Map;

public record InvestCommand(Expr ex) implements Statement {
    @Override
    public void eval(Player player, Map<String, Long> bindings) throws EvalError, InterruptedException {
        player.getCrew().invest(ex.eval(player, bindings));
        Thread.sleep(500);
    }
}
