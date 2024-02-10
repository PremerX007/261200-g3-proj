package Parser;

import java.util.Map;

public record AssignIdentifier(String key, Expr val) implements State {
    @Override
    public void eval(Map<String, Integer> bindings) throws Exception {
        boolean isNotSpecial = switch (key) {
            case "rows", "cols", "currow", "curcol", "budget", "deposit", "int", "maxdeposit", "random" -> false;
            default -> true;
        };
        if(!bindings.containsKey(key)) bindings.put(key,0);
        if(isNotSpecial) bindings.put(key, val.eval(bindings)); //no-op
    }
}
