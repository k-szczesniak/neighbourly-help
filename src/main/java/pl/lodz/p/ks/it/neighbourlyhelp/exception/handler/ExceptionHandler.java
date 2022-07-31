package pl.lodz.p.ks.it.neighbourlyhelp.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppBaseException;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.AppRuntimeException;

@ControllerAdvice
@Log4j2
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(AppBaseException.class)
    public final ResponseEntity<ErrorResponse> handleAppBaseExceptions(AppBaseException ex, WebRequest request) {
        return errorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<ErrorResponse> accessDeniedException(Exception ex, WebRequest request) {
        return errorResponse(ex, HttpStatus.FORBIDDEN);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({Exception.class, RuntimeException.class})
    public final ResponseEntity<ErrorResponse> allExceptions(Exception ex, WebRequest request) {
        return errorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleException(MissingRequestHeaderException ex) {
        return errorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AppRuntimeException.class)
    public ResponseEntity<ErrorResponse> handleAppRuntimeException(AppRuntimeException ex, WebRequest request) {
        return errorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    protected ResponseEntity<ErrorResponse> errorResponse(Throwable throwable,
                                                          HttpStatus status) {
        if (null != throwable) {
            log.error("error caught: " + throwable.getMessage(), throwable);
            ErrorResponse errorResponse = new ErrorResponse(throwable.getClass().getSimpleName(), throwable.getMessage());
            return response(errorResponse, status);
        } else {
            log.error("unknown error caught in RESTController, {}", status);
            return response(null, status);
        }
    }

    protected <T> ResponseEntity<T> response(T errorResponse, HttpStatus status) {
        log.debug("Responding with a status of {}", status);
        return new ResponseEntity<>(errorResponse, status);
    }

    @Data
    @AllArgsConstructor
    private static class ErrorResponse {
        private String errorOrigin;
        private String message;
    }
}
