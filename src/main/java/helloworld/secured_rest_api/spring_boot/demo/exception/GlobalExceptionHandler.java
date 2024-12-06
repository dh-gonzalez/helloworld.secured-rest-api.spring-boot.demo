package helloworld.secured_rest_api.spring_boot.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import helloworld.secured_rest_api.spring_boot.demo.common.dto.ErrorDetailsDTO;

/**
 * This class allows to handle global exception and provide the caller an explicit error message
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Global exception handler for RuntimeException
     * 
     * @param ex the RuntimeException that has been thrown
     * @return an explicit error message to the caller
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDetailsDTO handleRuntimeException(RuntimeException ex) {
        return new ErrorDetailsDTO(RuntimeException.class.getName(), ex.getMessage());
    }
}
