package Controller;

import Parser.EvalError;
import Parser.Statement.Statement;

import java.util.Map;

public class DoneCommand implements Statement {
    @Override
    public void eval(Player player, Map<String, Long> bindings) throws EvalError {
        player.playerDone();
    }
}
