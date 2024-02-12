package Parser.Expression;

import Parser.EvalError;

import java.util.Map;

public record Variable(String name) implements Expr{
    @Override
    public int eval(Map<String, Integer> bindings) throws EvalError {
        if (bindings.containsKey(name)){ return bindings.get(name); }
        throw new EvalError("undefined variable -> " + name);
    }

    /*
     Config to Special variable

        switch (name) {
            case "rows", "cols", "currow", "curcol", "budget", "deposit", "int", "maxdeposit", "random" -> updateSpecialVar(bindings);
        };

     In production should contact with Player and Crew class
     */

}
