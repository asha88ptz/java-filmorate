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
    public Film create(@RequestBody Film film) {
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
    public Film update(@RequestBody Film film) {
        log.debug("Got request to endpoint 'PUT /films'.");
        try {
            if (film.getId() == null) {
                throw new MissingIdException("Film Id is missing.");
            }
            if (!films.containsKey(film.getId())) {
                throw new NotFoundException("Not found film with id " + film.getId() + " .");
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
            throw new ValidationException("The Film name shouldn't be blank.");
        }
        if (film.getDescription().length() > Film.MAX_DESCRIPTION_LENGTH) {
            throw new ValidationException("The length of the Film description is longer than allowed.");
        }
        if (film.getReleaseDate().isBefore(Film.MIN_RELEASE_DATE)) {
            throw new ValidationException("The Film's release date is too early.");
        }
        if (film.getDuration() == null || film.getDuration() < 0) {
            throw new ValidationException("The duration of the Film should be positive.");
        }
    }
}
