package com.sergeykotov.op.domain;

import java.util.HashMap;
import java.util.Map;

public class Metrics {
    private int opTypeCount;
    private int actorCount;
    private int opCount;
    private double meanOpCount;
    private double meanOpCountDeviation;
    private Map<Actor, ActorMetrics> actorMetricsMap = new HashMap<>();
    private String note;

    public Metrics() {
    }

    public int getOpTypeCount() {
        return opTypeCount;
    }

    public void setOpTypeCount(int opTypeCount) {
        this.opTypeCount = opTypeCount;
    }

    public int getActorCount() {
        return actorCount;
    }

    public void setActorCount(int actorCount) {
        this.actorCount = actorCount;
    }

    public int getOpCount() {
        return opCount;
    }

    public void setOpCount(int opCount) {
        this.opCount = opCount;
    }

    public double getMeanOpCount() {
        return meanOpCount;
    }

    public void setMeanOpCount(double meanOpCount) {
        this.meanOpCount = meanOpCount;
    }

    public double getMeanOpCountDeviation() {
        return meanOpCountDeviation;
    }

    public void setMeanOpCountDeviation(double meanOpCountDeviation) {
        this.meanOpCountDeviation = meanOpCountDeviation;
    }

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