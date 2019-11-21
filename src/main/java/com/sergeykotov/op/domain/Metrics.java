package com.sergeykotov.op.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class Metrics {
    private int opTypeCount;
    private int actorCount;
    private int opCount;
    private double meanOpCountPerActor;
    private double meanOpCountDeviation;
    private Map<Actor, ActorMetrics> actorMetricsMap = new HashMap<>();
    private String note;

    public Metrics() {
    }

    @JsonProperty("op_type_count")
    public int getOpTypeCount() {
        return opTypeCount;
    }

    public void setOpTypeCount(int opTypeCount) {
        this.opTypeCount = opTypeCount;
    }

    @JsonProperty("actor_count")
    public int getActorCount() {
        return actorCount;
    }

    public void setActorCount(int actorCount) {
        this.actorCount = actorCount;
    }

    @JsonProperty("op_count")
    public int getOpCount() {
        return opCount;
    }

    public void setOpCount(int opCount) {
        this.opCount = opCount;
    }

    @JsonProperty("mean_op_count_per_actor")
    public double getMeanOpCountPerActor() {
        return meanOpCountPerActor;
    }

    public void setMeanOpCountPerActor(double meanOpCountPerActor) {
        this.meanOpCountPerActor = meanOpCountPerActor;
    }

    @JsonProperty("mean_op_count_deviation")
    public double getMeanOpCountDeviation() {
        return meanOpCountDeviation;
    }

    public void setMeanOpCountDeviation(double meanOpCountDeviation) {
        this.meanOpCountDeviation = meanOpCountDeviation;
    }

    @JsonProperty("actor_metrics_map")
    public Map<Actor, ActorMetrics> getActorMetricsMap() {
        return actorMetricsMap;
    }

    public void setActorMetricsMap(Map<Actor, ActorMetrics> actorMetricsMap) {
        this.actorMetricsMap = actorMetricsMap;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}