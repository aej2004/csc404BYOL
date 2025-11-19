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
import mov.MovStmt.FindS;
import mov.MovStmt.HaveS;
import mov.MovStmt.SayS;
import mov.MovStmt.WriteS;

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


public class Interpreter implements MovStmt.Visitor<Object>, MovCond.Visitor<Void> {

    public Set<Movie> allMoviesDB = new HashSet<>();
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

        System.out.println("Loaded " + allMoviesDB.size() + " movies into database");
        
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



    @Override
    public Void visitNegCMovCond(NegC movcond) {
        
        System.out.println("Visiting NegC condition");

        return null;

    }

    @Override
    public Void visitStrCMovCond(StrC movcond) {
        
        System.out.println("Visiting StrC condition");

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
    public Set<Object> visitFindSMovStmt(FindS findstmt) {
        Set<Object> result = new HashSet<>();

        if (findstmt.identifier.type == STRING) {
            result = findString(findstmt);
        } else if (findstmt.identifier.type == IDENTIFIER) {
            result = findIdentifier(findstmt);
        } else {
            throw new RuntimeError(findstmt.identifier, "Invalid identifier type in FindS statement");
        }

        System.out.println(result);
        return result;
    }

    public Set<Object> findString(FindS findstmt) {
        String search = (String) findstmt.identifier.literal;
        Set<Object> result = new HashSet<>();
        result = searchDB(findstmt.kind,findstmt.query, search);
        return result;
    }

    @SuppressWarnings("unchecked")
    public Set<Object> findIdentifier(FindS findstmt) {
        Set<Object> result = new HashSet<>();
        Set<Object> listCreated = (Set<Object>) globals.get(findstmt.identifier.lexeme);

        switch (findstmt.kind) {
            case MOVIES:
                for (Object obj : listCreated) {
                    if (obj instanceof String) {
                        switch (findstmt.query) {
                            case STARRING: 
                                String star = (String) obj;
                                result.addAll(searchDB(Kind.MOVIES, Query.STARRING, star));
                                break;
                            case DIRECTED_BY:
                                String director = (String) obj;
                                result.addAll(searchDB(Kind.MOVIES, Query.DIRECTED_BY, director));
                                break;
                        }
                    }
                }
                break;
            default:
                break;
        }

        return result;
    }

    public Set<Object> searchDB(Kind kind, Query query, String search){
        Set<Object> result = new HashSet<>();
        switch (kind) {
            case MOVIES:
                if (query == Query.STARRING) {
                    for (Movie m : allMoviesDB) {
                        if (m.stars.contains(search)) {
                            result.add(m.movie_name);
                        }
                    }
                } else if (query == Query.DIRECTED_BY) {
                    for (Movie m : allMoviesDB) {
                        if (m.director.contains(search)) {
                            result.add(m.movie_name);
                        }
                    }
                } 
                break;
            case RATINGS:
                for (Movie m : allMoviesDB) {
                    if (m.movie_name.contains(search)) {
                        result.add(m.rating);
                    }
                }
                break;
            case GENRE:
                if (query == Query.FOR) {
                    for (Movie m : allMoviesDB) {
                        if (m.stars.contains(search)) {
                            //System.out.println("Found movie: " + m.genre);
                            result.addAll(Arrays.asList(m.genre.split("\\s*,\\s*")));
                        }
                    }
                } else if (query == Query.OF) {
                    for (Movie m : allMoviesDB) {
                        if (m.movie_name.contains(search)) {
                            //System.out.println("Found movie: " + m.genre);
                            result.addAll(Arrays.asList(m.genre.split("\\s*,\\s*")));
                        }
                    }
                }
            case STARS:
                for (Movie m : allMoviesDB) {
                    if (m.movie_name.contains(search)) {
                        result.addAll(Arrays.asList(m.stars.split("\\s*,\\s*")));
                    }
                }
                break;
            case YEAR:
                if (query == Query.IN) {
                    for (Movie m : allMoviesDB) {
                        if (m.movie_name.contains(search) && m.year != 0) {
                            result.add(m.year);
                        }
                    }
                } else if (query == Query.FOR) {
                    for (Movie m : allMoviesDB) {
                        if (m.stars.contains(search) && m.year != 0) {
                            result.add(m.year);
                        }
                    }
                } else if (query == Query.OF) {
                    for (Movie m : allMoviesDB) {
                        if (m.director.contains(search) && m.year != 0) {
                            result.add(m.year);
                        }
                    }
                }
                break;
            case SUMMARY:
                for (Movie m : allMoviesDB) {
                    if (m.movie_name.contains(search)) {
                        result.add(m.description);
                    }
                }
                break;
            case LENGTH:
                for (Movie m : allMoviesDB) {
                    if (m.movie_name.contains(search)) {
                        result.add(m.length);
                    }
                }
                break;
            case DIRECTOR:
                if(query == Query.FOR) {
                    for (Movie m : allMoviesDB) {
                        if (m.movie_name.contains(search)) {
                            result.addAll(Arrays.asList(m.director.split("\\s*,\\s*")));
                        }
                    }
                } else if (query == Query.OF) {
                    for (Movie m : allMoviesDB) {
                        if (m.stars.contains(search)) {
                            result.addAll(Arrays.asList(m.director.split("\\s*,\\s*")));
                        }
                    }
                }
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    public Object visitHaveSMovStmt(HaveS havstmt) {
        
        String var = havstmt.identifier.lexeme;
        // Execute the statement to get the result
        Object result = execute(havstmt.statement);

        globals.put(var, result);

        return null;

    }

    @Override
    public Object visitSaySMovStmt(SayS movstmt) {
        
        System.out.println("Saying " + movstmt.identifier.lexeme + " "
                + movstmt.ratsum.lexeme + " " + movstmt.numstr.lexeme);

        return "say interpreted";

    }

    @Override
    public Object visitWriteSMovStmt(WriteS movstmt) {
        
        System.out.println("Writing " + movstmt.kind + " " + movstmt.query
                + " " + movstmt.identifier.lexeme + " with condition " + movstmt.condition);

        return "write interpreted";

    }

    @Override
    public Void visitBinaryC(BinaryC movcond) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitBinaryC'");
    }
    
}
