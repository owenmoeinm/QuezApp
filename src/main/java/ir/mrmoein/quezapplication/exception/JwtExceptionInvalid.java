package ir.mrmoein.quezapplication.exception;

public class JwtExceptionInvalid extends RuntimeException {
    public JwtExceptionInvalid(String message) {
        super(message);
    }
}
