package com.test.learn.godbless.exceptions;

public class PasswordException extends Exception {

    public PasswordException() {
        super("PasswordException");
    }

    public PasswordException(String message) {
        super(message);
    }

}
