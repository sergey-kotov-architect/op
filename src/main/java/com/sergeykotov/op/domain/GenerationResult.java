package com.sergeykotov.op.domain;

public class GenerationResult {
    private boolean generated;
    private String note;

    public GenerationResult() {
    }

    public boolean isGenerated() {
        return generated;
    }

    public void setGenerated(boolean generated) {
        this.generated = generated;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}