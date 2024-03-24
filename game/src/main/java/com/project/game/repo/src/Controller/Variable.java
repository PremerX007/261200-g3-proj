package com.project.game.repo.src.Controller;

import com.project.game.repo.src.Parser.EvalError;
import com.project.game.repo.src.Parser.Expression.Expr;

import java.util.Map;

public record Variable(String name) implements Expr {
    @Override
    public long eval(Player player, Map<String, Long> bindings) {
        return switch (name) {
            case "rows"         -> Territory.instance.getRow();
            case "cols"         -> Territory.instance.getCol();
            case "currow"       -> player.getCrew().getRow();
            case "curcol"       -> player.getCrew().getCol();
            case "budget"       -> player.getBudget();
            case "deposit"      -> player.getCrew().getNowDeposit();
            case "int"          -> player.getCrew().getInterest();
            case "maxdeposit"   -> Territory.instance.getMaxDeposit();
            case "random"       -> (long) ((Math.random() * 1000));
            default -> {
                if (!bindings.containsKey(name)) {
                    bindings.put(name, 0L);
                }
                yield bindings.get(name);
            }
        };
    }

    /*
     Config to Special variable

        switch (name) {
            case "rows", "cols", "currow", "curcol", "budget", "deposit", "int", "maxdeposit", "random" -> updateSpecialVar(bindings);
        };

     In production should contact with Player and Crew class
     */

}
