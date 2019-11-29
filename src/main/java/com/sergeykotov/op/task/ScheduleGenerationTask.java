package com.sergeykotov.op.task;

import com.sergeykotov.op.domain.Op;
import com.sergeykotov.op.service.OpService;
import com.sergeykotov.op.service.OptimisationService;
import com.sergeykotov.op.service.ScheduleService;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class ScheduleGenerationTask extends Thread {
    private static final Logger log = Logger.getLogger(ScheduleGenerationTask.class);
    private static final String NAME = "schedule-generation-task";

    private final OptimisationService optimisationService;
    private final OpService opService;

    public ScheduleGenerationTask(OptimisationService optimisationService, OpService opService) {
        this.optimisationService = optimisationService;
        this.opService = opService;
        setName(NAME);
    }

    @Override
    public void run() {
        ScheduleService.generating.set(true);
        try {
            log.info("extracting operations for generating a schedule...");
            List<Op> ops = opService.getAll().stream().peek(o -> o.setScheduled(false)).collect(Collectors.toList());
            log.info("op count: " + ops.size());

            log.info("generating a schedule...");
            long start = System.currentTimeMillis();
            List<Op> scheduledOps = optimisationService.optimise(ops);
            long elapsed = System.currentTimeMillis() - start;
            log.info("schedule has been generated");

            log.info("saving " + scheduledOps.size() + " scheduled operations...");
            scheduledOps.forEach(o -> o.setScheduled(true));
            boolean saved = true;
            try {
                opService.update(ops);
            } catch (Exception e) {
                saved = false;
            }
            String result = saved ? "schedule has been generated" : "failed to generate a schedule";
            String message = result + ", elapsed " + elapsed + " milliseconds";
            if (saved) {
                log.info(message);
            } else {
                log.error(message);
            }
        } finally {
            ScheduleService.generating.set(false);
        }
    }
}