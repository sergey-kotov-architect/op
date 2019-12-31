package com.sergeykotov.op.service;

import com.sergeykotov.op.dao.OpDao;
import com.sergeykotov.op.dao.ResultCode;
import com.sergeykotov.op.domain.Op;
import com.sergeykotov.op.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpService {
    private static final Logger log = LoggerFactory.getLogger(OpService.class);

    private final OpDao opDao;

    @Autowired
    public OpService(OpDao opDao) {
        this.opDao = opDao;
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
            if (e.getErrorCode() == ResultCode.SQLITE_CONSTRAINT.getCode()) {
                String message = "failed to create operations " + ops + " due to unique constraint";
                log.error(message, e);
                throw new InvalidDataException(message, e);
            }
            String message = "failed to create operations " + ops + ", error code: " + e.getErrorCode();
            log.error(message, e);
            throw new DatabaseException(message, e);
        }
        if (!created) {
            String message = "failed to create operations " + ops;
            log.error(message);
            throw new DatabaseException(message);
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
            String message = "failed to extract operations, error code " + e.getErrorCode();
            log.error(message, e);
            throw new ExtractionException(message, e);
        }
    }

    public Op getById(long id) {
        try {
            return opDao.getById(id).orElseThrow(NotFoundException::new);
        } catch (SQLException e) {
            String message = "failed to extract operation by ID " + id + ", error code " + e.getErrorCode();
            log.error(message, e);
            throw new ExtractionException(message, e);
        }
    }

    public List<Op> getScheduled() {
        try {
            return opDao.getScheduled();
        } catch (SQLException e) {
            String message = "failed to extract scheduled operations, error code " + e.getErrorCode();
            log.error(message, e);
            throw new ExtractionException(message, e);
        }
    }

    public List<Op> getScheduled(long actorId) {
        try {
            return opDao.getScheduled(actorId);
        } catch (SQLException e) {
            String format = "failed to extract scheduled operations by actor ID %d, error code %d";
            String message = String.format(format, actorId, e.getErrorCode());
            log.error(message, e);
            throw new ExtractionException(message, e);
        }
    }

    public void updateById(long id, Op op) {
        if (ScheduleService.generating.get()) {
            throw new ModificationException();
        }
        String opString = "name: " + op.getName() + ", note: " + op.getNote() + ", scheduled: " + op.isScheduled();
        log.info("updating operation by ID " + id + ", " + opString);
        boolean updated;
        try {
            updated = opDao.updateById(id, op);
        } catch (SQLException e) {
            if (e.getErrorCode() == ResultCode.SQLITE_CONSTRAINT.getCode()) {
                String message = "failed to update operation by ID " + id + " due to unique constraint";
                log.error(message, e);
                throw new InvalidDataException(message, e);
            }
            String message = "failed to update operation by ID " + id + ", error code " + e.getErrorCode();
            log.error(message, e);
            throw new DatabaseException(message, e);
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
            if (e.getErrorCode() == ResultCode.SQLITE_CONSTRAINT.getCode()) {
                String message = "failed to update operations " + ops + " due to unique constraint";
                log.error(message, e);
                throw new InvalidDataException(message, e);
            }
            String message = "failed to update operations " + ops + ", error code " + e.getErrorCode();
            log.error(message, e);
            throw new DatabaseException(message, e);
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
            if (e.getErrorCode() == ResultCode.SQLITE_CONSTRAINT.getCode()) {
                String message = "failed to delete operation by ID " + id + " due to database constraints";
                log.error(message, e);
                throw new InvalidDataException(message, e);
            }
            String message = "failed to delete operation by ID " + id + ", error code " + e.getErrorCode();
            log.error(message, e);
            throw new DatabaseException(message, e);
        }
        if (!deleted) {
            String message = "failed to delete operation by ID " + id;
            log.error(message);
            throw new InvalidDataException(message);
        }
        log.info("operation has been deleted by ID " + id);
    }

    public void deleteList(long[] ids) {
        if (ScheduleService.generating.get()) {
            throw new ModificationException();
        }
        String idList = Arrays.toString(ids);
        log.info("deleting operations by IDs " + idList + "...");
        boolean deleted;
        try {
            deleted = opDao.deleteList(ids);
        } catch (SQLException e) {
            if (e.getErrorCode() == ResultCode.SQLITE_CONSTRAINT.getCode()) {
                String message = "failed to delete operations by IDs " + idList + " due to database constraints";
                log.error(message, e);
                throw new InvalidDataException(message, e);
            }
            String message = "failed to delete operations by IDs " + idList + ", error code " + e.getErrorCode();
            log.error(message, e);
            throw new DatabaseException(message, e);
        }
        if (!deleted) {
            String message = "failed to delete operations by ID " + idList;
            log.error(message);
            throw new InvalidDataException(message);
        }
        log.info("operations have been deleted by IDs " + idList);
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
            if (e.getErrorCode() == ResultCode.SQLITE_CONSTRAINT.getCode()) {
                String message = "failed to delete unscheduled operations due to database constraints";
                log.error(message, e);
                throw new InvalidDataException(message, e);
            }
            String message = "failed to delete unscheduled operations, error code " + e.getErrorCode();
            log.error(message, e);
            throw new DatabaseException(message, e);
        }
        String message = count + " unscheduled operations have been deleted";
        log.info(message);
        return message;
    }
}