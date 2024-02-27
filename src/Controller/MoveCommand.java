package Controller;

import Parser.EvalError;
import Parser.Statement.Statement;

import java.util.Map;

public record MoveCommand(int direction) implements Statement {
    @Override
    public void eval(Player player, Map<String, Long> bindings) throws EvalError {
        player.getCrew().move(direction);
    }
}
