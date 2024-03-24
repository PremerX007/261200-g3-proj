package com.project.game.repo.src.Parser.Statement;

import com.project.game.repo.src.Parser.EvalError;
import com.project.game.repo.src.Parser.Expression.Expr;
import com.project.game.repo.src.Controller.Player;

import java.util.Map;

public record IfStatement (Expr ex, Statement then, Statement els) implements Statement {
    @Override
    public void eval(Player player, Map<String, Long> bindings) throws EvalError, InterruptedException {
        if(ex.eval(player, bindings) > 0 && !player.isDone()){
            then.eval(player, bindings);
        }else {
            els.eval(player, bindings);
        }
    }
}
