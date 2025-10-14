package mov;

public class Mov {

    static boolean hasError = false;
    
    static void error(int line, String message) {
        report(line, "", message);
    }

    private static void report(int line, String where, String message) {
        System.err.println(
            "[line " + line + "] Error" + where + ": " + message);
        hasError = true;
    }

}
