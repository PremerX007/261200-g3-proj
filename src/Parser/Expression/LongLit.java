package Parser.Expression;

import java.util.Map;

public record LongLit(long val) implements Expr {
    @Override
    public long eval(Map<String, Long> bindings) {
        return val;
    }
}