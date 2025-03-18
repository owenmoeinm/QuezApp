package ir.mrmoein.quezapplication.exception;

public class NotFoundRequestException extends RuntimeException{

    public NotFoundRequestException(String message) {
        super(message);
    }
}
