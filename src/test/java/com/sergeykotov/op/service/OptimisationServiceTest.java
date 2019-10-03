package com.sergeykotov.op.service;

import com.sergeykotov.op.domain.Actor;
import com.sergeykotov.op.domain.Op;
import com.sergeykotov.op.domain.OpType;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OptimisationServiceTest {
    private OptimisationService optimisationService;

    @Before
    public void setUp() {
        optimisationService = new OptimisationService();
    }

    @Test
    public void optimise() {
        Actor actor1 = new Actor();
        actor1.setId(1);
        actor1.setName("1");
        Actor actor2 = new Actor();
        actor2.setId(2);
        actor2.setName("2");
        Actor actor3 = new Actor();
        actor3.setId(3);
        actor3.setName("3");

        OpType opType1 = new OpType();
        opType1.setId(1);
        opType1.setName("1");
        OpType opType2 = new OpType();
        opType2.setId(2);
        opType2.setName("2");
        OpType opType3 = new OpType();
        opType3.setId(3);
        opType3.setName("3");

        LocalDate date = LocalDate.now();

        Op op11 = new Op();
        op11.setId(1);
        op11.setName("11");
        op11.setActor(actor1);
        op11.setOpType(opType1);
        op11.setDate(date);
        Op op21 = new Op();
        op21.setId(2);
        op21.setName("21");
        op21.setActor(actor2);
        op21.setOpType(opType1);
        op21.setDate(date);
        Op op22 = new Op();
        op22.setId(3);
        op22.setName("22");
        op22.setActor(actor2);
        op22.setOpType(opType2);
        op22.setDate(date);
        Op op32 = new Op();
        op32.setId(4);
        op32.setName("32");
        op32.setActor(actor3);
        op32.setOpType(opType2);
        op32.setDate(date);
        Op op33 = new Op();
        op33.setId(5);
        op33.setName("33");
        op33.setActor(actor3);
        op33.setOpType(opType3);
        op33.setDate(date);

        List<Op> ops = new ArrayList<>();
        ops.add(op11);
        ops.add(op21);
        ops.add(op22);
        ops.add(op32);
        ops.add(op33);

        List<Op> expectedSchedule = new ArrayList<>();
        expectedSchedule.add(op11);
        expectedSchedule.add(op22);
        expectedSchedule.add(op33);

        assertEquals(expectedSchedule, optimisationService.optimise(ops));
    }
}