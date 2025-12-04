package mov;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static mov.MovTokenType.*;

public class MovScanner {
    private final String source;
    private final List<MovToken> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    

    private static final Map<String, MovTokenType> keywords;
    static {
        keywords = new HashMap<>();
        keywords.put("find", FIND);
        keywords.put("have", HAVE);
        keywords.put("say", SAY);
        keywords.put("write", WRITE);
        keywords.put("where", WHERE);
        keywords.put("without", WITHOUT);
        keywords.put("is", IS);
        keywords.put("for", FOR);
        keywords.put("in", IN);
        keywords.put("of", OF);
        keywords.put("with", WITH);
        keywords.put("starring", STARRING);
        keywords.put("directed_by", DIRECTED_BY);
        keywords.put("not", NOT);

        keywords.put("movies", MOVIES);
        keywords.put("ratings", RATINGS);
        keywords.put("genre", GENRE);
        keywords.put("stars", STARS);
        keywords.put("year", YEAR);
        keywords.put("summary", SUMMARY);
        keywords.put("length", LENGTH);
        keywords.put("director", DIRECTOR);
        keywords.put("age_appropriate", AGE_APPROPRIATE);
    }

    MovScanner(String source) {
        this.source = source;
    }

    List<MovToken> scanTokens() { 
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new MovToken(MovTokenType.EOF, "", null, line));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        switch (c) {    

            case '=' -> addToken(EQUAL);
            case ' ', '\r', '\t' -> {} // Ignore whitespace.
            case '\n' -> line++;

            case '"' -> string();

            default -> {
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    Mov.error(line, "Unexpected character.");
                }
            }
        }
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) advance();

        String text = source.substring(start, current); 

        // Check for "directed by" as a single token
        if (text.equals("directed") && peek() == ' ') {
            int tempStart = current; // Save the current position
            advance(); // Consume the space
            if (source.substring(current, Math.min(current + 2, source.length())).equals("by")) {
                advance(); // Consume 'b'
                advance(); // Consume 'y'
                text = "directed_by"; // Combine into a single token
            } else {
                current = tempStart; // Revert if not "by"
            }
        }

        MovTokenType type = keywords.get(text);
        if (type == null) type = IDENTIFIER;
        addToken(type);
    }

    private void number() {
        while (isDigit(peek())) advance();

        if (peek() == '.' && isDigit(peekNext())) {
            advance(); // consume the "."

            while (isDigit(peek())) advance();
        }

        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }

        if (isAtEnd()) {
            Mov.error(line, "Unterminated string.");
            return;
        }

        advance(); // The closing ".

        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private char advance() {
        return source.charAt(current++);
    }

    private void addToken(MovTokenType type) {
        addToken(type, null);
    }

    private void addToken(MovTokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new MovToken(type, text, literal, line));
    }

}

