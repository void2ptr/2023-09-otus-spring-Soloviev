package ru.otus.hw.exceptions;

public class EntityTooManyException extends RuntimeException {
    public EntityTooManyException(String message) {
        super(message);
    }
}
