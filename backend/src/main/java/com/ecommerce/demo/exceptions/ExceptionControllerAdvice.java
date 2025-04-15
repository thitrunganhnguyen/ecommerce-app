package com.ecommerce.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(value = CustomException.class)
    public final ResponseEntity<String> handleCustomException(CustomException customException) {
        return new ResponseEntity<>(customException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = AuthentificationFailException.class)
    public final ResponseEntity<String> handleAuthentificationFailException(AuthentificationFailException authentificationFailException) {
        return new ResponseEntity<>(authentificationFailException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ProductNotExistException.class)
    public final ResponseEntity<String> handleProductNotExistException(ProductNotExistException productNotExistException) {
        return new ResponseEntity<>(productNotExistException.getMessage(), HttpStatus.BAD_REQUEST);
    }


}
