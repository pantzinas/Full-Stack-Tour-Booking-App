package gr.aueb.cf.tourapp.core;

import gr.aueb.cf.tourapp.core.exceptions.*;
import gr.aueb.cf.tourapp.dto.ResponseMessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(ValidationException ex) {
        BindingResult bindingResult = ex.getBindingResult();

        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return new  ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AppObjectAlreadyExistsException.class)
    public ResponseEntity<ResponseMessageDTO> handleConstraintViolationException(AppObjectAlreadyExistsException e) {
        return new ResponseEntity<>(new ResponseMessageDTO(e.getCode(), e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AppObjectInvalidArgumentException.class)
    public ResponseEntity<ResponseMessageDTO> handleConstraintViolationException(AppObjectInvalidArgumentException e) {
        return new ResponseEntity<>(new ResponseMessageDTO(e.getCode(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AppObjectNotAuthorizedException.class)
    public ResponseEntity<ResponseMessageDTO> handleConstraintViolationException(AppObjectNotAuthorizedException e) {
        return new ResponseEntity<>(new ResponseMessageDTO(e.getCode(), e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AppObjectNotFoundException.class)
    public ResponseEntity<ResponseMessageDTO> handleConstraintViolationException(AppObjectNotFoundException e) {
        return new ResponseEntity<>(new ResponseMessageDTO(e.getCode(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AppServerException.class)
    public ResponseEntity<ResponseMessageDTO> handleConstraintViolationException(AppServerException e) {
        return new ResponseEntity<>(new ResponseMessageDTO(e.getCode(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
