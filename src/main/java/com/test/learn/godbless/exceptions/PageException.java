package com.test.learn.godbless.exceptions;

public class PageException extends Exception {
    public PageException() {
        super("PageException");
    }

    public PageException(String message) {
        super(message);
    }
}
