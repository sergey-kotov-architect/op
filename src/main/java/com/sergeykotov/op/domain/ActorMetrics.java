package com.sergeykotov.op.domain;

public class ActorMetrics {
    private int opCount;
    private double opCountDeviation;

    public ActorMetrics() {
    }

    public int getOpCount() {
        return opCount;
    }

    public void setOpCount(int opCount) {
        this.opCount = opCount;
    }

    public double getOpCountDeviation() {
        return opCountDeviation;
    }

    public void setOpCountDeviation(double opCountDeviation) {
        this.opCountDeviation = opCountDeviation;
    }
}