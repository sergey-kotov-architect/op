package com.sergeykotov.op.controller;

import com.sergeykotov.op.domain.Op;
import com.sergeykotov.op.dto.Metrics;
import com.sergeykotov.op.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public List<Op> get() {
        return scheduleService.get();
    }

    @GetMapping("/{id}")
    public List<Op> getByActorId(@PathVariable long id) {
        return scheduleService.getByActorId(id);
    }

    @GetMapping("/metrics")
    public Metrics evaluateMetrics() {
        return scheduleService.evaluateMetrics();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void generate() {
        scheduleService.generate();
    }
}