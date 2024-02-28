package Controller;

import Parser.EvalError;
import Parser.Expression.Expr;

import java.util.Map;

public record Variable(String name) implements Expr {
    @Override
    public long eval(Player player, Map<String, Long> bindings) throws EvalError {
        switch (name){
            case "rows":
                return Territory.instance.getRow();
            case "cols":
                return Territory.instance.getCol();
            case "currow":
                return player.getCrew().getRow();
            case "curcol":
                return player.getCrew().getCol();
            case "budget":
                return player.getBudget();
            case "deposit":
                return player.getCrew().getNowDeposit();
            case "int":
                return player.getCrew().getInterest();
            case "maxdeposit":
                return Territory.instance.getMaxDeposit();
            case "random":
                return (long) ((Math.random() * 1000));
            default:
                break;
        }

        if (bindings.containsKey(name)){ return bindings.get(name); }
        throw new EvalError("undefined variable -> " + name);
    }

    /*
     Config to Special variable

        switch (name) {
            case "rows", "cols", "currow", "curcol", "budget", "deposit", "int", "maxdeposit", "random" -> updateSpecialVar(bindings);
        };

     In production should contact with Player and Crew class
     */

}
