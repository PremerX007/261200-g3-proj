package Parser;

import Parser.Statement.Statement;
import Tokenizer.LexicalError;
import Tokenizer.PlanTokenizer;
import Tokenizer.SyntaxError;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlanParserTest {
    @Test
    void AssignSpecialVariable() throws LexicalError, IOException, SyntaxError, EvalError {
        PlanParser plan = new PlanParser(new PlanTokenizer(new FileReader("src/Parser/TestConstPlaintext/AssignSpecialVariable.txt")));
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
        PlanParser plan = new PlanParser(new PlanTokenizer(new FileReader("src/Parser/TestConstPlaintext/exceptAssign.txt")));
        assertThrows(SyntaxError.class, plan::parse);

        plan = new PlanParser(new PlanTokenizer(new FileReader("src/Parser/TestConstPlaintext/goodAssign.txt")));
        Map<String, Long> execute = new HashMap<>();
        Map<String, Long> expect = new HashMap<>();
        expect.put("num", 12L);
        expect.put("time", 4L);
        expect.put("tm", 45L);

        Statement s = plan.parse();
        s.eval(execute);
        assertEquals(expect,execute);
    }

    @Test
    void BlockStatementParsing() throws LexicalError, IOException, SyntaxError, EvalError {
        Map<String, Long> execute = new HashMap<>();
        Map<String, Long> expect = new HashMap<>();
        StringBuilder s = new StringBuilder();

        // more statement in Block
        s.append("{ a = a + 5 b = 12 - 3 c = 9 ^ 3}");
        expect.put("a",5L);
        expect.put("b",9L);
        expect.put("c",729L);

        PlanParser plan = new PlanParser(new PlanTokenizer(new StringReader(s.toString())));
        Statement stm = plan.parse();
        stm.eval(execute);
        assertEquals(expect,execute);
    }

    @Test
    void IfStatementParsing() throws LexicalError, SyntaxError, IOException, EvalError {
        Map<String, Long> execute = new HashMap<>();
        Map<String, Long> expect = new HashMap<>();
        StringBuilder s = new StringBuilder();

        s.append("if (1) then a = 1 else a = 0\n");
        s.append("if (0) then b = 1 else b = 1-2\n");
        s.append("if (b) then c = 1 else c = 0\n");
        s.append("if (b) then { ");
        s.append("    wrong = 404\n");
        s.append("    not = 404\n");
        s.append("    th4n = 404\n");
        s.append("} else {\n");
        s.append("    d = 1\n");
        s.append("    e = 2\n");
        s.append("    f = 3\n");
        s.append("}");

        expect.put("a",1L);
        expect.put("b",-1L);
        expect.put("c",0L);
        expect.put("d",1L);
        expect.put("e",2L);
        expect.put("f",3L);

        PlanParser plan = new PlanParser(new PlanTokenizer(new StringReader(s.toString())));
        Statement stm = plan.parse();
        stm.eval(execute);
        assertEquals(expect,execute);
    }

    @Test
    void WhileStatementParsing() throws LexicalError, IOException, SyntaxError, EvalError {
        Map<String, Long> execute = new HashMap<>();
        Map<String, Long> expect = new HashMap<>();
        StringBuilder s = new StringBuilder();

        // Avoiding infinite loops
        s.append("while (1) t = t + 1");

        // Stop when expression equal zero
        s.append("a = 10");
        s.append("while (a) a = a - 1");

        expect.put("t",10000L);
        expect.put("a",0L);

        PlanParser plan = new PlanParser(new PlanTokenizer(new StringReader(s.toString())));
        Statement stm = plan.parse();
        stm.eval(execute);
        assertEquals(expect,execute);
    }

    void ExpresssionParsing() throws LexicalError, IOException, SyntaxError, EvalError {
        Map<String, Long> execute = new HashMap<>();
        Map<String, Long> expect = new HashMap<>();
        StringBuilder s = new StringBuilder();




        PlanParser plan = new PlanParser(new PlanTokenizer(new StringReader(s.toString())));
        Statement stm = plan.parse();
        stm.eval(execute);
        assertEquals(expect,execute);
    }

}