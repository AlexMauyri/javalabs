package ru.ssau.tk.DoubleA.javalabs.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.ssau.tk.DoubleA.javalabs.exceptions.ArrayIsNotSortedException;
import ru.ssau.tk.DoubleA.javalabs.exceptions.DifferentLengthOfArraysException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> illegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DifferentLengthOfArraysException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> differentLengthOfArraysException(DifferentLengthOfArraysException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ArrayIsNotSortedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> arrayIsNotSortedException(ArrayIsNotSortedException ex) {
        return new ResponseEntity<>("Array is not sorted", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> exception(Exception ex) {
        return new ResponseEntity<>("Unknown exception: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}