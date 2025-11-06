package mov;

enum  MovTokenType {

    // Types 
    KIND, QUERY, CONDITION, KEYWORD, DESCRIPT, LITERAL,
    
    // Query
    IS, FOR, IN, OF, WITH, STARRING, DIRECTED_BY,

    // Keyword 
    FIND, HAVE, SAY, WRITE,
    
    // Condition
    WHERE, WITHOUT, KINDC, STRC,
    NOT_EQUAL, LESS, LESS_EQUAL, GREATER, GREATER_EQUAL, NOT, AND, OR,

    // Kinds 
    MOVIES, RATINGS, GENRE, STARS, YEAR, SUMMARY, LENGTH, DIRECTOR, AGE_APPROPRIATE,

    // Literals
    IDENTIFIER, STRING, NUMBER,

    // Symbols
    EQUAL,

    EOF
    
}