package com.sergeykotov.op.controller;

import com.sergeykotov.op.domain.OpType;
import com.sergeykotov.op.service.OpTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public OpType create(@RequestBody @Valid OpType opType) {
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

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateById(@PathVariable long id, @RequestBody @Valid OpType opType) {
        opTypeService.updateById(id, opType);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable long id) {
        opTypeService.deleteById(id);
    }
}