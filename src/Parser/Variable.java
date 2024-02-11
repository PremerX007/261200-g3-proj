package Parser;

import java.util.Map;

public record Variable(String name) implements Expr{
    @Override
    public int eval(Map<String, Integer> bindings) throws EvalError {
        if (bindings.containsKey(name)){ return bindings.get(name); }
        throw new EvalError("undefined variable -> " + name);
    }
}
