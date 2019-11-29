package com.sergeykotov.op.service;

import com.sergeykotov.op.dao.OpDao;
import com.sergeykotov.op.domain.Op;
import com.sergeykotov.op.exception.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpService {
    private static final Logger log = Logger.getLogger(OpService.class);

    private final OpDao opDao;

    @Autowired
    public OpService(OpDao opDao) {
        this.opDao = opDao;
    }

    public Op create(Op op) {
        if (ScheduleService.generating.get()) {
            throw new ModificationException();
        }
        log.info("creating operation " + op + "... ");
        boolean created;
        try {
            created = opDao.create(op);
        } catch (SQLException e) {
            String message = "failed to create operation " + op + ", error code: " + e.getErrorCode();
            log.error(message, e);
            throw new InvalidDataException(message, e);
        }
        if (!created) {
            String message = "failed to create operation " + op;
            log.error(message);
            throw new InvalidDataException(message);
        }
        log.info("operation " + op + " has been created");
        try {
            return getAll().stream()
                    .filter(o -> o.getName().equals(op.getName()))
                    .findAny()
                    .orElseThrow(NotFoundException::new);
        } catch (Exception e) {
            log.error("failed to return created operation " + op, e);
            return op;
        }
    }

    public List<Op> create(List<Op> ops) {
        if (ScheduleService.generating.get()) {
            throw new ModificationException();
        }
        log.info("creating operations " + ops + "...");
        boolean created;
        try {
            created = opDao.create(ops);
        } catch (SQLException e) {
            String message = "failed to create operations " + ops + ", error code: " + e.getErrorCode();
            log.error(message, e);
            throw new InvalidDataException(message, e);
        }
        if (!created) {
            String message = "failed to create operations " + ops;
            log.error(message);
            throw new InvalidDataException(message);
        }
        log.info("operations have been created: " + ops);
        try {
            return getAll().stream().filter(ops::contains).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("failed to return created operations " + ops, e);
            return ops;
        }
    }

    public List<Op> getAll() {
        try {
            return opDao.getAll();
        } catch (SQLException e) {
            String message = "failed to extract operations, error code: " + e.getErrorCode();
            log.error(message, e);
            throw new ExtractionException(message, e);
        }
    }

    public List<Op> getScheduled() {
        try {
            return opDao.getScheduled();
        } catch (SQLException e) {
            String message = "failed to extract scheduled operations, error code: " + e.getErrorCode();
            log.error(message, e);
            throw new ExtractionException(message, e);
        }
    }

    public Op getById(long id) {
        return getAll().stream().filter(o -> o.getId() == id).findAny().orElseThrow(NotFoundException::new);
    }

    public void updateById(long id, Op op) {
        if (ScheduleService.generating.get()) {
            throw new ModificationException();
        }
        log.info("updating operation by ID " + id + "...");
        boolean updated;
        try {
            updated = opDao.updateById(id, op);
        } catch (SQLException e) {
            String message = "failed to update operation by ID " + id + ", error code: " + e.getErrorCode();
            log.error(message, e);
            throw new InvalidDataException(message, e);
        }
        if (!updated) {
            String message = "failed to update operation by ID " + id;
            log.error(message);
            throw new InvalidDataException(message);
        }
        log.info("operation has been updated by ID " + id);
    }

    public void updateByUser(List<Op> ops) {
        if (ScheduleService.generating.get()) {
            throw new ModificationException();
        }
        update(ops);
    }

    public void update(List<Op> ops) {
        log.info("updating operations " + ops + "...");
        boolean updated;
        try {
            updated = opDao.update(ops);
        } catch (SQLException e) {
            String message = "failed to update operations " + ops + ", error code: " + e.getErrorCode();
            log.error(message, e);
            throw new InvalidDataException(message, e);
        }
        if (!updated) {
            String message = "failed to update operations " + ops;
            log.error(message);
            throw new InvalidDataException(message);
        }
        log.info("operations have been updated: " + ops);
    }

    public void deleteById(long id) {
        if (ScheduleService.generating.get()) {
            throw new ModificationException();
        }
        log.info("deleting operation by ID " + id + "...");
        boolean deleted;
        try {
            deleted = opDao.deleteById(id);
        } catch (SQLException e) {
            String message = "failed to delete operation by ID " + id + ", error code: " + e.getErrorCode();
            log.error(message, e);
            throw new InvalidDataException(message, e);
        }
        if (!deleted) {
            String message = "failed to delete operation by ID " + id;
            log.error(message);
            throw new InvalidDataException(message);
        }
        log.info("operation has been deleted by ID " + id);
    }

    public String deleteUnscheduled() {
        if (ScheduleService.generating.get()) {
            throw new ModificationException();
        }
        log.info("deleting unscheduled operations...");
        int count;
        try {
            count = opDao.deleteUnscheduled();
        } catch (SQLException e) {
            String message = "failed to delete unscheduled operations, error code: " + e.getErrorCode();
            log.error(message, e);
            throw new DatabaseException(message, e);
        }
        String message = count + " unscheduled operations have been deleted";
        log.info(message);
        return message;
    }
}