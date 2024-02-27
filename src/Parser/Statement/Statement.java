package Parser.Statement;

import Parser.EvalError;
import Controller.Player;
import Tokenizer.LexicalError;
import Tokenizer.SyntaxError;
import UPBEAT.Position;

import java.io.IOException;
import java.util.Map;

public interface Statement {
    void eval(Player player, Map<String, Long> bindings) throws EvalError;
    default void eval(Map<String, Long> bindings) throws EvalError, LexicalError, SyntaxError, IOException {
        eval(new Player("Null Player",new Position(0,0),100),bindings);
    }
    default void append(Statement s) {}
}
