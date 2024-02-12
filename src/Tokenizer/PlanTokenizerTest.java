package Tokenizer;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class PlanTokenizerTest {
    @Test
    void acceptNumber() throws LexicalError, IOException {
        PlanTokenizer exr = new PlanTokenizer("src/Tokenizer/TestTokenPlaintext/acceptNumber.txt");
        String [] correct = {"929", "88", "384", "511", "338", "548", "676", "330", "165", "209", "172", "238", "854", "858", "908", "57", "618", "20", "39", "555", "608", "370", "986", "631", "395", "605", "985", "19", "550", "668", "166", "57", "429", "787", "74", "867", "965", "798", "932", "367", "606", "426", "918", "137", "378", "858", "80", "65", "720", "64"};
        for(int i = 0; i<correct.length && exr.hasNextToken(); i++){
            assertEquals(exr.consume(),correct[i]);
        }
    }

    @Test
    void acceptOperator() throws LexicalError, IOException {
        String [] correct = {"+", "-", "*", "/", "=", "%", "^"};
        PlanTokenizer exr = new PlanTokenizer("src/Tokenizer/TestTokenPlaintext/acceptOperator.txt");
        boolean [] operator = {false,false,false,false,false,false,false};

        while (exr.hasNextToken()){
            if(exr.peek().contains(correct[0])) operator[0] = true;
            else if (exr.peek().contains(correct[1])) operator[1] = true;
            else if (exr.peek().contains(correct[2])) operator[2] = true;
            else if (exr.peek().contains(correct[3])) operator[3] = true;
            else if (exr.peek().contains(correct[4])) operator[4] = true;
            else if (exr.peek().contains(correct[5])) operator[5] = true;
            else if (exr.peek().contains(correct[6])) operator[6] = true;
            exr.consume();
        }

        StringBuilder s = new StringBuilder();
        for (int i = 0; i<correct.length; i++){
            if(!operator[i]) s.append(correct[i]);
        }

        if(!s.isEmpty()) fail("Tokenizer should accept this operator -> " + s);
    }

    @Test
    void acceptParentheses() throws LexicalError, IOException {
        String [] correct = {"(", ")"};
        PlanTokenizer exr = new PlanTokenizer("src/Tokenizer/TestTokenPlaintext/acceptOperator.txt");
        boolean leftParentheses = false;
        boolean rightParentheses = false;

        while (exr.hasNextToken()){
            if(exr.peek().contains(correct[0])) leftParentheses = true;
            else if (exr.peek().contains(correct[1])) rightParentheses = true;
            exr.consume();
        }

        if(!leftParentheses && !rightParentheses) fail("Tokenizer don't accept Parentheses");
    }

    @Test
    void acceptBrackets() throws LexicalError, IOException {
        String [] correct = {"{", "}"};
        PlanTokenizer exr = new PlanTokenizer("src/Tokenizer/TestTokenPlaintext/acceptOperator.txt");

        boolean leftBrackets = false;
        boolean rightBrackets = false;

        while (exr.hasNextToken()){
            if(exr.peek().contains(correct[0])) leftBrackets = true;
            else if (exr.peek().contains(correct[1])) rightBrackets = true;
            exr.consume();
        }

        if(!leftBrackets && !rightBrackets) fail("Tokenizer don't accept Brackets");
    }

    @Test
    void acceptCorrectWord() throws LexicalError, IOException {
        PlanTokenizer exr = new PlanTokenizer("src/Tokenizer/TestTokenPlaintext/acceptCorrectWord.txt");
        String [] correct = {"collect", "done", "down", "downleft", "downright", "else", "if", "invest", "move", "nearby", "opponent", "relocate", "shoot", "then", "up", "upleft", "upright", "while"};
        for(int i = 0; i<correct.length && exr.hasNextToken(); i++){
            assertEquals(exr.consume(),correct[i]);
        }
    }

    @Test
    void supportEndOfLineComments() throws LexicalError, IOException {
        PlanTokenizer exr = new PlanTokenizer("src/Tokenizer/TestTokenPlaintext/endOfLineComments.txt");
        while (exr.hasNextToken()){
            assertDoesNotThrow(() -> exr.consume());
        }
    }

    @Test
    void whenFileNotFound() {
        assertThrows(IOException.class, () -> new PlanTokenizer("src/Tokenizer/TestTokenPlaintext/xyz.txt"));
    }

    @Test
    void whenFileEmpty() throws LexicalError, IOException {
        PlanTokenizer exr = new PlanTokenizer("src/Tokenizer/TestTokenPlaintext/emptyfile.txt");
        assertFalse(exr.hasNextToken());
        assertThrows(NoSuchElementException.class, exr::checkNextToken);
    }

    @Test
    void unknownCharacter() throws LexicalError, IOException {
        PlanTokenizer exr = new PlanTokenizer("src/Tokenizer/TestTokenPlaintext/unknownChar.txt");
        exr.consume();
        exr.consume();
        assertThrows(LexicalError.class, exr::computeNext);
    }

    @Test
    void sampleConstructionPlan() throws LexicalError, IOException {
        PlanTokenizer exr = new PlanTokenizer("src/Tokenizer/TestTokenPlaintext/sampleCons.txt");
        String [] correct = {"t", "=", "t", "+", "1", "m", "=", "0", "while", "(", "deposit", ")", "{", "if", "(", "deposit", "-", "100", ")", "then", "collect", "(", "deposit", "/", "4", ")", "else", "if", "(", "budget", "-", "25", ")", "then", "invest", "25", "else", "{", "}", "if", "(", "budget", "-", "100", ")", "then", "{", "}", "else", "done", "opponentLoc", "=", "opponent", "if", "(", "opponentLoc", "/", "10", "-", "1", ")", "then", "if", "(", "opponentLoc", "%", "10", "-", "5", ")", "then", "move", "downleft", "else", "if", "(", "opponentLoc", "%", "10", "-", "4", ")", "then", "move", "down", "else", "if", "(", "opponentLoc", "%", "10", "-", "3", ")", "then", "move", "downright", "else", "if", "(", "opponentLoc", "%", "10", "-", "2", ")", "then", "move", "right", "else", "if", "(", "opponentLoc", "%", "10", "-", "1", ")", "then", "move", "upright", "else", "move", "up", "else", "if", "(", "opponentLoc", ")", "then", "if", "(", "opponentLoc", "%", "10", "-", "5", ")", "then", "{", "cost", "=", "10", "^", "(", "nearby", "upleft", "%", "100", "+", "1", ")", "if", "(", "budget", "-", "cost", ")", "then", "shoot", "upleft", "cost", "else", "{", "}", "}", "else", "if", "(", "opponentLoc", "%", "10", "-", "4", ")", "then", "{", "cost", "=", "10", "^", "(", "nearby", "downleft", "%", "100", "+", "1", ")", "if", "(", "budget", "-", "cost", ")", "then", "shoot", "downleft", "cost", "else", "{", "}", "}", "else", "if", "(", "opponentLoc", "%", "10", "-", "3", ")", "then", "{", "cost", "=", "10", "^", "(", "nearby", "down", "%", "100", "+", "1", ")", "if", "(", "budget", "-", "cost", ")", "then", "shoot", "down", "cost", "else", "{", "}", "}", "else", "if", "(", "opponentLoc", "%", "10", "-", "2", ")", "then", "{", "cost", "=", "10", "^", "(", "nearby", "downright", "%", "100", "+", "1", ")", "if", "(", "budget", "-", "cost", ")", "then", "shoot", "downright", "cost", "else", "{", "}", "}", "else", "if", "(", "opponentLoc", "%", "10", "-", "1", ")", "then", "{", "cost", "=", "10", "^", "(", "nearby", "upright", "%", "100", "+", "1", ")", "if", "(", "budget", "-", "cost", ")", "then", "shoot", "upright", "cost", "else", "{", "}", "}", "else", "{", "cost", "=", "10", "^", "(", "nearby", "up", "%", "100", "+", "1", ")", "if", "(", "budget", "-", "cost", ")", "then", "shoot", "up", "cost", "else", "{", "}", "}", "else", "{", "dir", "=", "random", "%", "6", "if", "(", "dir", "-", "4", ")", "then", "move", "upleft", "else", "if", "(", "dir", "-", "3", ")", "then", "move", "downleft", "else", "if", "(", "dir", "-", "2", ")", "then", "move", "down", "else", "if", "(", "dir", "-", "1", ")", "then", "move", "downright", "else", "if", "(", "dir", ")", "then", "move", "upright", "else", "move", "up", "m", "=", "m", "+", "1", "}", "}", "if", "(", "budget", "-", "1", ")", "then", "invest", "1", "else", "{", "}"};
        for(int i = 0; i<correct.length && exr.hasNextToken(); i++){
            assertEquals(exr.consume(),correct[i]);
        }
    }

}