package com.project.game.repo.src.Controller;

import com.project.game.repo.src.Parser.EvalError;
import com.project.game.repo.src.Parser.Statement.Statement;

import java.util.Map;

public record RelocateCommand() implements Statement {
    @Override
    public void eval(Player player, Map<String, Long> bindings) throws EvalError, InterruptedException {
        player.getCrew().relocate();
        Thread.sleep(300);
    }
}
