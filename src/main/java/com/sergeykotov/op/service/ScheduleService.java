package com.sergeykotov.op.service;

import com.sergeykotov.op.domain.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        log.info("extracting operations for generating a schedule...");
        List<Op> ops = opService.getAll().stream().peek(o -> o.setScheduled(false)).collect(Collectors.toList());
        log.info("op count: " + ops.size());

        log.info("generating a schedule...");
        long start = System.currentTimeMillis();
        Stream<Op> scheduledOps = schedule(ops.stream());
        long elapsed = System.currentTimeMillis() - start;
        log.info("schedule has been generated");

        log.info("saving " + scheduledOps.count() + " scheduled operations...");
        boolean saved = opService.update(scheduledOps.collect(Collectors.toList()));
        String result = saved ? "schedule has been generated" : "failed to generate a schedule";
        String note = result + ", elapsed " + elapsed + " milliseconds";
        if (saved) {
            log.info(note);
        } else {
            log.error(note);
        }
        return note;
    }

    private Stream<Op> schedule(Stream<Op> ops) {
        Stream<Stream<Op>> schedules = getSchedules(ops);
        log.info("selecting an optimal schedule from " + schedules.count() + " possible ones");
        return getOptimalSchedule(schedules);
    }

    private Stream<Op> getOptimalSchedule(Stream<Stream<Op>> schedules) {
        return schedules.min(Comparator.comparingDouble(this::getDeviation)).orElse(Stream.empty());
    }

    private double getDeviation(Stream<Op> ops) {
        Set<Actor> actors = ops.map(Op::getActor).collect(Collectors.toSet());
        int actorCount = actors.size();
        double meanOpCountPerActor = (double) ops.count() / actorCount;
        double deviationSum = 0.0;
        for (Actor actor : actors) {
            long opCountPerActor = ops.filter(o -> o.getActor().equals(actor)).count();
            double deviation = Math.abs(opCountPerActor - meanOpCountPerActor);
            deviationSum += deviation;
        }
        return deviationSum / actorCount;
    }

    private Stream<Stream<Op>> getSchedules(Stream<Op> ops) {
        //TODO: implement composing all possible schedules
        return Stream.empty();
    }
}