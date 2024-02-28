package Controller;

import Parser.EvalError;
import Parser.Expression.Expr;

import java.util.Map;

public record Opponent() implements Expr {
    @Override
    public long eval(Player player, Map<String, Long> bindings) throws EvalError {
        return player.getCrew().opponentCheck();
    }
}
