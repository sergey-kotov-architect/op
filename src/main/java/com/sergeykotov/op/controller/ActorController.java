package com.sergeykotov.op.controller;

import com.sergeykotov.op.domain.Actor;
import com.sergeykotov.op.service.ActorService;
import com.sergeykotov.op.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/actor")
public class ActorController {
    private final AuthorizationService authorizationService;
    private final ActorService actorService;

    @Autowired
    public ActorController(AuthorizationService authorizationService, ActorService actorService) {
        this.authorizationService = authorizationService;
        this.actorService = actorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Actor create(@RequestHeader String authorization, @RequestBody @Valid Actor actor) {
        authorizationService.authorize(authorization);
        return actorService.create(actor);
    }

    @GetMapping
    public List<Actor> getAll(@RequestHeader String authorization) {
        authorizationService.authorize(authorization);
        return actorService.getAll();
    }

    @GetMapping("/{id}")
    public Actor getById(@RequestHeader String authorization, @PathVariable long id) {
        authorizationService.authorize(authorization);
        return actorService.getById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateById(@RequestHeader String authorization,
                           @PathVariable long id,
                           @RequestBody @Valid Actor actor) {
        authorizationService.authorize(authorization);
        actorService.updateById(id, actor);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@RequestHeader String authorization, @PathVariable long id) {
        authorizationService.authorize(authorization);
        actorService.deleteById(id);
    }
}