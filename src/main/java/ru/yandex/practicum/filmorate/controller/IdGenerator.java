package ru.yandex.practicum.filmorate.controller;

public class IdGenerator {
    private Integer id = 1;

    public Integer next() {
        return id++;
    }
}
