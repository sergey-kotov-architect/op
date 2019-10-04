package com.sergeykotov.op.service;

import com.sergeykotov.op.dao.OpDao;
import com.sergeykotov.op.domain.Op;
import com.sergeykotov.op.exception.InvalidDataException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Predicate;

@Service
public class OpService {
    private static final Logger log = Logger.getLogger(OpService.class);
    private static final int MAX_NAME_LENGTH = 100;
    private static final int MAX_NOTE_LENGTH = 4000;

    private final OpDao opDao;

    @Autowired
    public OpService(OpDao opDao) {
        this.opDao = opDao;
    }

    public boolean create(Op op) {
        validate(op);
        log.info("creating operation... " + op);
        boolean created;
        try {
            created = opDao.create(op);
        } catch (SQLException e) {
            log.error("failed to create operation " + op, e);
            throw new InvalidDataException();
        }
        if (!created) {
            log.error("failed to create operation " + op);
            throw new InvalidDataException();
        }
        log.info("operation has been created: " + op);
        return true;
    }

    public boolean create(List<Op> ops) {
        validate(ops);
        log.info("creating operations... " + ops);
        boolean created;
        try {
            created = opDao.create(ops);
        } catch (SQLException e) {
            log.error("failed to create operations " + ops, e);
            throw new InvalidDataException();
        }
        if (!created) {
            log.error("failed to create operations " + ops);
            throw new InvalidDataException();
        }
        log.info("operations have been created: " + ops);
        return true;
    }

    public List<Op> getAll() {
        try {
            return opDao.getAll();
        } catch (SQLException e) {
            log.error("failed to extract operations", e);
            throw new InvalidDataException();
        }
    }

    public List<Op> getScheduled() {
        try {
            return opDao.getScheduled();
        } catch (SQLException e) {
            log.error("failed to extract scheduled operations", e);
            throw new InvalidDataException();
        }
    }

    public Op getById(long id) {
        return getAll().stream().filter(o -> o.getId() == id).findAny().orElseThrow(InvalidDataException::new);
    }

    public boolean update(Op op) {
        validate(op);
        log.info("updating operation... " + op);
        boolean updated;
        try {
            updated = opDao.update(op);
        } catch (SQLException e) {
            log.error("failed to update operation " + op, e);
            throw new InvalidDataException();
        }
        if (!updated) {
            log.error("failed to update operation " + op);
            throw new InvalidDataException();
        }
        log.info("operation has been updated: " + op);
        return true;
    }

    public boolean update(List<Op> ops) {
        validate(ops);
        log.info("updating operations... " + ops);
        boolean updated;
        try {
            updated = opDao.update(ops);
        } catch (SQLException e) {
            log.error("failed to update operations " + ops, e);
            throw new InvalidDataException();
        }
        if (!updated) {
            log.error("failed to update operations " + ops);
            throw new InvalidDataException();
        }
        log.info("operations have been updated: " + ops);
        return true;
    }

    public boolean deleteById(long id) {
        log.info("deleting operation by id " + id + "...");
        boolean deleted;
        try {
            deleted = opDao.deleteById(id);
        } catch (SQLException e) {
            log.error("failed to delete operation by id " + id, e);
            throw new InvalidDataException();
        }
        if (!deleted) {
            log.error("failed to delete operation by id " + id);
            throw new InvalidDataException();
        }
        log.info("operation has been deleted by id " + id);
        return true;
    }

    public String deleteUnscheduled() {
        log.info("deleting unscheduled operations...");
        int count;
        try {
            count = opDao.deleteUnscheduled();
        } catch (SQLException e) {
            log.error("failed to delete unscheduled operations", e);
            throw new InvalidDataException();
        }
        String message = count + " unscheduled operations have been deleted";
        log.info(message);
        return message;
    }

    private void validate(List<Op> ops) {
        if (ops == null) {
            throw new InvalidDataException();
        }
        ops.forEach(this::validate);
    }

    private void validate(Op op) {
        if (op == null) {
            throw new InvalidDataException();
        }
        String name = op.getName();
        if (name == null || name.isEmpty() || name.length() > MAX_NAME_LENGTH) {
            throw new InvalidDataException();
        }
        String note = op.getNote();
        if (note != null && note.length() > MAX_NOTE_LENGTH) {
            throw new InvalidDataException();
        }
        if (op.getActor() == null) {
            throw new InvalidDataException();
        }
        if (op.getOpType() == null) {
            throw new InvalidDataException();
        }
        if (op.getDate() == null) {
            throw new InvalidDataException();
        }
        if (op.isScheduled()) {
            List<Op> scheduledOps;
            try {
                scheduledOps = getScheduled();
            } catch (Exception ignore) {
                return;
            }
            Predicate<Op> p = s -> s.getOpType().equals(op.getOpType()) && s.getDate().equals(op.getDate());
            if (scheduledOps.stream().anyMatch(p)) {
                throw new InvalidDataException();
            }
        }
    }
}