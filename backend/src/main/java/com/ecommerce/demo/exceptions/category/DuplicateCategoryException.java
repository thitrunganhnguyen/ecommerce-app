package com.ecommerce.demo.exceptions.category;

public class DuplicateCategoryException extends RuntimeException{
    public DuplicateCategoryException(String msg) {
        super(msg);
    }
}
