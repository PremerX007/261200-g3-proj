package Parser.Statement;

import Parser.EvalError;

import java.util.Map;

public interface State {
    void eval(Map<String, Integer> bindings) throws EvalError;
}
