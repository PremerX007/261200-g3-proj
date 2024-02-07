package Parser;

import java.util.Map;

public record IntLit(int val) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) {
        return val;
    }
}