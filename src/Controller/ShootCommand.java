package Controller;

import Parser.EvalError;
import Parser.Expression.Expr;
import Parser.Statement.Statement;

import java.util.Map;

public record ShootCommand(int direction, Expr expr) implements Statement {
    @Override
    public void eval(Player player, Map<String, Long> bindings) throws EvalError {
        player.getCrew().shoot(direction, expr.eval(player, bindings));
    }
}
