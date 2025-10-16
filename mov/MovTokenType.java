package mov;

enum  MovTokenType {

    // Types 
    KIND, KEYWORD, DESCRIPT, LITERAL,
    
    // Descriptors
    IS, FOR, IN, OF, WITH, STARRING, DIRECTED_BY,

    // Keyword 
    FIND, HAVE, SAY, WRITE, WHERE, WITHOUT,

    // Kinds 
    MOVIES, RATINGS, GENRE, STARS, YEAR, SUMMARY, LENGTH, DIRECTOR, AGE_APPROPRIATE,

    // Literals
    IDENTIFIER, STRING, NUMBER,

    // Symbols
    EQUAL,

    EOF
    
}
