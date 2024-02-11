package Parser;

import java.util.Map;

public record ifStatement(Expr ex, State then, State els) implements State{
    @Override
    public void eval(Map<String, Integer> bindings) throws EvalError {
        if(ex.eval(bindings) > 0){
            then.eval(bindings);
        }else {
            els.eval(bindings);
        }
    }
}
