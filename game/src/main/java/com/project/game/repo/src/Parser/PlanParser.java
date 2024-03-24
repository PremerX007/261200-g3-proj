package com.project.game.repo.src.Parser;

import com.project.game.repo.src.Parser.Expression.*;
import com.project.game.repo.src.Parser.Statement.*;

import com.project.game.repo.src.Controller.*;
import com.project.game.repo.src.Tokenizer.Tokenizer;
import com.project.game.repo.src.Tokenizer.LexicalError;
import com.project.game.repo.src.Tokenizer.SyntaxError;

public class PlanParser implements Parser{
    private Tokenizer tkz;

    public PlanParser(Tokenizer tkz){
        this.tkz = tkz;
    }
    @Override
    public Statement parse() throws LexicalError, SyntaxError {
        if(!tkz.hasNextToken())
            throw new SyntaxError("Construction plans should have statement at least one");

        Statement g = new BlockStatement();
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
        Statement block = new BlockStatement();
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

        return new IfStatement(expr,then,els);

    }

    private Statement parseWhileStatement() throws LexicalError, SyntaxError {
        tkz.consume("while");
        tkz.consume("(");
        if(tkz.peek(")")) throw new SyntaxError("In while loop should have expression");
        Expr ex = parseExpression();
        tkz.consume(")");

        Statement w = new WhileStatement(ex);
        w.append(parseStatement());

        return w;
    }

    private Statement parseCommand() throws LexicalError, SyntaxError {
        return switch (tkz.peek()) {
            case "done",
                    "relocate",
                    "move",
                    "invest",
                    "collect",
                    "shoot"  -> parseActionCommand();
            default -> parseAssignmentStatement();
        };
    }

    private Statement parseActionCommand() throws LexicalError, SyntaxError {
        return switch (tkz.peek()) {
            case "done"                 -> parseDoneCommand();
            case "relocate"             -> parseRelocateCommand();
            case "move"                 -> parseMoveCommand();
            case "invest", "collect"    -> parseRegionCommand();
            case "shoot"                -> parseAttackCommand();
            default -> throw new SyntaxError("");
        };
    }

    private Statement parseDoneCommand() throws LexicalError, SyntaxError {
        tkz.consume("done");
        // Execute done method (require gameplay)
        return new DoneCommand();
    }

    private Statement parseRelocateCommand() throws LexicalError, SyntaxError {
        tkz.consume("relocate");
        // Execute relocate method (require gameplay)
        return new RelocateCommand();
    }

    private Statement parseMoveCommand() throws LexicalError, SyntaxError {
        tkz.consume("move");
        int direction = parseDirection();
        // Execute move method (require crew)
        return new MoveCommand(direction);
    }

    private int parseDirection() throws LexicalError, SyntaxError {
        String dir = tkz.peek();
        return switch (tkz.consume()){
            case "up"           -> 1;
            case "upright"      -> 2;
            case "downright"    -> 3;
            case "down"         -> 4;
            case "downleft"     -> 5;
            case "upleft"       -> 6;
            default -> throw new SyntaxError("Direction is not correct " + "(" + dir + ")" + ", Please check your construction plans again." );
        };
    }

    private Statement parseRegionCommand() throws LexicalError, SyntaxError {
        if(tkz.peek("invest")){
            tkz.consume();
            Expr ex = parseExpression();
            // Execute invest method (require region)
            return new InvestCommand(ex);

        } else if (tkz.peek("collect")) {
            tkz.consume();
            // Execute collect method (require region)
            Expr ex = parseExpression();
            return new CollectCommand(ex);

        }
        return null;
    }

    private Statement parseAttackCommand() throws LexicalError, SyntaxError {
        tkz.consume("shoot");
        int d = parseDirection();
        Expr ex = parseExpression();
        // Execute shoot method (require crew)
        return new ShootCommand(d, ex);
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
            return new LongLit(Long.parseLong(tkz.consume()));
        } else if(tkz.peek("opponent") || tkz.peek("nearby")){
            return parseInfoExpression();
        } else if(tkz.peek("(")) {
            tkz.consume("(");
            Expr e = parseExpression();
            tkz.consume(")");
            return e;
        } else {
            return new Variable(tkz.consume()); //should handle operate +-*/
        }

        //else if(Character.isLetter(tkz.peek().charAt(0)))
    }

    private Expr parseInfoExpression() throws LexicalError, SyntaxError { // Careful not complete
        if(tkz.peek("opponent")){
            tkz.consume();
            // Execute opponent method (require Crew locate)
            return new Opponent();
        } else if (tkz.peek("nearby")) {
            tkz.consume();
            int d = parseDirection();
            // Execute nearby method (require Crew locate)
            return new Nearby(d); //that example not complete
        } else {
            throw new SyntaxError("Wrong grammar");
        }
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
