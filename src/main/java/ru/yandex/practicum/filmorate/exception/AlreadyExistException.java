package ru.yandex.practicum.filmorate.exception;

public class AlreadyExistException extends StorageException {
    public AlreadyExistException(String message) {
        super(message);
    }
}
