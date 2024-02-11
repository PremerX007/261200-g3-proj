package Parser;

import java.util.Map;

public interface State {
    void eval(Map<String, Integer> bindings) throws EvalError;
}
