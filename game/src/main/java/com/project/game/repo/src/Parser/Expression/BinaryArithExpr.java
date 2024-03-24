package com.project.game.repo.src.Parser.Expression;

import com.project.game.repo.src.Controller.Player;
import com.project.game.repo.src.Parser.EvalError;

import java.util.Map;

public record BinaryArithExpr(Expr left, String op, Expr right) implements Expr {
    @Override
    public long eval(Player player, Map<String, Long> bindings) throws EvalError {
        long lv = left.eval(player, bindings);
        long rv = right.eval(player, bindings);
        if (op.equals("+")) return lv + rv;
        if (op.equals("-")) return lv - rv;
        if (op.equals("*")) return lv * rv;
        if (op.equals("/")) return lv / rv;
        if (op.equals("%")) return lv % rv;
        if (op.equals("^")) return (long) Math.pow(lv, rv);
        throw new EvalError("unknown op -> " + op);
    }
}
