package Parser;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public record WhileStatement(Expr expr) implements GroupState {
    private static Queue<State> q = new LinkedList<>();
    @Override
    public void eval(Map<String, Integer> bindings) throws Exception {
        for (int counter = 0; counter < 10000 && expr.eval(bindings) > 0; counter++){
            for(State s : q){
                s.eval(bindings);
            }
        }
    }

    @Override
    public void append(State s){
        q.add(s);
    }
}
