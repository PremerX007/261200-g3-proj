package Parser;

import java.util.Map;

public record AssignIdentifier(String key, Expr val) implements State {
    @Override
    public void eval(Map<String, Integer> bindings) throws Exception {
        if(!bindings.containsKey(key)) bindings.put(key,0);
        bindings.put(key, val.eval(bindings));
    }
}
