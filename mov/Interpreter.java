package mov;

import core.data.*;

import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.Data;

import mov.MovCond.KindC;
import mov.MovCond.LtC;
import mov.MovCond.NegC;
import mov.MovCond.StrC;
import mov.MovStmt.FindS;
import mov.MovStmt.HaveS;
import mov.MovStmt.SayS;
import mov.MovStmt.WriteS;

import static mov.MovTokenType.*;

class Movie {
    String movie_name;
    int year;
    String certificate;
    String genre;
    int rating;
    String description;
    String director;
    String stars;

    public Movie(String movie_name, String year, String certificate, String genre,
                 String rating, String description, String director, String stars) {
        this.movie_name = movie_name;
        this.certificate = certificate;
        this.genre = genre;
        this.description = description;
        this.director = director;
        this.stars = stars;

        try {
            this.rating = Integer.parseInt(rating);
        } catch (NumberFormatException e) {
            this.rating = 0; // default value if parsing fails
        }

        try {
            this.year = Integer.parseInt(year);
        } catch (NumberFormatException e) {
            this.year = 0; // default value if parsing fails
        }
    }

    @Override
    public String toString() {
        return "Movie [movie_name=" + movie_name + ", year=" + year 
                        + ", certificate=" + certificate + ", genre=" + genre 
                        + ", rating=" + rating + ", director=" + director 
                        + ", stars=" + stars + ", description=" + description + "]";
    }
}


public class Interpreter implements MovStmt.Visitor<Object>, MovCond.Visitor<Void> {

    public List<Movie> actionDB = new ArrayList<>();
    public List<Movie> adventureDB = new ArrayList<>();
    public List<Movie> animationDB = new ArrayList<>();
    public List<Movie> biographyDB = new ArrayList<>();
    public List<Movie> crimeDB = new ArrayList<>();
    public List<Movie> familyDB = new ArrayList<>();
    public List<Movie> fantasyDB = new ArrayList<>();
    public List<Movie> film_noirDB = new ArrayList<>();
    public List<Movie> historyDB = new ArrayList<>();
    public List<Movie> horrorDB = new ArrayList<>();
    public List<Movie> mysteryDB = new ArrayList<>();
    public List<Movie> romanceDB = new ArrayList<>();
    public List<Movie> scifiDB = new ArrayList<>();
    public List<Movie> sportsDB = new ArrayList<>();
    public List<Movie> thrillerDB = new ArrayList<>();
    public List<Movie> warDB = new ArrayList<>();

    List<Movie>[] allMoviesDB = new List[] {actionDB, adventureDB, animationDB, biographyDB,
                                    crimeDB, familyDB, fantasyDB, film_noirDB,
                                    historyDB, horrorDB, mysteryDB, romanceDB,
                                    scifiDB, sportsDB, thrillerDB, warDB };

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
                        
        for (int i = 0; i < allMoviesDB.length; i++) {
            allMoviesDB[i] = loadDB(allMoviesDB[i], allDataSources[i]);
            System.out.println("Loaded " + allMoviesDB[i].size() + " movies into database " + i);

        }
        
    }

    public List<Movie> loadDB(List<Movie> db, DataSource ds) {
        ds.load();
        ds.printUsageString();

        List<Movie> movies = ds.fetchList(Movie.class, "movie_name", "year", "certificate", "genre", 
                                                    "rating", "description", "director", "star");

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

    private void execute(MovStmt stmt) {
        stmt.accept(this);
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
    public Object visitFindSMovStmt(FindS movstmt) {

        System.out.println("Finding " + movstmt.kind + " " + movstmt.identifier.lexeme
                + " with condition " + movstmt.condition);

        if(movstmt.kind == Kind.MOVIES) {
            List<Movie> foundMovie = new ArrayList<>();

            switch(movstmt.query) {

                case STARRING:

                    for (int i = 0; i < allMoviesDB.length; i++) {
                        for (int j = 0; j < allMoviesDB[i].size(); j++) {
                            System.out.println("Checking movie: " + allMoviesDB[i].get(j));
                            if (allMoviesDB[i].get(i).stars.contains(movstmt.identifier.lexeme)) {
                                foundMovie.add(allMoviesDB[i].get(i));
                            }
                        }
                    }
                    break;

            }
            
            System.out.println("Found movies: " + foundMovie);
            return foundMovie;
        } else {
            System.out.println("Unknown kind: " + movstmt.kind);
        }

        return "find interpreted";
    }

    @Override
    public Object visitHaveSMovStmt(HaveS movstmt) {
        
        System.out.println("Checking if have " + movstmt.identifier.lexeme + " " + movstmt.statement);

        return "have interpreted";

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
    
}
