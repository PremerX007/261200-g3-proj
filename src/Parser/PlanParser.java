package Parser;

import Parser.Expression.*;
import Parser.Statement.*;

import Tokenizer.Tokenizer;
import Tokenizer.LexicalError;
import Tokenizer.SyntaxError;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class PlanParser implements Parser{
    private Tokenizer tkz;

    public PlanParser(Tokenizer tkz){
        this.tkz = tkz;
    }

    @Override
    public Statement parse() throws LexicalError, SyntaxError {
        if(!tkz.hasNextToken())
            throw new SyntaxError("Construction plans should have statement at least one");

        Statement g = new GroupStatement();
        while(tkz.hasNextToken()){
            Statement s = parseStatement();
            g.append(s);
        }

        return g;
    }

    private Statement parseStatement() throws LexicalError, SyntaxError {
        return switch (tkz.peek()) {
            case "{"        -> parseBlockStatement();
            case "if"       -> parseIfStatement();
            case "while"    -> parseWhileStatement();
            default         -> parseCommand();
        };
    }


    private Statement parseBlockStatement() throws LexicalError, SyntaxError {
        Statement block = new GroupStatement();
        tkz.consume("{");
        while(!tkz.peek("}")){
            block.append(parseStatement());
        }
        tkz.consume("}");
        return block;
    }

    private Statement parseIfStatement() throws LexicalError, SyntaxError {
        tkz.consume("if");

        tkz.consume("(");
        Expr expr = parseExpression();
        tkz.consume(")");

        tkz.consume("then");
        Statement then = parseStatement();
        tkz.consume("else");
        Statement els = parseStatement();

        return new ifStatement(expr,then,els);

    }

    private Statement parseWhileStatement() throws LexicalError, SyntaxError {
        tkz.consume("while");
        tkz.consume("(");
        Expr ex = parseExpression();
        tkz.consume(")");

        Statement w = new WhileStatement(ex);
        w.append(parseStatement());

        return w;
    }

    private Statement parseCommand() throws LexicalError, SyntaxError {
        return switch (tkz.peek()) {
            case "done", "relocate", "move", "invest", "collect", "shoot" -> parseActionCommand();
            default -> parseAssignmentStatement();
        };
    }

    private Statement parseActionCommand() throws LexicalError, SyntaxError {
        return switch (tkz.peek()) {
            case "done"     -> parseDoneCommand();
            case "relocate" -> parseRelocateCommand();
            case "move"     -> parseMoveCommand();
            case "invest", "collect" -> parseRegionCommand();
            case "shoot"    -> parseAttackCommand();
            default -> throw new SyntaxError("Wrong grammar");
        };
    }

    private Statement parseDoneCommand() {
        // Execute done method (require gameplay)
        return new Statement() {
            @Override
            public void eval(Map<String, Integer> bindings) {
                System.out.println("Wait for \"done\" func");
            }
        };
    }

    private Statement parseRelocateCommand() {
        // Execute relocate method (require gameplay)
        return new Statement() {
            @Override
            public void eval(Map<String, Integer> bindings) {
                System.out.println("Wait for \"Relocate\" func");
            }
        };
    }

    private Statement parseMoveCommand() throws LexicalError, SyntaxError {
        tkz.consume("move");
        long d = parseDirection();
        // Execute move method (require crew)
        return new Statement() {
            @Override
            public void eval(Map<String, Integer> bindings) {
                System.out.println("Wait for \"Move\" func");
                System.out.println("Direction = " + d);
            }
        };
    }

    private long parseDirection() throws LexicalError, SyntaxError {
        return switch (tkz.consume()){
            case "up"           -> 1;
            case "upright"      -> 2;
            case "downright"    -> 3;
            case "down"         -> 4;
            case "downleft"     -> 5;
            case "upleft"       -> 6;
            default -> throw new SyntaxError("Direction is not correct, Please check your construction plans again.");
        };
    }

    private Statement parseRegionCommand() throws LexicalError, SyntaxError {
        if(tkz.peek("invest")){
            tkz.consume();
            Expr ex = parseExpression();
            // Execute invest method (require region)
            return new Statement() {
                @Override
                public void eval(Map<String, Integer> bindings) throws EvalError {
                    System.out.println("Wait for \"Invest\" func");
                    System.out.println("Invest Money is " + ex.eval(bindings) + " coins");
                }
            };

        } else if (tkz.peek("collect")) {
            tkz.consume();
            // Execute collect method (require region)
            Expr ex = parseExpression();
            return new Statement() {
                @Override
                public void eval(Map<String, Integer> bindings) throws EvalError {
                    System.out.println("Wait for \"Collect\" func");
                    System.out.println("I Need Collect " + ex.eval(bindings) + " coins from this Region");
                }
            };

        }
        return null;
    }

    private Statement parseAttackCommand() throws LexicalError, SyntaxError {
        tkz.consume("shoot");
        long d = parseDirection();
        Expr ex = parseExpression();
        // Execute shoot method (require crew)
        return new Statement() {
            @Override
            public void eval(Map<String, Integer> bindings) throws EvalError {
                System.out.println("Wait for \"Shoot\" func");
                System.out.println("Shoot!! @Direction ->" + d + " With Damage " + ex.eval(bindings));
            }
        };
    }

    private Statement parseAssignmentStatement() throws LexicalError, SyntaxError {
        String key = tkz.consume();
        tkz.consume("=");
        Expr value = parseExpression();
        return new AssignIdentifier(key, value);
    }

    private Expr parseExpression() throws LexicalError, SyntaxError {
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

    private Expr parseTerm() throws LexicalError, SyntaxError {
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

    private Expr parseFactor() throws LexicalError, SyntaxError {
        Expr p = parsePower();
        if(tkz.peek("^")){
            tkz.consume();
            p = new BinaryArithExpr(p, "^", parseFactor());
        }
        return p;
    }

    private Expr parsePower() throws LexicalError, SyntaxError {
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
            return parseInfoExpression();
        }
    }

    private Expr parseInfoExpression() throws LexicalError, SyntaxError { // Careful not complete
        if(tkz.peek("opponent")){
            tkz.consume();
            // Execute opponent method (require Crew locate)
            return new IntLit(1); //that example not complete
        } else if (tkz.peek("nearby")) {
            tkz.consume();
            // Execute nearby method (require Crew locate)
            long d = parseDirection();
            return new IntLit((int) (100*d+1)); //that example not complete
        } else {
            throw new SyntaxError("Wrong grammar");
        }
        // Be Careful not complete
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
