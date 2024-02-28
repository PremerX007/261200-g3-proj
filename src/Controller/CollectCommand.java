package Controller;

import Parser.EvalError;
import Parser.Expression.Expr;
import Parser.Statement.Statement;

import java.util.Map;

public record CollectCommand(Expr ex) implements Statement {
    @Override
    public void eval(Player player, Map<String, Long> bindings) throws EvalError {
        player.getCrew().collect(ex.eval(player, bindings));
    }
}
