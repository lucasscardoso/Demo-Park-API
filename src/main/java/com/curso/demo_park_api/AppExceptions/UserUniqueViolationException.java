package com.curso.demo_park_api.AppExceptions;

public class UserUniqueViolationException extends RuntimeException {

    public UserUniqueViolationException(String message) {
        super(message);
    }
}
