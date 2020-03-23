package com.sergeykotov.op.task;

import com.sergeykotov.op.domain.Op;
import com.sergeykotov.op.service.OpService;
import com.sergeykotov.op.service.OptimisationService;
import com.sergeykotov.op.service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScheduleGenerationTask extends Thread {
    private static final Logger log = LoggerFactory.getLogger(ScheduleGenerationTask.class);
    private static final String NAME = "schedule-generation-task";

    private final OptimisationService optimisationService;
    private final OpService opService;

    @Autowired
    private CacheManager cacheManager;

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
            log.info("{} operations have been extracted", ops.size());

            log.info("generating an optimal schedule...");
            long start = System.currentTimeMillis();
            List<Op> scheduledOps = optimisationService.optimise(ops);
            long elapsed = System.currentTimeMillis() - start;
            log.info("optimal schedule has been generated in {} milliseconds", elapsed);

            log.info("saving {} scheduled operations...", scheduledOps.size());
            scheduledOps.forEach(o -> o.setScheduled(true));
            try {
                opService.update(ops);
            } catch (Exception e) {
                log.error("failed to save generated schedule");
                return;
            }
            log.info("schedule has been saved");
            cacheManager.getCacheNames().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
        } finally {
            ScheduleService.generating.set(false);
        }
    }
}