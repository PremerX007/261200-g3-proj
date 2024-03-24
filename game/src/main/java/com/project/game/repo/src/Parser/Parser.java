package com.project.game.repo.src.Parser;

import com.project.game.repo.src.Parser.Statement.Statement;
import com.project.game.repo.src.Tokenizer.LexicalError;
import com.project.game.repo.src.Tokenizer.SyntaxError;

public interface Parser {
    Statement parse() throws LexicalError, SyntaxError;
}
