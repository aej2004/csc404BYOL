package mov;

import core.data.*;

import static mov.MovTokenType.*;

import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.Data;

//import mov.MovCond.AndC;
import mov.MovCond.BinaryC;
import mov.MovCond.KindC;
import mov.MovCond.LtC;
import mov.MovCond.NegC;
//import mov.MovCond.OrC;
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

    public List<Movie> allMoviesDB = new ArrayList<>();

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
                        
        allMoviesDB = new ArrayList<Movie>();

        for (int i = 0; i < allDataSources.length; i++) {
            
            loadDB(allMoviesDB, allDataSources[i]);

        }

        System.out.println("Loaded " + allMoviesDB.size() + " movies into database");
        
    }

    public List<Movie> loadDB(List<Movie> db, DataSource ds) {
        ds.load();

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

    @SuppressWarnings("incomplete-switch")
    @Override
    public Object visitFindSMovStmt(FindS movstmt) {

        String search = (String) movstmt.identifier.literal;
        List<Movie> foundMovie = new ArrayList<>();

        switch (movstmt.kind) {

            case MOVIES:

                switch (movstmt.query) {

                    case STARRING:

                        for (Movie m : allMoviesDB) {
                            if(m.stars.contains(search)) {
                                foundMovie.add(m);
                            }
                        }
                        break;
                    
                    case DIRECTED_BY:

                        for (Movie m : allMoviesDB) {
                            if(m.director.contains(search)) {
                                foundMovie.add(m);
                            }
                        }
                        break;

                }
            
                System.out.println("Found movies: " + foundMovie);
                return foundMovie;

            // all of the movie are being loaded in with the rating of zero
            case RATINGS:

                double rating = -1; 

                switch (movstmt.query) {

                    case FOR:

                        for(Movie m : allMoviesDB) {
                            if (m.movie_name.equals(search)){
                                foundMovie.add(m);
                            }
                        }

                        for(Movie m : foundMovie) {
                            rating = m.rating;
                        }

                        break;

                }

                System.out.println("Found movies: " + foundMovie);
                System.out.println("Rating: " + rating);
                return rating;

            case GENRE:

                String genre = "";

                switch(movstmt.query) {

                    case FOR:

                        for(Movie m : allMoviesDB) {
                            if(m.stars.contains(search)) {
                                foundMovie.add(m);
                            }
                        }

                        for (Movie m : foundMovie) {
                            
                            if (!(genre.contains(m.genre))) {
                                genre += m.genre;
                            }

                        }

                        break;

                    case OF:

                        for (Movie m : allMoviesDB) {
                            if(m.movie_name.equals(search)) {
                                foundMovie.add(m);
                            }
                        }

                        for (Movie m : foundMovie) {

                            if (!(genre.equals(m.genre))) {
                                genre += m.genre;
                            }

                        }

                        break;

                }

                System.out.println("Genre: " + genre);
                return genre;
            
            case STARS: 

                String stars = "";

                switch (movstmt.query) {

                    case IN:

                        for (Movie m : allMoviesDB) {

                            if (m.movie_name.contains(search)) {
                                foundMovie.add(m);
                            }

                        }

                        for (Movie m : foundMovie) {

                            if (!(stars.contains(m.stars))) {
                                stars += m.stars;
                            }

                        }

                        break;

                }

                System.out.println("Stars: " + stars);
                return stars;

            default:

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

    /* 
    @Override
    public Void visitAndCMovCond(AndC movcond) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitAndCMovCond'");
    }

    @Override
    public Void visitOrCMovCond(OrC movcond) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitOrCMovCond'");
    }
    */

    @Override
    public Void visitBinaryC(BinaryC movcond) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitBinaryC'");
    }
    
}
