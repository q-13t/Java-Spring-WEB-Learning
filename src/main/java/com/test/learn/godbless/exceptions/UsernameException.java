package com.test.learn.godbless.exceptions;

public class UsernameException extends Exception {

    public UsernameException() {
        super("UsernameException");
    }

    public UsernameException(String message) {
        super(message);
    }

}
