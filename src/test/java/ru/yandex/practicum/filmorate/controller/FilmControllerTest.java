package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


class FilmControllerTest {
    private FilmController filmController;
    private Film film;

    @BeforeEach
    void init() {
        filmController = new FilmController();
        film = new Film();
        film.setName("film");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(1970, 1, 1));
        film.setDuration(40);
    }

    @Test
    void validateDoesntThrowException() {
        assertDoesNotThrow(() -> filmController.validate(film));
    }

    @Test
    void validateNameWhenBlankThrowsException() {
        film.setName("");
        assertThrows(ValidationException.class, () -> filmController.validate(film));
    }

    @Test
    void validateDescriptionWhenTooLongThrowsException() {
        film.setDescription("a".repeat(Film.MAX_DESCRIPTION_LENGTH + 1));
        assertThrows(ValidationException.class, () -> filmController.validate(film));
    }


    @Test
    void validateReleaseDateWhenTooEarlyThrowsException() {
        film.setReleaseDate(Film.MIN_RELEASE_DATE.minusDays(1));
        assertThrows(ValidationException.class, () -> filmController.validate(film));
    }

    @Test
    void validateNegativeDurationThrowsException() {
        film.setDuration(-1);
        assertThrows(ValidationException.class, () -> filmController.validate(film));
    }

}
