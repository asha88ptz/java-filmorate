package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;


@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private final IdGenerator idGenerator = new IdGenerator();

    @GetMapping()
    public Collection<Film> findAll() {
        log.debug("Got request to endpoint 'GET /films'. Current number of added films: {}.", films.size());
        return films.values();
    }

    @PostMapping()
    public Film add(@RequestBody Film film) {
        log.debug("Got request to endpoint 'POST /films'.");
        try {
            film.setId(idGenerator.next());
            validate(film);
        } catch( StorageException | ValidationException e) {
            log.error(e.getMessage());
            throw e;
        }
        films.put(film.getId(), film);
        log.info("Add film: '{}'.", film.getName());
        return film;
    }

    @PutMapping()
    public Film put(@RequestBody Film film) {
        log.debug("Got request to endpoint 'PUT /films'.");
        try {
            if (film.getId() == null) {
                throw new MissingIdException("Missing film id.");
            }
            if (!films.containsKey(film.getId())) {
                throw new NotFoundException("Film with id " + film.getId() + " not found.");
            }
            validate(film);
        } catch( StorageException | ValidationException e) {
            log.error(e.getMessage());
            throw e;
        }
        films.put(film.getId(), film);
        log.info("Update film: '{}'.", film.getName());
        return film;
    }

    void validate(Film film)  {
        if (film.getName().isBlank()) {
            throw new ValidationException("Invalid film name");
        }
        if (film.getDescription().length() > Film.MAX_DESCRIPTION_LENGTH) {
            throw new ValidationException("Invalid film description");
        }
        if (film.getReleaseDate().isBefore(Film.MIN_RELEASE_DATE)) {
            throw new ValidationException("Invalid film release date");
        }
        if (film.getDuration() == null || film.getDuration() < 0) {
            throw new ValidationException("Invalid film duration");
        }
    }
}
