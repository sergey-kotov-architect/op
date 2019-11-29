package com.sergeykotov.op.controller;

import com.sergeykotov.op.domain.Op;
import com.sergeykotov.op.dto.Metrics;
import com.sergeykotov.op.service.AuthorizationService;
import com.sergeykotov.op.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schedule")
public class ScheduleController {
    private final AuthorizationService authorizationService;
    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(AuthorizationService authorizationService, ScheduleService scheduleService) {
        this.authorizationService = authorizationService;
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public List<Op> get(@RequestHeader String authorization) {
        authorizationService.authorize(authorization);
        return scheduleService.get();
    }

    @GetMapping("/{id}")
    public List<Op> getByActorId(@RequestHeader String authorization, @PathVariable long id) {
        authorizationService.authorize(authorization);
        return scheduleService.getByActorId(id);
    }

    @GetMapping("/metrics")
    public Metrics evaluateMetrics(@RequestHeader String authorization) {
        authorizationService.authorize(authorization);
        return scheduleService.evaluateMetrics();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void generate(@RequestHeader String authorization) {
        authorizationService.authorize(authorization);
        scheduleService.generate();
    }

    @GetMapping("/status")
    public boolean getStatus(@RequestHeader String authorization) {
        authorizationService.authorize(authorization);
        return !ScheduleService.generating.get();
    }
}