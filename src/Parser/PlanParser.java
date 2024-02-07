package Parser;

import Tokenizer.Tokenizer;

import java.util.HashMap;
import java.util.Map;

public class PlanParser implements Parser{
    private Tokenizer tkz;
    Map<String, Integer> identifier = new HashMap<>();

    public PlanParser(Tokenizer tkz){
        this.tkz = tkz;
    }

    @Override
    public void parse() throws Exception {
        parsePlan();
//        if(tkz.hasNextToken()) { throw new Exception("leftover token"); }
    }

    private void parsePlan() throws Exception {
        while(tkz.hasNextToken()){
            parseStatement();
        }
    }

    private void parseStatement() throws Exception {
        if (tkz.peek("{")) {
            parseBlockStatement();
        } else if (tkz.peek("if")){
            parseIfStatement();
        } else if (tkz.peek("while")){
            parseWhileStatement();
        } else {
            parseCommand();
        }
    }


    private void parseBlockStatement() throws Exception {
        tkz.consume("{");
        while(!tkz.peek("}")){
            parseStatement();
        }
        tkz.consume("}");

    }

    private void parseIfStatement() {

    }

    private void parseWhileStatement() throws Exception {
        tkz.consume("while");
        tkz.consume("(");
        parseExpression();
        tkz.consume(")");
        parseStatement();
    }

    private void parseCommand() throws Exception {
        switch (tkz.peek()){
            case "done":
            case "relocate":
            case "move":
            case "invest":
            case "collect":
            case "shoot":
                parseActionCommand();
                break;
            default:
                parseAssignmentStatement();
        }
    }

    private void parseActionCommand() {
        
    }

    private void parseAssignmentStatement() throws Exception {
        String key = tkz.consume();
        // identifier
        if(!identifier.containsKey(key)){
            identifier.put(key,0);
        }
        if(tkz.peek("=")){
            tkz.consume();
            Expr value = parseExpression();
            identifier.remove(key);
            identifier.put(key, value.eval(identifier));
        }else{
            throw new Exception("Wrong grammar");
        }
    }

    private Expr parseExpression() throws Exception {
        Expr e = parseTerm();
        while (tkz.peek("+")){
            tkz.consume();
            e = new BinaryArithExpr(e, "+", parseTerm());
        }
        while (tkz.peek("-")){
            tkz.consume();
            e = new BinaryArithExpr(e, "-", parseTerm());
        }
        return e;
    }

    private Expr parseTerm() throws Exception {
        Expr e = parseFactor();
        while (tkz.peek("*")){
            tkz.consume();
            e = new BinaryArithExpr(e, "*", parseFactor());
        }
        while (tkz.peek("/")){
            tkz.consume();
            e = new BinaryArithExpr(e, "/", parseFactor());
        }
        while (tkz.peek("%")){
            tkz.consume();
            e = new BinaryArithExpr(e, "%", parseFactor());
        }
        return e;
    }

    private Expr parseFactor() throws Exception {
        Expr p = parsePower();
        if(tkz.peek("^")){
            tkz.consume();
            p = new BinaryArithExpr(p, "^", parseFactor());
        }
        return p;
    }

    private Expr parsePower() throws Exception {
        if(isNumeric(tkz.peek())){
            return new IntLit(Integer.parseInt(tkz.consume()));
        } else if(Character.isLetter(tkz.peek().charAt(0))){
            return new Variable(tkz.consume());
        } else if(tkz.peek("(")) {
            tkz.consume("(");
            Expr e = parseExpression();
            tkz.consume(")");
            return e;
        } else {
            Expr e = parseInfoExpression();
            return e;
        }
    }

    private Expr parseInfoExpression() throws Exception { // Careful not complete
        if(tkz.peek("opponent")){
            tkz.consume();
            // Execute opponent method (require Crew locate)
        } else if (tkz.peek("nearby")) {
            tkz.consume();
            // Execute nearby method (require Crew locate)
        } else {
            throw new Exception("Wrong grammar");
        }
        return null; // Careful not complete
    }

    /** REF : https://www.baeldung.com/java-check-string-number */
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
