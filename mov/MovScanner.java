import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MovScanner {

    // Method to tokenize a line
    public static void tokenizeLine(String line){
        String[] keywords = { "find", "have", "say", "write", "where", "without", "is", "for", "in", "of",
                "with", "starring", "directed by"};
        String[] kinds = {"movies", "ratings", "genre", "stars", "year", "summary", "length", "director", "?age-appropriate"};
        
        // Split the line into tokens using regex to handle quoted strings
        Pattern pattern = Pattern.compile("\"[^\"]*\"|\\S+");
        Matcher matcher = pattern.matcher(line);
        
        while (matcher.find()) {
            String token = matcher.group();
            if (token.isEmpty()) continue;
            if (isInArray(token, keywords)){
                System.out.println("Token: Keyword: " + token);
            } else if (isInArray(token, kinds)){
                System.out.println("Token: Kind: " + token);
                // Check if the word is a string in quotes
            } else if (token.startsWith("\"") && token.endsWith("\"")) {
                System.out.println("Token: String: " + token);
               // Check if the word is a number or decimal
            } else if (token.matches("\\d+(\\.\\d+)?")) {
                System.out.println("Token: Number: " + token);
            } else if (token.equals("=")){
                System.out.println("Token: Operator: " + token);
            // If its not any of the above just treat it as an identifier
            } else {
                System.out.println("Token: Identifier: " + token);
        }
    }
}
    //Helper Method to check if a string is in an array
    public static boolean isInArray(String token, String[] array){
        for (String word : array){
            if (word.equals(token)){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        // Load .mov file
        try {
            File file = new File("sample.mov");
            Scanner scanner = new java.util.Scanner(file);
            // Read the file line by line
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                System.out.println(line);
                // Tokenize the line
                tokenizeLine(line);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");

        }
    }
}            
                
    

