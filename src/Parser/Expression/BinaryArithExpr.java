package Parser.Expression;

import Parser.EvalError;

import java.util.Map;

public record BinaryArithExpr(Expr left, String op, Expr right) implements Expr {
    @Override
    public int eval(Map<String, Integer> bindings) throws EvalError {
        int lv = left.eval(bindings);
        int rv = right.eval(bindings);
        if (op.equals("+")) return lv + rv;
        if (op.equals("-")) return lv - rv;
        if (op.equals("*")) return lv * rv;
        if (op.equals("/")) return lv / rv;
        if (op.equals("%")) return lv % rv;
        if (op.equals("^")) return (int) Math.pow(lv, rv);
        throw new EvalError("unknown op -> " + op);
    }
}
