package ir.mrmoein.quezapplication.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerProgram {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> error = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(
                (errors) -> {
                    String field = ((FieldError) errors).getField();
                    String defaultMessage = errors.getDefaultMessage();
                    error.put(field, defaultMessage);
                }
        );
        return new ResponseEntity<>(error , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> nullObject(NullPointerException e) {
        Map<String , Object> response = new HashMap<>();
        response.put("error" , e.getMessage());
        response.put("status" , HttpStatus.FORBIDDEN.value());

        return new ResponseEntity<>(response , HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RegisterFailedException.class)
    public ResponseEntity<Object> registerFailedException (RegisterFailedException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", e.getMessage());
        response.put("status", HttpStatus.FORBIDDEN.value());

        return new ResponseEntity<>(response , HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotAccessException.class)
    public ResponseEntity<Object> notAccessException (NotAccessException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", e.getMessage());
        response.put("status", HttpStatus.FORBIDDEN.value());

        return new ResponseEntity<>(response , HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotFoundRequestException.class)
    public ResponseEntity<Object> notFoundException (NotFoundRequestException e ) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", e.getMessage());
        response.put("status", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(response , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDuplicateKeyException(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

}
