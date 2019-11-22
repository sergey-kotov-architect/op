package com.sergeykotov.op.service;

import com.sergeykotov.op.dao.OpTypeDao;
import com.sergeykotov.op.domain.OpType;
import com.sergeykotov.op.exception.ExtractionException;
import com.sergeykotov.op.exception.InvalidDataException;
import com.sergeykotov.op.exception.ModificationException;
import com.sergeykotov.op.exception.NotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class OpTypeService {
    private static final Logger log = Logger.getLogger(OpTypeService.class);

    private final OpTypeDao opTypeDao;
    private final ScheduleService scheduleService;

    @Autowired
    public OpTypeService(OpTypeDao opTypeDao, ScheduleService scheduleService) {
        this.opTypeDao = opTypeDao;
        this.scheduleService = scheduleService;
    }

    public boolean create(OpType opType) {
        log.info("creating operation type... " + opType);
        boolean created;
        try {
            created = opTypeDao.create(opType);
        } catch (SQLException e) {
            log.error("failed to create operation type " + opType, e);
            throw new InvalidDataException();
        }
        if (!created) {
            log.error("failed to create operation type " + opType);
            throw new InvalidDataException();
        }
        log.info("operation type has been created");
        return true;
    }

    public List<OpType> getAll() {
        try {
            return opTypeDao.getAll();
        } catch (SQLException e) {
            log.error("failed to extract operation types", e);
            throw new ExtractionException();
        }
    }

    public OpType getById(long id) {
        return getAll().stream().filter(o -> o.getId() == id).findAny().orElseThrow(NotFoundException::new);
    }

    public boolean update(OpType opType) {
        log.info("updating operation type... " + opType);
        boolean updated;
        try {
            updated = opTypeDao.update(opType);
        } catch (SQLException e) {
            log.error("failed to update operation type " + opType, e);
            throw new InvalidDataException();
        }
        if (!updated) {
            log.error("failed to update operation type " + opType);
            throw new InvalidDataException();
        }
        log.info("operation type has been updated: " + opType);
        return true;
    }

    public boolean deleteById(long id) {
        if (scheduleService.isGenerating()) {
            throw new ModificationException();
        }
        log.info("deleting operation type by id " + id + "...");
        boolean deleted;
        try {
            deleted = opTypeDao.deleteById(id);
        } catch (SQLException e) {
            log.error("failed to delete operation type by id " + id, e);
            throw new InvalidDataException();
        }
        if (!deleted) {
            log.error("failed to delete operation type by id " + id);
            throw new InvalidDataException();
        }
        log.info("operation type has been deleted by id " + id);
        return true;
    }
}