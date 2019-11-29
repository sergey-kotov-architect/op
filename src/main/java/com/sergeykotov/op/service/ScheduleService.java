package com.sergeykotov.op.service;

import com.sergeykotov.op.domain.Actor;
import com.sergeykotov.op.domain.Op;
import com.sergeykotov.op.domain.OpType;
import com.sergeykotov.op.dto.ActorMetrics;
import com.sergeykotov.op.dto.Metrics;
import com.sergeykotov.op.exception.ModificationException;
import com.sergeykotov.op.task.ScheduleGenerationTask;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class ScheduleService {
    private static final Logger log = Logger.getLogger(ScheduleService.class);
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
        metrics.setGenerating(ScheduleService.generating.get());

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