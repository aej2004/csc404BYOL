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

    private MovExpr assignment() {

        if(match(EQUAL)) {

            MovToken keyword = previous();
            MovToken literal = consume(STRING, "Expect literal after 'have'.");
            MovToken symbol = consume(EQUAL, "Expect symbol after literal.");
            MovExpr expression = null;

            if (match(EQUAL)) {
                expression = expression();
            }

            return new MovExpr.Have(keyword, literal, symbol, expression);

        } else if (match(SAY)) {

            MovToken keyword = peek();
            MovToken literal1 = consume(STRING, "Expect literal after 'say'.");
            MovToken kind = consume(KIND, "Expect 'kind' after literal.");
            MovToken literal2 = consume(LITERAL, "Expect literal after 'kind'.");

            return new MovExpr.Say(keyword, literal1, kind, literal2);

        }

    }

    private MovStmt declaration() {
        try {
            if (match(HAVE)) return haveDeclaration();
            if (match(SAY)) return sayDeclaration();
            return statement();
        } catch (ParseError error) {
            synchronize();
            return null;
        }
    }

    private MovStmt haveDeclaration() {
        MovToken keyword = previous();
        MovToken literal = consume(STRING, "Expect literal after 'have'.");
        MovToken symbol = consume(EQUAL, "Expect symbol after literal.");
        MovExpr expression = null;
        
        if (match(EQUAL)) {
            expression = expression();
        }
        
        return new MovStmt.Have(keyword, literal, symbol, expression);
    }

    private MovStmt sayDeclaration() {
        MovToken keyword = previous();
        MovToken literal1 = consume(STRING, "Expect literal after 'say'.");
        MovToken kind = consume(KIND, "Expect 'kind' after literal.");
        MovToken literal2 = consume(STRING, "Expect literal after 'kind'.");
        
        return new MovStmt.Say(keyword, literal1, kind, literal2);
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
        if (match(WRITE)) return writeStatement();
        throw error(peek(), "Expect statement.");
    }

    private MovStmt findStatement() {
        MovToken keyword = previous();
        MovToken kind = consume(KIND, "Expect 'kind' after 'find'.");
        MovToken descriptor = consume(DESCRIPT, "Expect 'where' after 'kind'.");
        MovToken literal = consume(STRING, "Expect literal after 'where'.");

        return new MovStmt.Find(keyword, kind, descriptor, literal);
    }

    private MovStmt writeStatement() {
        MovToken keyword = previous();
        MovToken kind = consume(KIND, "Expect 'kind' after 'write'.");
        MovToken descriptor = consume(DESCRIPT, "Expect 'without' after 'kind'.");
        MovToken literal = consume(STRING, "Expect literal after 'without'.");

        return new MovStmt.Write(keyword, kind, descriptor, literal);
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
