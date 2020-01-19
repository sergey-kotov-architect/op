package com.sergeykotov.op.dto;

import java.util.List;

public class Metrics {
    private long dateCount;
    private int opTypeCount;
    private int actorCount;
    private int opCount;

    private double meanOpCountPerActor;
    private double meanOpCountDeviation;

    private long minOpCountPerActor;
    private long maxOpCountPerActor;
    private String minOpCountActor;
    private String maxOpCountActor;

    private String note;
    private List<ActorMetrics> actors;

    public Metrics() {
    }

    public long getDateCount() {
        return dateCount;
    }

    public void setDateCount(long dateCount) {
        this.dateCount = dateCount;
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

    public double getMeanOpCountPerActor() {
        return meanOpCountPerActor;
    }

    public void setMeanOpCountPerActor(double meanOpCountPerActor) {
        this.meanOpCountPerActor = meanOpCountPerActor;
    }

    public double getMeanOpCountDeviation() {
        return meanOpCountDeviation;
    }

    public void setMeanOpCountDeviation(double meanOpCountDeviation) {
        this.meanOpCountDeviation = meanOpCountDeviation;
    }

    public long getMinOpCountPerActor() {
        return minOpCountPerActor;
    }

    public void setMinOpCountPerActor(long minOpCountPerActor) {
        this.minOpCountPerActor = minOpCountPerActor;
    }

    public long getMaxOpCountPerActor() {
        return maxOpCountPerActor;
    }

    public void setMaxOpCountPerActor(long maxOpCountPerActor) {
        this.maxOpCountPerActor = maxOpCountPerActor;
    }

    public String getMinOpCountActor() {
        return minOpCountActor;
    }

    public void setMinOpCountActor(String minOpCountActor) {
        this.minOpCountActor = minOpCountActor;
    }

    public String getMaxOpCountActor() {
        return maxOpCountActor;
    }

    public void setMaxOpCountActor(String maxOpCountActor) {
        this.maxOpCountActor = maxOpCountActor;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<ActorMetrics> getActors() {
        return actors;
    }

    public void setActors(List<ActorMetrics> actors) {
        this.actors = actors;
    }
}