package com.sergeykotov.op.controller;

import com.sergeykotov.op.domain.Op;
import com.sergeykotov.op.service.OpService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void create(@RequestBody @Valid Op op) {
        opService.create(op);
    }

    @PostMapping("/list")
    public void createList(@RequestBody @Valid List<Op> ops) {
        opService.create(ops);
    }

    @GetMapping
    public List<Op> getAll() {
        return opService.getAll();
    }

    @GetMapping("/{id}")
    public Op getById(@PathVariable long id) {
        return opService.getById(id);
    }

    @PutMapping
    public void update(@RequestBody @Valid Op op) {
        opService.update(op);
    }

    @PutMapping("/list")
    public void updateList(@RequestBody @Valid List<Op> ops) {
        opService.updateByUser(ops);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id) {
        opService.deleteById(id);
    }

    @DeleteMapping
    public String deleteUnscheduled() {
        return opService.deleteUnscheduled();
    }
}