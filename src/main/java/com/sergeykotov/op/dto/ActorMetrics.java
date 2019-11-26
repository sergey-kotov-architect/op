package com.sergeykotov.op.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ActorMetrics {
    private long opCount;
    private double opCountDeviation;

    public ActorMetrics() {
    }

    @JsonProperty("op_count")
    public long getOpCount() {
        return opCount;
    }

    public void setOpCount(long opCount) {
        this.opCount = opCount;
    }

    @JsonProperty("op_count_deviation")
    public double getOpCountDeviation() {
        return opCountDeviation;
    }

    public void setOpCountDeviation(double opCountDeviation) {
        this.opCountDeviation = opCountDeviation;
    }
}