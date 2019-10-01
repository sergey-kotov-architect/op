package com.sergeykotov.op.controller;

import com.sergeykotov.op.domain.OpType;
import com.sergeykotov.op.service.OpTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/op_type")
public class OpTypeController {
    private final OpTypeService opTypeService;

    @Autowired
    public OpTypeController(OpTypeService opTypeService) {
        this.opTypeService = opTypeService;
    }

    @PostMapping
    public boolean create(@RequestBody OpType opType) {
        return opTypeService.create(opType);
    }

    @GetMapping
    public List<OpType> getAll() {
        return opTypeService.getAll();
    }

    @GetMapping("/{id}")
    public OpType getById(@PathVariable long id) {
        return opTypeService.getById(id);
    }

    @PutMapping
    public boolean update(@RequestBody OpType opType) {
        return opTypeService.update(opType);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable long id) {
        return opTypeService.deleteById(id);
    }
}