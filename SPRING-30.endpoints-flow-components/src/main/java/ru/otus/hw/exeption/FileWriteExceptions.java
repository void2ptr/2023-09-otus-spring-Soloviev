package ru.otus.hw.exeption;

public class FileWriteExceptions extends RuntimeException {
    public FileWriteExceptions(String message, Throwable ex) {
        super(message, ex);
    }

}
