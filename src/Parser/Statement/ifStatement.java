package Parser.Statement;

import Parser.EvalError;
import Parser.Expression.Expr;

import java.util.Map;

public record ifStatement(Expr ex, Statement then, Statement els) implements Statement {
    @Override
    public void eval(Map<String, Long> bindings) throws EvalError {
        if(ex.eval(bindings) > 0){
            then.eval(bindings);
        }else {
            els.eval(bindings);
        }
    }
}
