package com.sergeykotov.op.service;

import com.sergeykotov.op.domain.Actor;
import com.sergeykotov.op.domain.Op;
import com.sergeykotov.op.domain.OpType;
import com.sergeykotov.op.dto.ActorMetrics;
import com.sergeykotov.op.dto.Metrics;
import com.sergeykotov.op.exception.ModificationException;
import com.sergeykotov.op.task.ScheduleGenerationTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class ScheduleService {
    private static final Logger log = LoggerFactory.getLogger(ScheduleService.class);
    public static final AtomicBoolean generating = new AtomicBoolean();

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final OptimisationService optimisationService;
    private final OpService opService;

    @Autowired
    public ScheduleService(OptimisationService optimisationService, OpService opService) {
        this.optimisationService = optimisationService;
        this.opService = opService;
    }

    public List<Op> get() {
        return opService.getScheduled();
    }

    public List<Op> getByActorId(long actorId) {
        return opService.getScheduled(actorId);
    }

    @Cacheable("metrics")
    public Metrics evaluateMetrics() {
        log.info("evaluating metrics...");
        long start = System.currentTimeMillis();

        List<Op> ops = get();
        Set<Actor> actors = ops.stream().map(Op::getActor).collect(Collectors.toSet());
        Set<OpType> opTypes = ops.stream().map(Op::getOpType).collect(Collectors.toSet());
        long dateCount = ops.stream().map(Op::getDate).distinct().count();
        int opCount = ops.size();
        int actorCount = actors.size();
        double meanOpCountPerActor = (double) opCount / actorCount;

        Metrics metrics = new Metrics();
        metrics.setActorCount(actorCount);
        metrics.setOpTypeCount(opTypes.size());
        metrics.setOpCount(opCount);
        metrics.setDateCount(dateCount);
        metrics.setMeanOpCountPerActor(meanOpCountPerActor);

        long minOpCount = Long.MAX_VALUE;
        String minActor = null;
        long maxOpCount = 0;
        String maxActor = null;

        metrics.setActors(new ArrayList<>(actorCount));
        double deviationSum = 0.0;
        for (Actor actor : actors) {
            long opCountPerActor = ops.stream().filter(o -> o.getActor().equals(actor)).count();
            double deviation = Math.abs(opCountPerActor - meanOpCountPerActor);
            deviationSum += deviation;

            ActorMetrics actorMetrics = new ActorMetrics();
            actorMetrics.setName(actor.getName());
            actorMetrics.setOpCount(opCountPerActor);
            actorMetrics.setOpCountDeviation(deviation);
            metrics.getActors().add(actorMetrics);

            if (opCountPerActor < minOpCount) {
                minOpCount = opCountPerActor;
                minActor = actor.getName();
            }
            if (opCountPerActor > maxOpCount) {
                maxOpCount = opCountPerActor;
                maxActor = actor.getName();
            }
        }
        double meanDeviation = deviationSum / actorCount;
        metrics.setMeanOpCountDeviation(meanDeviation);

        metrics.setMinOpCountPerActor(minOpCount);
        metrics.setMinOpCountActor(minActor);
        metrics.setMaxOpCountPerActor(maxOpCount);
        metrics.setMaxOpCountActor(maxActor);

        long elapsed = System.currentTimeMillis() - start;
        String note = "metrics have been evaluated, elapsed " + elapsed + " milliseconds";
        metrics.setNote(note);
        log.info(note);
        return metrics;
    }

    public void generate() {
        if (generating.get()) {
            throw new ModificationException();
        }
        executorService.submit(new ScheduleGenerationTask(optimisationService, opService));
    }
}