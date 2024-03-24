package com.project.game.repo.src.Parser.Statement;

import com.project.game.repo.src.Parser.EvalError;
import com.project.game.repo.src.Parser.Expression.Expr;
import com.project.game.repo.src.Controller.Player;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class WhileStatement implements Statement {
    private Expr expr;
    private List<Statement> q = new ArrayList<>();
    public WhileStatement(Expr expr){
        this.expr = expr;
    }

    @Override
    public void eval(Player player, Map<String, Long> bindings) throws EvalError, InterruptedException {
        for (int counter = 0; counter < 10000 && expr.eval(player, bindings) > 0; counter++){
            for(Statement s : q){
                if(player.isDone()) return;
                s.eval(player, bindings);
                if(player.isDone()) return;
            }
        }
    }
    @Override
    public void append(Statement s){
        q.add(s);
    }
}
