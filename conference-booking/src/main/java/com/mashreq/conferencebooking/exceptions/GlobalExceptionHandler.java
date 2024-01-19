package com.mashreq.conferencebooking.exceptions;

import java.util.HashMap;
import java.util.Map;

import com.mashreq.conferencebooking.dto.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) ->{

            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String error = "Malformed JSON request: Valid time format : HH:mm";
        return buildResponseEntity(new Response(HttpStatus.BAD_REQUEST, error, ex.getLocalizedMessage()));
    }

    private ResponseEntity<Object> buildResponseEntity(Response response) {
        return new ResponseEntity<>(response, response.status());
    }

    @ExceptionHandler({InvaildBookingDateTimeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleException(InvaildBookingDateTimeException ex) {
        String error = "Invalid Request";
        return buildResponseEntity(new Response(HttpStatus.BAD_REQUEST, error, ex.getLocalizedMessage()));
    }

    @ExceptionHandler({ RoomNotAvailableException.class, RecordNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleException(Exception ex) {
        String error = "Record not found";
        return buildResponseEntity(new Response(HttpStatus.NOT_FOUND, error, ex.getLocalizedMessage()));
    }

    @ExceptionHandler({Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleException(Throwable ex) {
        String error = "Unexpected Error";
        return buildResponseEntity(new Response(HttpStatus.INTERNAL_SERVER_ERROR, error, ex.getLocalizedMessage()));
    }



}