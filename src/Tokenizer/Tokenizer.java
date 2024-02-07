package Tokenizer;

public interface Tokenizer {
    boolean hasNextToken();
    String peek();
    boolean peek(String s);
    String consume() throws Exception;
    void consume(String s) throws Exception;
}