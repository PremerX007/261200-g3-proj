package com.project.game.repo.src.Tokenizer;

public interface Tokenizer {
    boolean hasNextToken();
    String peek();
    boolean peek(String s);
    String consume() throws LexicalError;
    void consume(String s) throws SyntaxError, LexicalError;
}