package mov;

public class MovToken {
    final MovTokenType type;
    final String lexeme;
    final Object literal;
    final int line;
 
    MovToken(MovTokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}