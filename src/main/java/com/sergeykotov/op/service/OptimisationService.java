package com.sergeykotov.op.service;

import com.sergeykotov.op.domain.Actor;
import com.sergeykotov.op.domain.Op;
import com.sergeykotov.op.domain.OpType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OptimisationService {
    public List<Op> optimise(List<Op> ops) {
        List<List<Op>> schedules = getSchedules(ops);
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
                    if (schedules.isEmpty()) {
                        List<Op> newSchedule = new ArrayList<>();
                        newSchedule.add(op);
                        newSchedules.add(newSchedule);
                    } else {
                        for (List<Op> s : schedules) {
                            List<Op> newSchedule = new ArrayList<>(s);
                            newSchedule.add(op);
                            newSchedules.add(newSchedule);
                        }
                    }
                }
                schedules = newSchedules;
            }
        }
        return schedules;
    }
}