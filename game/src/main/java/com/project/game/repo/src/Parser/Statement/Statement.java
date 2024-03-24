package com.project.game.repo.src.Parser.Statement;

import com.project.game.repo.src.Parser.EvalError;
import com.project.game.repo.src.Controller.Player;
import com.project.game.repo.src.Tokenizer.LexicalError;
import com.project.game.repo.src.Tokenizer.SyntaxError;
import com.project.game.repo.src.UPBEAT.Position;

import java.io.IOException;
import java.util.Map;

public interface Statement {
    void eval(Player player, Map<String, Long> bindings) throws EvalError, InterruptedException;
    default void eval(Map<String, Long> bindings) throws EvalError, LexicalError, SyntaxError, IOException, InterruptedException {
        eval(new Player("Null Player", new Position(0,0),100, "#000000", "#FF0000"), bindings);
    }
    default void append(Statement s) {}
}
