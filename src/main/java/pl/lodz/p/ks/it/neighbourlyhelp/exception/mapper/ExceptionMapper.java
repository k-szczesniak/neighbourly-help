package pl.lodz.p.ks.it.neighbourlyhelp.exception.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;

@ControllerAdvice
public class ExceptionMapper extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AppBaseException.class)
    public final ResponseEntity<ErrorResponse> handleAppBaseExceptions(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getClass().getSimpleName(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public final ResponseEntity<ErrorResponse> allExceptions(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getClass().getSimpleName(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @Data
    @AllArgsConstructor
    private static class ErrorResponse {
        private String errorOrigin;
        private String message;
    }
}
