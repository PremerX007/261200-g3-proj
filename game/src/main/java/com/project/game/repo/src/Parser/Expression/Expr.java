package com.project.game.repo.src.Parser.Expression;

import com.project.game.repo.src.Controller.Player;
import com.project.game.repo.src.Parser.EvalError;

import java.util.Map;

public interface Expr {
    long eval(Player player, Map<String, Long> bindings) throws EvalError;
}
