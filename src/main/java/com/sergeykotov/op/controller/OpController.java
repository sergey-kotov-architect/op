package com.sergeykotov.op.controller;

import com.sergeykotov.op.domain.Op;
import com.sergeykotov.op.service.OpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/op")
public class OpController {
    private final OpService opService;

    @Autowired
    public OpController(OpService opService) {
        this.opService = opService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<Op> createList(@RequestBody @Valid List<Op> ops) {
        return opService.create(ops);
    }

    @GetMapping
    public List<Op> getAll() {
        return opService.getAll();
    }

    @GetMapping("/{id}")
    public Op getById(@PathVariable long id) {
        return opService.getById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateById(@PathVariable long id, @RequestBody @Valid Op op) {
        opService.updateById(id, op);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateList(@RequestBody @Valid List<Op> ops) {
        opService.updateByUser(ops);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable long id) {
        opService.deleteById(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteList(@RequestBody long[] ids) {
        opService.deleteList(ids);
    }

    @DeleteMapping("unscheduled")
    public String deleteUnscheduled() {
        return opService.deleteUnscheduled();
    }
}