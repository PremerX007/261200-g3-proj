package Parser.Statement;

import Parser.EvalError;
import Controller.Player;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class GroupStatement implements Statement {
    private Queue<Statement> q = new LinkedList<>();
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
