package Parser.Expression;

import Controller.Player;

import java.util.Map;

public record LongLit(long val) implements Expr {
    @Override
    public long eval(Player player, Map<String, Long> bindings) {
        return val;
    }
}