import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;

public class ExprTokenizer implements Tokenizer{
    private String src;
    private String next;
    private int pos;
    private int line;
    public ExprTokenizer() throws Exception {
        this.line = 1;
        ReadConstructionPlan();
        computeNext();
    }

    public void ReadConstructionPlan(){
        try (BufferedReader reader = new BufferedReader(new FileReader("src/conp.txt"))){
            long currentLine = 1;
            while ((src = reader.readLine()) != null){
                if(currentLine < line){
                    currentLine++;
                    continue;
                }
                this.line++;
                this.pos = 0;
                return;
            }
            next = null;
        } catch (IOException e){
            System.err.format("System can not manage target file : %s%n", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
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

    public String consume() throws Exception {
        checkNextToken();
        String result = next;
        computeNext();
        return result;
    }

    public void consume(String s) throws Exception {
        if(peek(s)){
            consume();
        }else{
            throw new Exception(s + " expected");
        }
    }

    public void computeNext() throws Exception {
        StringBuilder s = new StringBuilder();
        while (pos < src.length() && Character.isWhitespace(src.charAt(pos))) {
            pos++; // ignore whitespace
        }

        if (pos == src.length()) { // no more tokens
            ReadConstructionPlan();
            computeNext();
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
            throw new Exception("unknown character: " + c);
        }
        next = s.toString();
    }
}