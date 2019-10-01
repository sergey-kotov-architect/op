package com.sergeykotov.op.service;

import com.sergeykotov.op.domain.Metrics;
import com.sergeykotov.op.domain.Op;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {
    private static final Logger log = Logger.getLogger(ScheduleService.class);
    private final OpService opService;

    @Autowired
    public ScheduleService(OpService opService) {
        this.opService = opService;
    }

    public List<Op> get() {
        return opService.getAll().stream().filter(Op::isScheduled).collect(Collectors.toList());
    }

    public String generate() {
        log.info("extracting operations for generating a schedule...");
        List<Op> ops = opService.getAll().stream().peek(o -> o.setScheduled(false)).collect(Collectors.toList());
        log.info("op count: " + ops.size());

        log.info("generating a schedule...");
        long start = System.currentTimeMillis();
        List<Op> scheduledOps = schedule(ops);
        long elapsed = System.currentTimeMillis() - start;
        log.info("schedule has been generated");

        log.info("saving " + scheduledOps.size() + " scheduled operations...");
        boolean saved = opService.update(scheduledOps);
        String result = saved ? "schedule has been generated" : "failed to generate a schedule";
        String note = result + ", elapsed " + elapsed + " milliseconds";
        if (saved) {
            log.info(note);
        } else {
            log.error(note);
        }
        return note;
    }

    public Metrics evaluateMetrics() {
        long start = System.currentTimeMillis();
        log.info("evaluating metrics...");
        List<Op> ops = get();
        Metrics metrics = new Metrics();

        //TODO: implement metrics evaluation

        long elapsed = System.currentTimeMillis() - start;
        String note = "metrics have been evaluated, elapsed " + elapsed + " milliseconds";
        metrics.setNote(note);
        log.info(note);
        return metrics;
    }

    private List<Op> schedule(List<Op> ops) {
        //TODO: implement schedule generation
        return Collections.emptyList();
    }
}