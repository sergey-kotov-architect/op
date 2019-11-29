package com.sergeykotov.op.controller;

import com.sergeykotov.op.domain.Op;
import com.sergeykotov.op.service.AuthorizationService;
import com.sergeykotov.op.service.OpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/op")
public class OpController {
    private final AuthorizationService authorizationService;
    private final OpService opService;

    @Autowired
    public OpController(AuthorizationService authorizationService, OpService opService) {
        this.authorizationService = authorizationService;
        this.opService = opService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Op create(@RequestHeader String authorization, @RequestBody @Valid Op op) {
        authorizationService.authorize(authorization);
        return opService.create(op);
    }

    @PostMapping("/list")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Op> createList(@RequestHeader String authorization, @RequestBody @Valid List<Op> ops) {
        authorizationService.authorize(authorization);
        return opService.create(ops);
    }

    @GetMapping
    public List<Op> getAll(@RequestHeader String authorization) {
        authorizationService.authorize(authorization);
        return opService.getAll();
    }

    @GetMapping("/{id}")
    public Op getById(@RequestHeader String authorization, @PathVariable long id) {
        authorizationService.authorize(authorization);
        return opService.getById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateById(@RequestHeader String authorization, @PathVariable long id, @RequestBody @Valid Op op) {
        authorizationService.authorize(authorization);
        opService.updateById(id, op);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateList(@RequestHeader String authorization, @RequestBody @Valid List<Op> ops) {
        authorizationService.authorize(authorization);
        opService.updateByUser(ops);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@RequestHeader String authorization, @PathVariable long id) {
        authorizationService.authorize(authorization);
        opService.deleteById(id);
    }

    @DeleteMapping("/list")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteList(@RequestHeader String authorization, @RequestBody long[] ids) {
        authorizationService.authorize(authorization);
        opService.deleteList(ids);
    }

    @DeleteMapping
    public String deleteUnscheduled(@RequestHeader String authorization) {
        authorizationService.authorize(authorization);
        return opService.deleteUnscheduled();
    }
}