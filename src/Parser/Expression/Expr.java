package Parser.Expression;

import Parser.EvalError;

import java.util.Map;

public interface Expr {
    int eval(Map<String, Integer> bindings) throws EvalError;
}
