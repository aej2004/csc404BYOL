package mov;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MovTest {
    public static void main(String[] args) {
        String filePath = "mov/sample2.mov"; // Adjust the path if necessary

        try {
            // Read the content of sample.mov
            String source = Files.readString(Paths.get(filePath));

            // Create a MovScanner instance
            MovScanner scanner = new MovScanner(source);

            // Scan tokens
            List<MovToken> tokens = scanner.scanTokens();

            // Print the tokens
            for (MovToken token : tokens) {
                System.out.println(token);
            }

            MovParser mp = new MovParser(tokens);
            List<MovStmt> statements = mp.parse();

            for (MovStmt stmt : statements) {
                System.out.println(stmt);
            }

            // find movies starring "Tom Hanks"
            //System.out.println(new MovStmt.FindS(Kind.MOVIE, Query.STARRING, new MovToken(MovTokenType.STRING, "Tom Hanks", "Tom Hanks", 1), null ));

            //Interpreter interpreter = new Interpreter();
            //interpreter.interpret(statements);

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
