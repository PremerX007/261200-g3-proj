package Parser;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public record BlockStatement() implements GroupState {
    private static Queue<State> q = new LinkedList<>();
    @Override
    public void eval(Map<String, Integer> bindings) throws Exception {
        for(State s : q){
            s.eval(bindings);
        }
    }

    @Override
    public void append(State s){
        q.add(s);
    }

}
