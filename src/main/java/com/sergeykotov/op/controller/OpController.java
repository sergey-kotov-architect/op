package com.sergeykotov.op.controller;

import com.sergeykotov.op.domain.Op;
import com.sergeykotov.op.service.OpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public boolean create(@RequestBody Op op) {
        return opService.create(op);
    }

    @PostMapping
    public boolean create(@RequestBody List<Op> ops) {
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

    @PutMapping
    public boolean update(@RequestBody Op op) {
        return opService.update(op);
    }

    @PutMapping
    public boolean update(@RequestBody List<Op> ops) {
        return opService.update(ops);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable long id) {
        return opService.deleteById(id);
    }

    @DeleteMapping
    public boolean deleteUnscheduled() {
        return opService.deleteUnscheduled();
    }
}