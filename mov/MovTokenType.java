package mov;

enum  MovTokenType {

    // Types 
    KIND, QUERY, CONDITION, KEYWORD, DESCRIPT, LITERAL,
    
    // Query
    IS, FOR, IN, OF, WITH, STARRING, DIRECTED_BY, NOT,

    // Keyword 
    FIND, HAVE, SAY, WRITE,
    
    // Condition
    WHERE, WITHOUT, KINDC, STRC,

    // Kinds 
    MOVIES, RATINGS, GENRE, STARS, YEAR, SUMMARY, LENGTH, DIRECTOR, AGE_APPROPRIATE,

    // Literals
    IDENTIFIER, STRING, NUMBER,

    // Symbols
    EQUAL,

    EOF
    
}