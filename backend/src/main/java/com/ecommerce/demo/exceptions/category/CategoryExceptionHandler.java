package com.ecommerce.demo.exceptions.category;

import com.ecommerce.demo.controller.CategoryController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = CategoryController.class)
public class CategoryExceptionHandler {

    @ExceptionHandler(value = DuplicateCategoryException.class)
    public final ResponseEntity<String> handleDuplicateCategoryException(DuplicateCategoryException duplicateCategoryException) {
        return new ResponseEntity<>(duplicateCategoryException.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = CategoryNotFoundException.class)
    public final ResponseEntity<String> handleCategoryNotFoundException(CategoryNotFoundException categoryNotFoundException) {
        return new ResponseEntity<>(categoryNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = CategoryNotEmptyException.class)
    public final ResponseEntity<String> handleCategoryNotEmptyException(CategoryNotEmptyException categoryNotEmptyException) {
        return new ResponseEntity<>(categoryNotEmptyException.getMessage(), HttpStatus.CONFLICT);
    }
}

