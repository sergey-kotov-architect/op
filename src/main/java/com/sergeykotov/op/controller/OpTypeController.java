package com.sergeykotov.op.controller;

import com.sergeykotov.op.domain.OpType;
import com.sergeykotov.op.service.OpTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public void create(@RequestBody @Valid OpType opType) {
        opTypeService.create(opType);
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
    public void update(@RequestBody @Valid OpType opType) {
        opTypeService.update(opType);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id) {
        opTypeService.deleteById(id);
    }
}