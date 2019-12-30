package com.sergeykotov.op.domain;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;

public class Op {
    private long id;

    @Size(max = 255)
    @NotEmpty
    private String name;

    @Size(max = 4000)
    private String note;

    @NotNull
    private Actor actor;

    @NotNull
    private OpType opType;

    @NotNull
    private LocalDate date;

    private boolean scheduled;

    public Op() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public OpType getOpType() {
        return opType;
    }

    public void setOpType(OpType opType) {
        this.opType = opType;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isScheduled() {
        return scheduled;
    }

    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Op op = (Op) o;
        return Objects.equals(getActor(), op.getActor()) &&
                Objects.equals(getOpType(), op.getOpType()) &&
                Objects.equals(getDate(), op.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getActor(), getOpType(), getDate());
    }

    @Override
    public String toString() {
        return getName();
    }
}