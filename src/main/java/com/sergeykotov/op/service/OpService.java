package com.sergeykotov.op.service;

import com.sergeykotov.op.domain.Op;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpService {
    private static final Logger log = Logger.getLogger(OpService.class);

    public boolean create(Op op) {
        log.info("creating an operation " + op);
        return false;
    }

    public List<Op> getAll() {
        return null;
    }

    public Op getById(long id) {
        return null;
    }

    public boolean update(Op op) {
        log.info("updating an operation " + op);
        return false;
    }

    public boolean update(List<Op> ops) {
        log.info("updating operations " + ops);
        return false;
    }

    public boolean deleteById(long id) {
        log.info("deleting an operation by id " + id);
        return false;
    }
}