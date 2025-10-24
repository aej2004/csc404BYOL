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
        MovCond condition = condition();

        return new MovStmt.FindS(kind, query, obj, condition);
    }

    private MovStmt haveStatment() {
        MovToken identifier = advance();
        advance(); // consume the '='
        MovStmt statement = statement();

        return new MovStmt.HaveS(identifier, statement);
    }

    private MovStmt sayStatement() {
        MovToken identifier = advance();
        Kind ratsum = ratsum();
        MovToken numstr = numstr();
        
        return new MovStmt.SayS(identifier, ratsum, numstr);
    }

    private MovStmt writeStatement() {
        Kind kind = kind();
        Query query = query();
        MovToken obj = advance();
        MovCond condition = condition();

        return new MovStmt.WriteS(kind, query, obj, condition);
    }

    private Kind kind() {
        if (match(MovTokenType.MOVIES)) {
            return Kind.MOVIES;
        } else if (match(MovTokenType.RATINGS)) {
            return Kind.RATINGS;
        } else if (match(MovTokenType.GENRE)) {
            return Kind.GENRE;
        } else if (match(MovTokenType.STARS)) {
                return Kind.STARS;
        } else if (match(MovTokenType.YEAR)) {
            return Kind.YEAR;
        } else if (match(MovTokenType.SUMMARY)) {
            return Kind.SUMMARY;
        } else if (match(MovTokenType.LENGTH)) {
            return Kind.LENGTH;
        } else if (match(MovTokenType.DIRECTOR)) {
            return Kind.DIRECTOR;
        } else {
            throw error(peek(), "Expect kind.");
        }
    }

    private Query query() {
        if (match(MovTokenType.STARRING)) {
            return Query.STARRING;
        } else if (match(MovTokenType.DIRECTED_BY)) {
            return Query.DIRECTED_BY;
        } else if (match(MovTokenType.FOR)) {
            return Query.FOR;
        } else if (match(MovTokenType.IN)) {
            return Query.IN;
        } else if (match(MovTokenType.OF)) {
            return Query.OF;
        } else if (match(MovTokenType.IS)) {
            return Query.IS;
        } else {
            throw error(peek(), "Expect query.");
        }
    }

    private MovCond condition() {
        if (match(MovTokenType.WHERE)) {
            Kind kind = kind();
            Query query = query();
            MovToken identifier = advance();
            return new MovCond.WhereC(kind, query, identifier);
        } else {
            return null; // no condition
        }
    }

    private Kind ratsum() {
        if (match(MovTokenType.RATINGS)) {
            return Kind.RATINGS;
        } else if (match(MovTokenType.SUMMARY)) {
            return Kind.SUMMARY;
        } else {
            throw error(peek(), "Expect ratsum.");
        }
    }

    private MovToken numstr() {
        if (match(MovTokenType.NUMBER)) {
            return new MovToken(MovTokenType.NUMBER, previous().lexeme, previous().literal, previous().line);
        } else if (match(MovTokenType.STRING)) {
            return new MovToken(MovTokenType.STRING, previous().lexeme, previous().literal, previous().line);
        } else {
            throw error(peek(), "Expect number or string.");
        }
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
