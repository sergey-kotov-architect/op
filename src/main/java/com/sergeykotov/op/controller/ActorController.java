package com.sergeykotov.op.controller;

import com.sergeykotov.op.domain.Actor;
import com.sergeykotov.op.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    @ResponseStatus(HttpStatus.CREATED)
    public Actor create(@RequestBody @Valid Actor actor) {
        return actorService.create(actor);
    }

    @GetMapping
    public List<Actor> getAll() {
        return actorService.getAll();
    }

    @GetMapping("/{id}")
    public Actor getById(@PathVariable long id) {
        return actorService.getById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateById(@PathVariable long id, @RequestBody @Valid Actor actor) {
        actorService.updateById(id, actor);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable long id) {
        actorService.deleteById(id);
    }
}