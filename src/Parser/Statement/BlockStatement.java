package Parser.Statement;

import Parser.EvalError;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class BlockStatement implements GroupState {
    private Queue<State> q = new LinkedList<>();
    @Override
    public void eval(Map<String, Integer> bindings) throws EvalError {
        for(State s : q){
            s.eval(bindings);
        }
    }

    @Override
    public void append(State s){
        q.add(s);
    }

}
