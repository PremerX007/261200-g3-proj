package Parser.Statement;

import Parser.EvalError;

import java.util.Map;

public interface Statement {
    void eval(Map<String, Long> bindings) throws EvalError;
    default void append(Statement s) {}
}
