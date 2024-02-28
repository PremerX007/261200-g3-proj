package Parser.Statement;

import Controller.Player;
import Parser.EvalError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BlockStatement implements Statement {
    private List<Statement> q = new ArrayList<>();
    @Override
    public void eval(Player player, Map<String, Long> bindings) throws EvalError {
        for(Statement s : q){
            if(player.isDone()) return;
            s.eval(player, bindings);
            if(player.isDone()) return;
        }
    }

    @Override
    public void append(Statement s){
        q.add(s);
    }
}
