package com.sergeykotov.op.controller;

import com.sergeykotov.op.domain.Actor;
import com.sergeykotov.op.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/actor")
public class ActorController {
    private final ActorService actorService;

    @Autowired
    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @PostMapping
    public void create(@RequestBody Actor actor) {
        actorService.create(actor);
    }

    @GetMapping
    public List<Actor> getAll() {
        return actorService.getAll();
    }

    @GetMapping("/{id}")
    public Actor getById(@PathVariable long id) {
        return actorService.getById(id);
    }

    @PutMapping
    public void update(@RequestBody Actor actor) {
        actorService.update(actor);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id) {
        actorService.deleteById(id);
    }
}