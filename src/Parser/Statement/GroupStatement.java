package Parser.Statement;

import Parser.EvalError;
import Controller.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupStatement implements Statement {
    private List<Statement> q = new ArrayList<>();
    @Override
    public void eval(Player player, Map<String, Long> bindings) throws EvalError {
        // when execute evaluator before run parse construction plan
        if(q.isEmpty()) throw new EvalError("There are no statement in queue, please parse your construction plan before execute evaluator");

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
