package com.sergeykotov.op.service;

import com.sergeykotov.op.domain.OpType;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpTypeService {
    private static final Logger log = Logger.getLogger(OpTypeService.class);

    public boolean create(OpType opType) {
        log.info("creating an operation type " + opType);
        return false;
    }

    public List<OpType> getAll() {
        return null;
    }

    public OpType getById(long id) {
        return null;
    }

    public boolean update(OpType opType) {
        log.info("updating an operation type " + opType);
        return false;
    }

    public boolean deleteById(long id) {
        log.info("deleting an operation type by id " + id);
        return false;
    }
}