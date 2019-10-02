package com.sergeykotov.op.service;

import com.sergeykotov.op.dao.OpTypeDao;
import com.sergeykotov.op.domain.OpType;
import com.sergeykotov.op.exception.InvalidDataException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class OpTypeService {
    private static final Logger log = Logger.getLogger(OpTypeService.class);

    private final OpTypeDao opTypeDao;

    @Autowired
    public OpTypeService(OpTypeDao opTypeDao) {
        this.opTypeDao = opTypeDao;
    }

    public boolean create(OpType opType) {
        log.info("creating an operation type " + opType);
        boolean created;
        try {
            created = opTypeDao.create(opType);
        } catch (SQLException e) {
            log.error("failed to create an operation type " + opType, e);
            throw new InvalidDataException();
        }
        if (!created) {
            log.error("failed to create an operation type " + opType);
            throw new InvalidDataException();
        }
        return true;
    }

    public List<OpType> getAll() {
        try {
            return opTypeDao.getAll();
        } catch (SQLException e) {
            log.error("failed to extract operation types", e);
            throw new InvalidDataException();
        }
    }

    public OpType getById(long id) {
        return getAll().stream().filter(o -> o.getId() == id).findAny().orElseThrow(InvalidDataException::new);
    }

    public boolean update(OpType opType) {
        log.info("updating an operation type " + opType);
        boolean updated;
        try {
            updated = opTypeDao.update(opType);
        } catch (SQLException e) {
            log.error("failed to update an operation type " + opType, e);
            throw new InvalidDataException();
        }
        if (!updated) {
            log.error("failed to update an operation type " + opType);
            throw new InvalidDataException();
        }
        return true;
    }

    public boolean deleteById(long id) {
        log.info("deleting an operation type by id " + id);
        boolean deleted;
        try {
            deleted = opTypeDao.deleteById(id);
        } catch (SQLException e) {
            log.error("failed to delete an operation type by id " + id, e);
            throw new InvalidDataException();
        }
        if (!deleted) {
            log.error("failed to delete an operation type by id " + id);
            throw new InvalidDataException();
        }
        return true;
    }
}