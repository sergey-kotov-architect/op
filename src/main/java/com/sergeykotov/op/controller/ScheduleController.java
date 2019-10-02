package com.sergeykotov.op.controller;

import com.sergeykotov.op.domain.Metrics;
import com.sergeykotov.op.domain.Op;
import com.sergeykotov.op.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/metrics")
    public Metrics evaluateMetrics() {
        return scheduleService.evaluateMetrics();
    }

    @PutMapping
    public String generate() {
        return scheduleService.generate();
    }
}