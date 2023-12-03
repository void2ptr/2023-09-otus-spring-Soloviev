package ru.otus.hw.dao.exceptions;

public class FileReadException extends RuntimeException {
    public FileReadException(String message, Throwable ex) {
        super(message, ex);
    }
}
