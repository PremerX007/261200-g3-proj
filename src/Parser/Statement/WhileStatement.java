package Parser.Statement;

import Parser.EvalError;
import Parser.Expression.Expr;
import Controller.Player;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class WhileStatement implements Statement {
    private Expr expr;
    private List<Statement> q = new ArrayList<>();
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
