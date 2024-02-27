package Parser.Statement;

import Parser.EvalError;
import Parser.Expression.Expr;
import Controller.Player;

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
    public void eval(Player player, Map<String, Long> bindings) throws EvalError {
        for (int counter = 0; counter < 10000 && expr.eval(player, bindings) > 0; counter++){
            for(Statement s : q){
                if(player.isDone()) return;
                s.eval(player, bindings);
                if(player.isDone()) return;
            }
        }
    }

    @Override
    public void append(Statement s){
        q.add(s);
    }
}
