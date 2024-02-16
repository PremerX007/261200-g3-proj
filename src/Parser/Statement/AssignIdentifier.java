package Parser.Statement;

import Parser.EvalError;
import Parser.Expression.Expr;

import java.util.Map;

public record AssignIdentifier(String key, Expr val) implements Statement {
    @Override
    public void eval(Map<String, Long> bindings) throws EvalError {
        boolean isNotSpecial = switch (key) {
            case "rows", "cols", "currow", "curcol", "budget", "deposit", "int", "maxdeposit", "random" -> false;
            default -> true;
        };
        if(isNotSpecial){ //no-op
            if(!bindings.containsKey(key))
                bindings.put(key, 0L);

            bindings.put(key, val.eval(bindings));
        }
    }
}
