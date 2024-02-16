package Parser.Expression;

import Parser.EvalError;

import java.util.Map;

public interface Expr {
    long eval(Map<String, Long> bindings) throws EvalError;
}
