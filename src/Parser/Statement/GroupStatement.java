package Parser.Statement;

import Parser.EvalError;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class GroupStatement implements Statement {
    private Queue<Statement> q = new LinkedList<>();
    @Override
    public void eval(Map<String, Integer> bindings) throws EvalError {
        // when execute evaluator before run parse construction plan
        if(q.isEmpty()) throw new EvalError("There are no statement in queue, please parse your construction plan before execute evaluator");

        for(Statement s : q){
            s.eval(bindings);
        }
    }

    @Override
    public void append(Statement s){
        q.add(s);
    }

}
