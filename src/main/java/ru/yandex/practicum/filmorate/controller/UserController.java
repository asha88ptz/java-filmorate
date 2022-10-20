package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.MissingIdException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.StorageException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private final IdGenerator idGenerator = new IdGenerator();
    @GetMapping
    public Collection<User> findAll() {
        log.debug("Got request to endpoint 'GET /users'. Current number of added users: {}.", users.size());
        return users.values();
    }

    @PostMapping
    public User add(@RequestBody User user) {
        log.debug("Got request to endpoint 'POST /users'.");
        try {
            user.setId(idGenerator.next());
            validate(user);
        } catch(StorageException | ValidationException e) {
            log.error(e.getMessage());
            throw e;
        }
        users.put(user.getId(), user);
        log.info("Add user: '{}'.", user.getName());
        return user;
    }

    @PutMapping
    public User put(@RequestBody User user) {
        log.debug("Got request to endpoint 'PUT /users'.");
        try {
            if (user.getId() == null) {
                throw new MissingIdException("Missing user id.");
            }
            if (!users.containsKey(user.getId())) {
                throw new NotFoundException("User with id " + user.getId() + " not found.");
            }
            validate(user);
        } catch( StorageException | ValidationException e) {
            log.error(e.getMessage());
            throw e;
        }
        users.put(user.getId(), user);
        log.info("Update user: '{}'.", user.getName());
        return user;
    }

    void validate(User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Invalid user email");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Invalid user login");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("The user name is missing or blank. The login will be used to display");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Invalid user birthday");
        }
    }

 }
