package com.ecommerce.demo.exceptions;

public class AuthentificationFailException extends IllegalArgumentException{
    public AuthentificationFailException(String msg) {
        super(msg);
    }
}
