package Tokenizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;

public class PlanTokenizer implements Tokenizer{
    private String src;
    private String next;
    private int pos;
    public PlanTokenizer(String filename) throws IOException, LexicalError {
        this.pos = 0;
        ReadConstructionPlan(filename);
        computeNext();
    }

    public void ReadConstructionPlan(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))){
            StringBuilder str = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                line = line.replaceAll("#.*", "");
                str.append(line);
                str.append(' ');
            }
            this.src = str.toString();
        } catch (IOException e){
            throw new IOException("System can not manage target file -> " + e);
        }
    }

    public boolean hasNextToken() { return next != null; }

    public void checkNextToken() {
        if (!hasNextToken()) throw new NoSuchElementException("no more tokens");
    }

    public String peek() {
        checkNextToken();
        return next;
    }

    public boolean peek(String s) {
        if(!hasNextToken()) return false;
        return peek().equals(s);
    }

    public String consume() throws LexicalError {
        checkNextToken();
        String result = next;
        computeNext();
        return result;
    }

    public void consume(String s) throws SyntaxError, LexicalError {
        if(peek(s)){
            consume();
        }else{
            throw new SyntaxError(s + " expected, Please check your construction plans.");
        }
    }

    public void computeNext() throws LexicalError {
        StringBuilder s = new StringBuilder();
        while (pos < src.length() && Character.isWhitespace(src.charAt(pos))) {
            pos++; // ignore whitespace
        }

        if (pos == src.length()) { // no more tokens
            next = null;
            return;
        }

        char c = src.charAt(pos);

        if (Character.isDigit(c)) { // start of number
            s.append(c);
            for (pos++; pos < src.length() && Character.isDigit(src.charAt(pos)); pos++){
                s.append(src.charAt(pos));
            }
        }
        else if (Character.isLetter(c)) { // start of string
            s.append(c);
            for (pos++; pos < src.length() && Character.isLetter(src.charAt(pos)); pos++){
                s.append(src.charAt(pos));
            }
        }
        else if (c == '+' || c == '(' || c == '-' || c == '*' || c == '/' || c == '%' || c == ')' || c == '=' || c == '{' || c == '}' || c == '^') {
            s.append(c);
            pos++;
        }
        else {
            throw new LexicalError("unknown character: " + c);
        }
        next = s.toString();
    }
}