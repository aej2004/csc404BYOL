package mov;

import core.data.*;

import static mov.MovTokenType.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.crypto.Data;

import mov.MovCond.BinaryC;
import mov.MovCond.KindC;
import mov.MovCond.LtC;
import mov.MovCond.NegC;
import mov.MovCond.StrC;
import mov.MovStmt.Expression;
import mov.MovStmt.FindS;

import mov.MovExpr.HaveE;
import mov.MovExpr.SayE;
import mov.MovExpr.WriteE;

class Movie {
    String movie_name;
    int year;
    String certificate;
    String genre;
    double rating;
    String description;
    String director;
    String stars;
    String length;

    public Movie(String movie_name, String year, String certificate, String genre,
                 String rating, String description, String director, String stars,
                 String length) {
        this.movie_name = movie_name;
        this.certificate = certificate;
        this.genre = genre;
        this.description = description;
        this.director = director;
        this.stars = stars;
        this.length = length;

        try {
            this.rating = Double.parseDouble(rating);
        } catch (NumberFormatException e) {
            this.rating = 0.0; // default value if parsing fails
        }

        try {
            this.year = Integer.parseInt(year);
        } catch (NumberFormatException e) {
            this.year = 0; // default value if parsing fails
        }
    }

    @Override
    public String toString() {
        return "Movie [movie_name=" + movie_name + ", year=" + year + ", length=" + length
                        + ", certificate=" + certificate + ", genre=" + genre 
                        + ", rating=" + rating + ", director=" + director 
                        + ", stars=" + stars + ", description=" + description + "]";
    }
}


public class Interpreter implements MovStmt.Visitor<Object>, MovCond.Visitor<Void>, MovExpr.Visitor<Object> {

    public Set<Movie> allMoviesDB = new HashSet<>();
    public Set<Movie> currentMoviesDB = new HashSet<>();
    public Map<String, Object> globals = new HashMap<>();

    public Interpreter() {

        DataSource actionDS = DataSource.connect("data/action.csv");
        DataSource adventureDS = DataSource.connect("data/adventure.csv");
        DataSource animationDS = DataSource.connect("data/animation.csv");
        DataSource biographyDS = DataSource.connect("data/biography.csv");
        DataSource crimeDS = DataSource.connect("data/crime.csv");
        DataSource familyDS = DataSource.connect("data/family.csv");
        DataSource fantasyDS = DataSource.connect("data/fantasy.csv");
        DataSource film_noirDS = DataSource.connect("data/film-noir.csv");
        DataSource historyDS = DataSource.connect("data/history.csv");
        DataSource horrorDS = DataSource.connect("data/horror.csv");
        DataSource mysteryDS = DataSource.connect("data/mystery.csv");
        DataSource romanceDS = DataSource.connect("data/romance.csv");
        DataSource scifiDS = DataSource.connect("data/scifi.csv");
        DataSource sportsDS = DataSource.connect("data/sports.csv");
        DataSource thrillerDS = DataSource.connect("data/thriller.csv");
        DataSource warDS = DataSource.connect("data/war.csv");

        DataSource[] allDataSources = new DataSource[] {actionDS, adventureDS, animationDS, biographyDS,
                                    crimeDS, familyDS, fantasyDS, film_noirDS,
                                    historyDS, horrorDS, mysteryDS, romanceDS,
                                    scifiDS, sportsDS, thrillerDS, warDS };

        for (int i = 0; i < allDataSources.length; i++) {
            
            loadDB(allMoviesDB, allDataSources[i]);

        }

        //System.out.println("Loaded " + allMoviesDB.size() + " movies into database");
        
    }

    @SuppressWarnings("unchecked")
    public Set<Movie> loadDB(Set<Movie> db, DataSource ds) {
        ds.load();

        Set<Movie> movies = new HashSet<>(ds.fetchList(Movie.class, "movie_name", "year", "certificate", "genre", 
                                                            "rating", "description", "director", "star", "runtime"));

        for (Movie m : movies) {
            db.add(m);
        }

        return db;
    }

    void interpret(List<MovStmt> statements) {
        try {
            for (MovStmt statement : statements) {
                execute(statement);
            }
        } catch (RuntimeError error) {
            Mov.error(-1, "" + error);
        }
    }

    private Object execute(MovStmt stmt) {
        return stmt.accept(this);
    }

    public void findCondition(MovCond condition) {
        switch (condition.getClass().getSimpleName()) {
            case "StrC":
                condition.accept(this);
                break;
            default:
                throw new RuntimeError(null, "Unknown condition type");
        }        
    }

    @Override
    public Void visitNegCMovCond(NegC movcond) {
        
        System.out.println("Visiting NegC condition");

        return null;

    }

    @Override
    public Void visitStrCMovCond(StrC movcond) {
        String search = movcond.str.replace("\"", "");

        for(Movie m : allMoviesDB) {

            if (!(m.movie_name.contains(search)) && !(m.director.contains(search)) 
                    && !(m.stars.contains(search))){
                currentMoviesDB.add(m);
            }
        }

        return null;
    }

    @Override
    public Void visitKindCMovCond(KindC movcond) {
        
        System.out.println("Visiting KindC condition");

        return null;

    }

    @Override
    public Void visitLtCMovCond(LtC movcond) {
        
        System.out.println("Visiting LtC condition");

        return null;

    }

    @Override
    public Void visitBinaryCMovCond(BinaryC movcond) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitBinaryCMovCond'");
    }

    @Override
    public Set<Object> visitFindSMovStmt(FindS findstmt) {
        Set<Object> result = new HashSet<>();

        if (findstmt.condition == null) {
            if (findstmt.identifier.type == STRING) {
                result = findString(findstmt, allMoviesDB);
            } else if (findstmt.identifier.type == IDENTIFIER) {
                result = findIdentifier(findstmt, allMoviesDB);
            } else {
                throw new RuntimeError(findstmt.identifier, "Invalid identifier type in FindS statement");
            }
        } else if (findstmt.condition != null) {
            findCondition(findstmt.condition);
            if (findstmt.identifier.type == STRING) {
                result = findString(findstmt, currentMoviesDB);
            } else if (findstmt.identifier.type == IDENTIFIER) {
                result = findIdentifier(findstmt, currentMoviesDB);
            } else {
                throw new RuntimeError(findstmt.identifier, "Invalid identifier type in FindS statement");
            }
        }

        return result;
    }

    public Set<Object> findString(FindS findstmt, Set<Movie> moviesDB) {
        String search = (String) findstmt.identifier.literal;
        Set<Object> result = new HashSet<>();
        result = searchDB(findstmt.kind,findstmt.query, search, moviesDB);
        return result;
    }

    @SuppressWarnings("unchecked")
    public Set<Object> findIdentifier(FindS findstmt, Set<Movie> moviesDB) {
        Set<Object> result = new HashSet<>();
        Set<Movie> listCreated = (Set<Movie>) globals.get(findstmt.identifier.lexeme);

        for (Object obj : listCreated) {
            switch (findstmt.kind) {
                case MOVIES:
                    if (findstmt.query == Query.STARRING) {
                        result.addAll(searchDB(Kind.MOVIES, Query.STARRING, (String) obj, moviesDB));
                    } else if (findstmt.query == Query.DIRECTED_BY) {
                        result.addAll(searchDB(Kind.MOVIES, Query.DIRECTED_BY, (String) obj, moviesDB));
                    }
                    break;
                case RATINGS:
                    if (findstmt.query == Query.FOR) {
                        result.addAll(searchDB(Kind.RATINGS, Query.FOR, (String) obj, moviesDB));
                    }
                    break;
                case GENRE:
                    if (findstmt.query == Query.FOR) {
                        result.addAll(searchDB(Kind.GENRE, Query.FOR, (String) obj, moviesDB));
                    } else if (findstmt.query == Query.OF) {
                        result.addAll(searchDB(Kind.GENRE, Query.OF, (String) obj, moviesDB));
                    }
                    break;
                case STARS:
                    if (findstmt.query == Query.IN) {
                        result.addAll(searchDB(Kind.STARS, Query.IN, (String) obj, moviesDB));
                    }
                    break;
                case YEAR:
                    if (findstmt.query == Query.IN) {
                        result.addAll(searchDB(Kind.YEAR, Query.IN, (String) obj, moviesDB));
                    } else if (findstmt.query == Query.FOR) {
                        result.addAll(searchDB(Kind.YEAR, Query.FOR, (String) obj, moviesDB));
                    } else if (findstmt.query == Query.OF) {
                        result.addAll(searchDB(Kind.YEAR, Query.OF, (String) obj, moviesDB));
                    }
                    break;
                case SUMMARY:
                    if (findstmt.query == Query.OF) {
                        result.addAll(searchDB(Kind.SUMMARY, Query.OF, (String) obj, moviesDB));
                    }
                    break;
                case LENGTH:
                    if (findstmt.query == Query.OF) {
                        result.addAll(searchDB(Kind.LENGTH, Query.OF, (String) obj, moviesDB));
                    }
                    break;
                default:
                    break;
            }
        }

        return result;
    }

    public Set<Object> searchDB(Kind kind, Query query, String search, Set<Movie> moviesDB) {
        Set<Object> result = new HashSet<>();

        for (Movie m : moviesDB) {
            switch (kind) {
                case MOVIES:
                    if (query == Query.STARRING && m.stars.contains(search)) {
                        result.add(m.movie_name);
                    } else if (query == Query.DIRECTED_BY && m.director.contains(search)) {
                        result.add(m.movie_name);
                    }
                    break;
                case RATINGS:
                    if (query == Query.FOR && m.movie_name.contains(search)) {
                        result.add(m.rating);
                    }
                    break;
                case GENRE:
                    if (query == Query.FOR && m.stars.contains(search)) {
                        result.addAll(Arrays.asList(m.genre.split("\\s*,\\s*")));
                    } else if (query == Query.OF && m.movie_name.contains(search)) {
                        result.addAll(Arrays.asList(m.genre.split("\\s*,\\s*")));
                    }
                    break;
                case STARS:
                    if (query == Query.IN && m.movie_name.contains(search)) {
                        result.addAll(Arrays.asList(m.stars.split("\\s*,\\s*")));
                    }
                    break;
                case SUMMARY:
                    if (query == Query.OF && m.movie_name.contains(search)) {
                        result.add(m.description);
                    }
                    break;
                case YEAR:
                    if (query == Query.IN && m.movie_name.contains(search) && m.year != 0) {
                        result.add(m.year);
                    } else if (query == Query.FOR && m.stars.contains(search) && m.year != 0) {
                        result.add(m.year);
                    } else if (query == Query.OF && m.director.contains(search) && m.year != 0) {
                        result.add(m.year);
                    }
                    break;
                case LENGTH:
                    if (query == Query.OF && m.movie_name.contains(search) && m.length != "") {
                        result.add(m.length);
                    }
                    break;
                case DIRECTOR:
                    if (query == Query.FOR && m.movie_name.contains(search)) {
                        result.addAll(Arrays.asList(m.director.split("\\s*,\\s*")));
                    } else if (query == Query.OF && m.stars.contains(search)) {
                        result.addAll(Arrays.asList(m.director.split("\\s*,\\s*")));
                    }
                    break;
                default:
                    break;

            }
        }

        return result;
    }

    @Override
    public Object visitWriteEMovExpr(WriteE writeexpr) {
        Set<Object> result = new HashSet<>();
        if (writeexpr.condition == null) {
            if (writeexpr.identifier.type == STRING) {
                result = writeString(writeexpr, allMoviesDB);
            } else if (writeexpr.identifier.type == IDENTIFIER) {
                result = writeIdentifier(writeexpr, allMoviesDB);
            } else {
                throw new RuntimeError(writeexpr.identifier, "Invalid identifier type in FindS statement");
            }
        } else if (writeexpr.condition != null) {
            findCondition(writeexpr.condition);
            if (writeexpr.identifier.type == STRING) {
                result = writeString(writeexpr, currentMoviesDB);
            } else if (writeexpr.identifier.type == IDENTIFIER) {
                result = writeIdentifier(writeexpr, currentMoviesDB);
            } else {
                throw new RuntimeError(writeexpr.identifier, "Invalid identifier type in FindS statement");
            }
        }

        printDisplay(result);
        return null;
    }

    public void printDisplay(Set<Object> result) {

        if (result == null || result.isEmpty()) {
            System.out.println("No results found.");
            return;
        }
    
        // Convert to list for stable ordering (optional but nicer)
        List<Object> list = new ArrayList<>(result);
    
        // Compute column widths
        int indexWidth = String.valueOf(list.size()).length();
        int valueWidth = 0;
        for (Object obj : list) {
            valueWidth = Math.max(valueWidth, obj.toString().length());
        }
    
        // Headers
        String indexHeader = "No.";
        String valueHeader = "Result";
    
        indexWidth = Math.max(indexWidth, indexHeader.length());
        valueWidth = Math.max(valueWidth, valueHeader.length());
    
        String format = "| %-" + indexWidth + "s | %-" + valueWidth + "s |%n";
    
        // Print header
        System.out.printf(format, indexHeader, valueHeader);
        System.out.println("+" + "-".repeat(indexWidth + 2) +
                           "+" + "-".repeat(valueWidth + 2) + "+");
    
        // Print rows
        int i = 1;
        for (Object obj : list) {
            System.out.printf(format, i++, obj.toString());
        }
    }
    

    public Set<Object> writeString(WriteE writeexpr, Set<Movie> moviesDB) {
        String search = (String) writeexpr.identifier.literal;
        Set<Object> result = new HashSet<>();
        result = searchDB(writeexpr.kind,writeexpr.query, search, moviesDB);
        return result;
    }

    @SuppressWarnings("unchecked")
    public Set<Object> writeIdentifier(WriteE writeexpr, Set<Movie> moviesDB) {
        Set<Object> result = new HashSet<>();
        Set<Movie> listCreated = (Set<Movie>) globals.get(writeexpr.identifier.lexeme);

        for (Object obj : listCreated) {
            switch (writeexpr.kind) {
                case MOVIES:
                    if (writeexpr.query == Query.STARRING) {
                        result.addAll(searchDB(Kind.MOVIES, Query.STARRING, (String) obj, moviesDB));
                    } else if (writeexpr.query == Query.DIRECTED_BY) {
                        result.addAll(searchDB(Kind.MOVIES, Query.DIRECTED_BY, (String) obj, moviesDB));
                    }
                    break;
                case RATINGS:
                    if (writeexpr.query == Query.FOR) {
                        result.addAll(searchDB(Kind.RATINGS, Query.FOR, (String) obj, moviesDB));
                    }
                    break;
                case GENRE:
                    if (writeexpr.query == Query.FOR) {
                        result.addAll(searchDB(Kind.GENRE, Query.FOR, (String) obj, moviesDB));
                    } else if (writeexpr.query == Query.OF) {
                        result.addAll(searchDB(Kind.GENRE, Query.OF, (String) obj, moviesDB));
                    }
                    break;
                case STARS:
                    if (writeexpr.query == Query.IN) {
                        result.addAll(searchDB(Kind.STARS, Query.IN, (String) obj, moviesDB));
                    }
                    break;
                case YEAR:
                    if (writeexpr.query == Query.IN) {
                        result.addAll(searchDB(Kind.YEAR, Query.IN, (String) obj, moviesDB));
                    } else if (writeexpr.query == Query.FOR) {
                        result.addAll(searchDB(Kind.YEAR, Query.FOR, (String) obj, moviesDB));
                    } else if (writeexpr.query == Query.OF) {
                        result.addAll(searchDB(Kind.YEAR, Query.OF, (String) obj, moviesDB));
                    }
                    break;
                case SUMMARY:
                    if (writeexpr.query == Query.OF) {
                        result.addAll(searchDB(Kind.SUMMARY, Query.OF, (String) obj, moviesDB));
                    }
                    break;
                case LENGTH:
                    if (writeexpr.query == Query.OF) {
                        result.addAll(searchDB(Kind.LENGTH, Query.OF, (String) obj, moviesDB));
                    }
                    break;
                default:
                    break;
            }
        }

        return result;
    }

    @Override
    public Object visitExpressionMovStmt(Expression movexpr) {
        movexpr.expr.accept(this);
        return null;
    }

    @Override
    public Object visitHaveEMovExpr(HaveE havexpr) {
        String var = havexpr.identifier.lexeme;
        Object result = execute(havexpr.statement);

        globals.put(var, result);

        return null;
    }

    @Override
    public Object visitSayEMovExpr(SayE sayexpr) {
        Object kind = sayexpr.identifier.literal;
        Object change = sayexpr.numstr.literal;
        Set<Movie> m = new HashSet<>();

        for (Movie obj : allMoviesDB) {
            if (obj.movie_name.equals(kind)) {
                m.add(obj);
            }
        }

        for (Movie mov : m) {
            switch (sayexpr.ratsum.type) {
                case RATINGS:
                    mov.rating = (Double) change;
                    break;
                case SUMMARY:
                    mov.description = (String) change;
                    break;
                default:
                    throw new RuntimeError(sayexpr.ratsum, "Invalid ratsum type in SayE expression");
            }
        }

        return null;
    }
    
}
