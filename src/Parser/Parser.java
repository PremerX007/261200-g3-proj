package Parser;

import Tokenizer.LexicalError;
import Tokenizer.SyntaxError;

public interface Parser {
    void parse() throws Exception, EvalError, LexicalError, SyntaxError;
}
