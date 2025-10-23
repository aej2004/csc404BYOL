package mov;

import core.data.*;

import java.util.List;

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

    public Movie(String movie_name, String year) {
        this.movie_name = movie_name;

        try {
            this.year = Integer.parseInt(year);
        } catch (NumberFormatException e) {
            this.year = 0; // default value if parsing fails
        }
    }

    @Override
    public String toString() {
        return "Movie [movie_name=" + movie_name + ", year=" + year + "]";
    }
}


public class Interpreter implements MovStmt.Visitor<Object>, MovCond.Visitor<Void> {


    Movie[] database;

    public Interpreter() {
        DataSource ds = DataSource.connect("data/action.csv");
        ds.load();
        ds.printUsageString();

        Movie[] action = ds.fetchArray(Movie.class, "movie_name", "year");
        for (Movie m : action) {
            System.out.println(m);
        }

        database = action;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitNegCMovCond'");
    }

    @Override
    public Void visitStrCMovCond(StrC movcond) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitStrCMovCond'");
    }

    @Override
    public Void visitKindCMovCond(KindC movcond) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitKindCMovCond'");
    }

    @Override
    public Void visitLtCMovCond(LtC movcond) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitLtCMovCond'");
    }

    @Override
    public Object visitFindSMovStmt(FindS movstmt) {

        System.out.println("Trying to find " + movstmt.kind + " " + movstmt.query
                + " " + movstmt.identifier.lexeme + " with condition " + movstmt.condition);

        return "answer";
    }

    @Override
    public Object visitHaveSMovStmt(HaveS movstmt) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitHaveSMovStmt'");
    }

    @Override
    public Object visitSaySMovStmt(SayS movstmt) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitSaySMovStmt'");
    }

    @Override
    public Object visitWriteSMovStmt(WriteS movstmt) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitWriteSMovStmt'");
    }
    
}
