package ru.yandex.practicum.filmorate.exception;

public class MissingIdException extends StorageException {
    public MissingIdException(String message) {
        super(message);
    }
}
