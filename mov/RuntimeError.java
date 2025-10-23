package mov;

public class RuntimeError extends RuntimeException {
    final MovToken token;

    RuntimeError(MovToken token, String message) {
        super(message);
        this.token = token;
    }

    @Override
    public String getMessage() {
        return "Runtime error at " + token + ": " + super.getMessage();
    }
}