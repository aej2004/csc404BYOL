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
            statements.add(declaration());
        }
        return statements;
    }

    private MovExpr expression() {
        return assignment();
    }

    private MovStmt declaration() {
        try {
            if (match(KIND)) return kindDeclaration();
            return statement();
        } catch (ParseError error) {
            synchronize();
            return null;
        }
    }

    private MovStmt kindDeclaration() {
        MovToken name = consume(IDENTIFIER, "Expect kind name.");
        
        MovExpr initializer = null;
        if (match(EQUAL)) {
            initializer = expression();
        }

    }

    private void synchronize() {
        advance();

        /* 
        figure out what is the determining factor for the end of a statement

        while (!isAtEnd()) {
            if (previous().type == ) return;
        */

            switch (peek().type) {
                case FIND:
                case HAVE:
                case SAY:
                case WRITE:
                case WHERE:
                case WITHOUT:
                case KIND:
                    return;
            }

            advance();
        /* } */
    }

    private MovStmt statement() {
        if (match(FIND)) return findStatement();
        if (match(HAVE)) return haveStatement();
        if (match(SAY)) return sayStatement();
        if (match(WRITE)) return writeStatement();
        if (match(WHERE)) return whereStatement();
        if (match(WITHOUT)) return withoutStatement();
        throw error(peek(), "Expect statement.");
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
