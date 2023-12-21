package it.proactivity.myinsurance.controller;

import it.proactivity.myinsurance.exception.HolderNotFoundException;
import it.proactivity.myinsurance.exception.InvalidHolderException;
import it.proactivity.myinsurance.exception.InvalidParamException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HolderRestExceptionHandler {


    @ExceptionHandler
    ResponseEntity<String> handleException(HolderNotFoundException exc){
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    ResponseEntity<String> handleException(InvalidParamException ie){
        return new ResponseEntity<>(ie.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    ResponseEntity<String> handleException(InvalidHolderException ie){
        return new ResponseEntity<>(ie.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    ResponseEntity<String> handleException(Exception ie){
        return new ResponseEntity<>(ie.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
