package com.sergeykotov.op.dto;

public class ActorMetrics {
    private String name;
    private long opCount;
    private double opCountDeviation;

    public ActorMetrics() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getOpCount() {
        return opCount;
    }

    public void setOpCount(long opCount) {
        this.opCount = opCount;
    }

    public double getOpCountDeviation() {
        return opCountDeviation;
    }

    public void setOpCountDeviation(double opCountDeviation) {
        this.opCountDeviation = opCountDeviation;
    }
}