package ru.yandex.practicum.filmorate.exception;

public class NotFoundException extends StorageException {
    public NotFoundException(String message) {
        super(message);
    }
}
