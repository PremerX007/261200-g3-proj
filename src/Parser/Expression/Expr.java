package Parser.Expression;

import Controller.Player;
import Parser.EvalError;

import java.util.Map;

public interface Expr {
    long eval(Player player, Map<String, Long> bindings) throws EvalError;
}
