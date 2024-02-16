package Parser;

import Parser.Statement.Statement;
import Tokenizer.LexicalError;
import Tokenizer.PlanTokenizer;
import Tokenizer.SyntaxError;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlanParserTest {
    @Test
    void AssignSpecialVariable() throws LexicalError, IOException, SyntaxError, EvalError {
        PlanParser plan = new PlanParser(new PlanTokenizer("src/Parser/TestConstPlaintext/AssignSpecialVariable.txt"));
        Map<String, Long> execute = new HashMap<>();
        Map<String, Long> expect = new HashMap<>();
        expect.put("a", 1L);
        expect.put("b", 2L);

        Statement s = plan.parse();
        s.eval(execute);
        assertEquals(expect,execute);
    }

    @Test
    void AssignStatement() throws LexicalError, IOException, SyntaxError, EvalError {
        PlanParser plan = new PlanParser(new PlanTokenizer("src/Parser/TestConstPlaintext/exceptAssign.txt"));
        assertThrows(SyntaxError.class, plan::parse);

        plan = new PlanParser(new PlanTokenizer("src/Parser/TestConstPlaintext/goodAssign.txt"));
        Map<String, Long> execute = new HashMap<>();
        Map<String, Long> expect = new HashMap<>();
        expect.put("num", 12L);
        expect.put("time", 4L);
        expect.put("tm", 45L);

        Statement s = plan.parse();
        s.eval(execute);
        assertEquals(expect,execute);
    }

    void BlockStatementParsing(){

    }

    void IfStatementParsing(){

    }

    void WhileStatementParsing(){

    }

    void ExpresssionParsing(){

    }
}