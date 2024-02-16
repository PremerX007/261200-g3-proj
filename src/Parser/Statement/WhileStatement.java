package Parser.Statement;

import Parser.EvalError;
import Parser.Expression.Expr;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class WhileStatement implements Statement {
    private Expr expr;
    private Queue<Statement> q = new LinkedList<>();
    public WhileStatement(Expr expr){
        this.expr = expr;
    }

    @Override
    public void eval(Map<String, Long> bindings) throws EvalError {
        for (int counter = 0; counter < 10000 && expr.eval(bindings) > 0; counter++){
            for(Statement s : q){
                s.eval(bindings);
            }
        }
    }

    @Override
    public void append(Statement s){
        q.add(s);
    }
}
