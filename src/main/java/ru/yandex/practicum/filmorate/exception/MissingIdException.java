package ru.yandex.practicum.filmorate.exception;

public class MissingIdException extends ValidationException {
    public MissingIdException(String message) {
        super(message);
    }
}
