package Parser;

import java.util.Map;

public interface Expr extends Node{
    int eval(Map<String, Integer> bindings) throws EvalError;
}
