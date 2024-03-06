package ru.otus.hw.exeption;

public class FileReadExceptions extends RuntimeException {
    public FileReadExceptions(String message, Throwable ex) {
        super(message, ex);
    }

}
