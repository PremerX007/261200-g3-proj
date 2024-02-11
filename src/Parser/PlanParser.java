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
        if(!tkz.hasNextToken()){
            throw new Exception("construction plans should have at least one");
        }
        while(tkz.hasNextToken()){
            State s = parseStatement();
            s.eval(identifier);
        }
    }

    private State parseStatement() throws Exception {
        return switch (tkz.peek()) {
            case "{"        -> parseBlockStatement();
            case "if"       -> parseIfStatement();
            case "while"    -> parseWhileStatement();
            default         -> parseCommand();
        };
    }


    private State parseBlockStatement() throws Exception {
        GroupState b = new BlockStatement();
        tkz.consume("{");
        while(!tkz.peek("}")){
            b.append(parseStatement());
        }
        tkz.consume("}");
        return b;
    }

    private State parseIfStatement() throws Exception {
        tkz.consume("if");

        tkz.consume("(");
        Expr expr = parseExpression();
        tkz.consume(")");

        tkz.consume("then");
        State then = parseStatement();
        tkz.consume("else");
        State els = parseStatement();

        return new ifStatement(expr,then,els);

    }

    private State parseWhileStatement() throws Exception {
        tkz.consume("while");
        tkz.consume("(");
        Expr ex = parseExpression(); // no-op not complete
        tkz.consume(")");

        GroupState w = new WhileStatement(ex);
        w.append(parseStatement());

        return w;
    }

    private State parseCommand() throws Exception {
        return switch (tkz.peek()) {
            case "done", "relocate", "move", "invest", "collect", "shoot" -> parseActionCommand();
            default -> parseAssignmentStatement();
        };
    }

    private State parseActionCommand() throws Exception {
        return switch (tkz.peek()) {
            case "done"     -> parseDoneCommand();
            case "relocate" -> parseRelocateCommand();
            case "move"     -> parseMoveCommand();
            case "invest", "collect" -> parseRegionCommand();
            case "shoot"    -> parseAttackCommand();
            default -> throw new Exception("Wrong grammar");
        };
    }

    private State parseDoneCommand() {
        // Execute done method (require gameplay)
        return new State() {
            @Override
            public void eval(Map<String, Integer> bindings) throws Exception {
                System.out.println("Wait for \"done\" func");
            }
        };
    }

    private State parseRelocateCommand() {
        // Execute relocate method (require gameplay)
        return new State() {
            @Override
            public void eval(Map<String, Integer> bindings) throws Exception {
                System.out.println("Wait for \"Relocate\" func");
            }
        };
    }

    private State parseMoveCommand() throws Exception {
        tkz.consume("move");
        long d = parseDirection();
        // Execute move method (require crew)
        return new State() {
            @Override
            public void eval(Map<String, Integer> bindings) throws Exception {
                System.out.println("Wait for \"Move\" func");
                System.out.println("Direction = " + d);
            }
        };
    }

    private long parseDirection() throws Exception {
        return switch (tkz.consume()){
            case "up" -> 1;
            case "upright" -> 2;
            case "downright" -> 3;
            case "down" -> 4;
            case "downleft" -> 5;
            case "upleft" -> 6;
            default -> throw new Exception("Direction is not correct");
        };
    }

    private State parseRegionCommand() throws Exception {
        if(tkz.peek("invest")){
            tkz.consume();
            Expr ex = parseExpression();
            // Execute invest method (require region)
            return new State() {
                @Override
                public void eval(Map<String, Integer> bindings) throws Exception {
                    System.out.println("Wait for \"Invest\" func");
                    System.out.println("Invest Money is " + ex.eval(bindings) + " coins");
                }
            };

        } else if (tkz.peek("collect")) {
            tkz.consume();
            // Execute collect method (require region)
            Expr ex = parseExpression();
            return new State() {
                @Override
                public void eval(Map<String, Integer> bindings) throws Exception {
                    System.out.println("Wait for \"Collect\" func");
                    System.out.println("I Need Collect " + ex.eval(bindings) + " coins from this Region");
                }
            };

        }
        return null;
    }

    private State parseAttackCommand() throws Exception {
        tkz.consume("shoot");
        long d = parseDirection();
        Expr ex = parseExpression();
        // Execute shoot method (require crew)
        return new State() {
            @Override
            public void eval(Map<String, Integer> bindings) throws Exception {
                System.out.println("Wait for \"Shoot\" func");
                System.out.println("Shoot!! @Direction ->" + d + " With Damage " + ex.eval(bindings));
            }
        };
    }

    private State parseAssignmentStatement() throws Exception {
        String key = tkz.consume();
        if(tkz.peek("=")){
            tkz.consume();
            Expr value = parseExpression();
            return new AssignIdentifier(key, value);
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
            return parseInfoExpression();
        }
    }

    private Expr parseInfoExpression() throws Exception { // Careful not complete
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
            throw new Exception("Wrong grammar");
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
