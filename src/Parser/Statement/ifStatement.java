package Parser.Statement;

import Parser.EvalError;
import Parser.Expression.Expr;
import Controller.Player;

import java.util.Map;

public record IfStatement (Expr ex, Statement then, Statement els) implements Statement {
    @Override
    public void eval(Player player, Map<String, Long> bindings) throws EvalError {
        if(ex.eval(player, bindings) > 0 && !player.isDone()){
            then.eval(player, bindings);
        }else {
            els.eval(player, bindings);
        }
    }
}
