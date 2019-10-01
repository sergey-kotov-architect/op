package com.sergeykotov.op.domain;

public class ActorMetrics {
    private long opCount;
    private double opCountDeviation;

    public ActorMetrics() {
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