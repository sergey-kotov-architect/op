package com.sergeykotov.op.service;

import com.sergeykotov.op.domain.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
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
        return opService.getScheduled();
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
        List<Op> scheduledOps = schedule(ops);
        long elapsed = System.currentTimeMillis() - start;
        log.info("schedule has been generated");

        log.info("saving " + scheduledOps.size() + " scheduled operations...");
        List<Op> scheduled = scheduledOps.stream().peek(o -> o.setScheduled(true)).collect(Collectors.toList());
        boolean saved = opService.update(scheduled);
        String result = saved ? "schedule has been generated" : "failed to generate a schedule";
        String note = result + ", elapsed " + elapsed + " milliseconds";
        if (saved) {
            log.info(note);
        } else {
            log.error(note);
        }
        return note;
    }

    private List<Op> schedule(List<Op> ops) {
        List<List<Op>> schedules = getSchedules(ops);
        System.out.println("selecting an optimal schedule from " + schedules.size() + " possible ones");
        return getOptimalSchedule(schedules);
    }

    private List<Op> getOptimalSchedule(List<List<Op>> schedules) {
        return schedules.stream().min(Comparator.comparingDouble(this::getDeviation)).orElse(Collections.emptyList());
    }

    private double getDeviation(List<Op> ops) {
        Set<Actor> actors = ops.stream().map(Op::getActor).collect(Collectors.toSet());
        int actorCount = actors.size();
        double meanOpCountPerActor = (double) ops.size() / actorCount;
        double deviationSum = 0.0;
        for (Actor actor : actors) {
            long opCountPerActor = ops.stream().filter(o -> o.getActor().equals(actor)).count();
            double deviation = Math.abs(opCountPerActor - meanOpCountPerActor);
            deviationSum += deviation;
        }
        return deviationSum / actorCount;
    }

    private List<List<Op>> getSchedules(List<Op> ops) {
        List<List<Op>> schedules = new ArrayList<>();
        Set<OpType> opTypes = ops.stream().map(Op::getOpType).collect(Collectors.toSet());
        Set<LocalDate> dates = ops.stream().map(Op::getDate).collect(Collectors.toSet());
        for (OpType opType : opTypes) {
            for (LocalDate date : dates) {
                List<Op> opsForActor = ops.stream()
                        .filter(o -> o.getOpType().equals(opType) && o.getDate().equals(date))
                        .collect(Collectors.toList());
                List<List<Op>> newSchedules = new ArrayList<>();
                for (Op op : opsForActor) {
                    for (List<Op> s : schedules) {
                        List<Op> newSchedule = new ArrayList<>(s);
                        newSchedule.add(op);
                        newSchedules.add(newSchedule);
                    }
                }
                schedules = newSchedules;
            }
        }
        return schedules;
    }
}