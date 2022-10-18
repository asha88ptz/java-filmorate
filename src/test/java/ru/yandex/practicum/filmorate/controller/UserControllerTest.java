package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;
    private User user;

    @BeforeEach
    void init() {
        userController = new UserController();
        user = new User();
        user.setEmail("user@mail");
        user.setLogin("userLogin");
        user.setName("userName");
        user.setBirthday(LocalDate.of(1970, 1, 1));
    }

    @Test
    void validateDoesntThrowException() {
        assertDoesNotThrow(() -> userController.validate(user));
    }

    @Test
    void validateEmailWhenBlankThrowsException() {
        user.setEmail("");
        assertThrows(ValidationException.class, () -> userController.validate(user));
    }

    @Test
    void validateEmailWhenWrongFormatThrowsException() {
        user.setEmail("wrongFormatEmail");
        assertThrows(ValidationException.class, () -> userController.validate(user));
    }

    @Test
    void validateLoginWhenBlankThrowsException() {
        user.setEmail("");
        assertThrows(ValidationException.class, () -> userController.validate(user));
    }

    @Test
    void validateLoginWhenWrongFormatThrowsException() {
        user.setEmail("spaces in login");
        assertThrows(ValidationException.class, () -> userController.validate(user));
    }

    @Test
    void validateNameWhenBlankDoesntThrowException() {
        user.setName("");
        assertDoesNotThrow(() -> userController.validate(user));
    }

    @Test
    void validateBirthdayWhenSetWronglyInFutureThrowsException() {
        user.setBirthday(LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, () -> userController.validate(user));
    }

}