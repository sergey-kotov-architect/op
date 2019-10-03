package com.sergeykotov.op.service;

import com.sergeykotov.op.domain.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class ScheduleService {
    private static final Logger log = Logger.getLogger(ScheduleService.class);
    private static final AtomicBoolean generating = new AtomicBoolean();
    private final OpService opService;
    private final OptimisationService optimisationService;

    @Autowired
    public ScheduleService(OpService opService, OptimisationService optimisationService) {
        this.opService = opService;
        this.optimisationService = optimisationService;
    }

    public List<Op> get() {
        return opService.getScheduled();
    }

    public List<Op> getByActorId(long id) {
        return get().stream().filter(o -> o.getActor().getId() == id).collect(Collectors.toList());
    }

    public Metrics evaluateMetrics() {
        long start = System.currentTimeMillis();
        log.info("evaluating metrics...");

        List<Op> ops = get();
        Set<Actor> actors = ops.stream().map(Op::getActor).collect(Collectors.toSet());
        Set<OpType> opTypes = ops.stream().map(Op::getOpType).collect(Collectors.toSet());
        int opCount = ops.size();
        int actorCount = actors.size();
        double meanOpCountPerActor = (double) opCount / actorCount;

        Metrics metrics = new Metrics();
        metrics.setActorCount(actorCount);
        metrics.setOpTypeCount(opTypes.size());
        metrics.setOpCount(opCount);
        metrics.setMeanOpCountPerActor(meanOpCountPerActor);

        double deviationSum = 0.0;
        for (Actor actor : actors) {
            long opCountPerActor = ops.stream().filter(o -> o.getActor().equals(actor)).count();
            double deviation = Math.abs(opCountPerActor - meanOpCountPerActor);
            deviationSum += deviation;

            ActorMetrics actorMetrics = new ActorMetrics();
            actorMetrics.setOpCount(opCountPerActor);
            actorMetrics.setOpCountDeviation(deviation);
            metrics.getActorMetricsMap().put(actor, actorMetrics);
        }
        double meanDeviation = deviationSum / actorCount;
        metrics.setMeanOpCountDeviation(meanDeviation);

        long elapsed = System.currentTimeMillis() - start;
        String note = "metrics have been evaluated, elapsed " + elapsed + " milliseconds";
        metrics.setNote(note);
        log.info(note);
        return metrics;
    }

    public String generate() {
        if (generating.get()) {
            return "schedule is being generating";
        }
        generating.set(true);
        try {
            return generateSchedule();
        } finally {
            generating.set(false);
        }
    }

    private String generateSchedule() {
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
        boolean saved = opService.update(ops);
        String result = saved ? "schedule has been generated" : "failed to generate a schedule";
        String message = result + ", elapsed " + elapsed + " milliseconds";
        if (saved) {
            log.info(message);
        } else {
            log.error(message);
        }
        return message;
    }
}