package Parser;

import java.util.Map;

public record Variable(String name) implements Expr{
    @Override
    public int eval(Map<String, Integer> bindings) throws Exception {
        if (bindings.containsKey(name)){ return bindings.get(name); }
        throw new Exception("undefined variable -> " + name);
    }
}
