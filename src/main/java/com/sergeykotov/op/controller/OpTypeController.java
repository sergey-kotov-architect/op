package com.sergeykotov.op.controller;

import com.sergeykotov.op.domain.OpType;
import com.sergeykotov.op.service.AuthorizationService;
import com.sergeykotov.op.service.OpTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/op_type")
public class OpTypeController {
    private final AuthorizationService authorizationService;
    private final OpTypeService opTypeService;

    @Autowired
    public OpTypeController(AuthorizationService authorizationService, OpTypeService opTypeService) {
        this.authorizationService = authorizationService;
        this.opTypeService = opTypeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OpType create(@RequestHeader String authorization, @RequestBody @Valid OpType opType) {
        authorizationService.authorize(authorization);
        return opTypeService.create(opType);
    }

    @GetMapping
    public List<OpType> getAll(@RequestHeader String authorization) {
        authorizationService.authorize(authorization);
        return opTypeService.getAll();
    }

    @GetMapping("/{id}")
    public OpType getById(@RequestHeader String authorization, @PathVariable long id) {
        authorizationService.authorize(authorization);
        return opTypeService.getById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateById(@RequestHeader String authorization,
                           @PathVariable long id,
                           @RequestBody @Valid OpType opType) {
        authorizationService.authorize(authorization);
        opTypeService.updateById(id, opType);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@RequestHeader String authorization, @PathVariable long id) {
        authorizationService.authorize(authorization);
        opTypeService.deleteById(id);
    }
}