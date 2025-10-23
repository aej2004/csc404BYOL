package mov;

import java.util.List;

import static mov.MovTokenType.*;

public class MovParser {
    
    private static class ParseError extends RuntimeException {}

    private final List<MovToken> tokens;
    private int current = 0;
    private int loopStack = 0;     // number of enclosed loops

    MovParser(List<MovToken> tokens) {
        this.tokens = tokens;
    }

    List<MovStmt> parse() {
        List<MovStmt> statements = new java.util.ArrayList<>();

        while (!isAtEnd()) {
            statements.add(statement());
        }
      return statements;
    }

    private MovStmt statement() {
        if (match(FIND)) return findStatement();
        if (match(HAVE)) return haveStatment();
        if (match(SAY)) return sayStatement();
        if (match(WRITE)) return writeStatement();
        throw error(peek(), "Expect statement.");
    }


    /*
    private MovCond declaration() {
        try {
            if (match(NEGC)) return negCondition();
            if (match(STRC)) return strCondition();
            if (match(KINDC)) return kindCondition();
            if (match(LTC)) return ltCondition();
            return condition();
        } catch (ParseError error) {
            synchronize();
            return null;
        }
    }

    private MovCond negCondition() {
        Condition condition;

        return new MovCond.NegC(condition);
    }

    private MovCond strCondition() {
        String str;

        return new MovCond.StrC(str);
    }

    private MovCond kindCondition() {
        Kind kind;
        String str;

        return new MovCond.KindC(kind, str);
    }

    private MovCond ltCondition() {
        Condition left;
        Condition right;
        MovToken operator;

        return new MovCond.LtC(left, right, operator);
    }
        */

        /*
    private void synchronize() {
        advance();

        /* 
        figure out what is the determining factor for the end of a statement

        while (!isAtEnd()) {
            if (previous().type == ) return;
        

            switch (peek().type) {
                case FIND:
                case WRITE:
                case WHERE:
                case WITHOUT:
                case KIND:
                    return;
            }

            advance();
        /* } 
    }*/


    private MovStmt findStatement() {
        Kind kind = kind();
        Query query = query();
        MovToken obj = advance();
        Condition condition = condition();

        return new MovStmt.FindS(kind, query, obj, condition);
    }

    private MovStmt haveStatment() {
        Kind kind;
        MovToken identifier;
        Condition condition;
        return null;
        //return new MovStmt.HaveS(kind, identifier, condition);
    }

    private MovStmt sayStatement() {
        MovToken identifier;
        MovToken ratsum;
        MovToken numstr;
        return null;

        //return new MovStmt.SayS(identifier, ratsum, numstr);
    }

    private MovStmt writeStatement() {
        Kind kind;
        Query query;
        MovToken identifier;
        Condition condition;
        return null;

       // return new MovStmt.WriteS(kind, query, identifier, condition);
    }

    private Kind kind() {
        if (match(MovTokenType.MOVIES)) {
            return Kind.MOVIE;
        } else if (match(MovTokenType.STARS)) {
                return Kind.STARS;
        } else {
            throw error(peek(), "Expect kind.");
        }
    }

    private Query query() {
        if (match(MovTokenType.STARRING)) {
            return Query.STARRING;
        } else if (match(MovTokenType.IN)) {
            return Query.IN;
        } else {
            throw error(peek(), "Expect query.");
        }
    }

    private Condition condition() {
        return null;
    }







    /* General parsing utility methods */

    private MovToken consume(MovTokenType type, String message) {
        if (check(type)) return advance();
        throw error(peek(), message);
    }

    /** Consumes the next token if it matches any of the given type(s) */
    private boolean match(MovTokenType... types) {
        for (MovTokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(MovTokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private MovToken advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private MovToken previous() {
        return tokens.get(current - 1);
    }

    private boolean isAtEnd() {
        return peek().type == MovTokenType.EOF;
    }

    private MovToken peek() {
        return tokens.get(current);
    }

    private ParseError error(MovToken token, String message) {
        Mov.error(token.line, message);
        return new ParseError();
    }

}
