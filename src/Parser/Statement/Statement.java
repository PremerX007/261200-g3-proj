package Parser.Statement;

import Parser.EvalError;

import java.util.Map;

public interface Statement {
    void eval(Map<String, Integer> bindings) throws EvalError;
    default void append(Statement s) {}
}
