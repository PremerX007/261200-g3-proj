package com.project.game.repo.src.Parser.Statement;

import com.project.game.repo.src.Controller.Player;
import com.project.game.repo.src.Parser.EvalError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BlockStatement implements Statement {
    private List<Statement> q = new ArrayList<>();
    @Override
    public void eval(Player player, Map<String, Long> bindings) throws EvalError, InterruptedException {
        for(Statement s : q){
            if(player.isDone()) return;
            s.eval(player, bindings);
            if(player.isDone()) return;
        }
    }

    @Override
    public void append(Statement s){
        q.add(s);
    }
}
