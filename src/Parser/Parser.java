package Parser;

import Parser.Statement.Statement;
import Tokenizer.LexicalError;
import Tokenizer.SyntaxError;

public interface Parser {
    Statement parse() throws LexicalError, SyntaxError;
}
