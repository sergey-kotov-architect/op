package com.sergeykotov.op.service;

import com.sergeykotov.op.dao.OpDao;
import com.sergeykotov.op.domain.Op;
import com.sergeykotov.op.exception.InvalidDataException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class OpService {
    private static final Logger log = Logger.getLogger(OpService.class);

    private final OpDao opDao;

    @Autowired
    public OpService(OpDao opDao) {
        this.opDao = opDao;
    }

    public boolean create(Op op) {
        log.info("creating an operation " + op);
        boolean created;
        try {
            created = opDao.create(op);
        } catch (SQLException e) {
            log.error("failed to create an operation " + op, e);
            throw new InvalidDataException();
        }
        if (!created) {
            log.error("failed to create an operation " + op);
            throw new InvalidDataException();
        }
        return true;
    }

    public boolean create(List<Op> ops) {
        log.info("creating operations " + ops);
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
        log.info("updating an operation " + op);
        boolean updated;
        try {
            updated = opDao.update(op);
        } catch (SQLException e) {
            log.error("failed to update an operation " + op, e);
            throw new InvalidDataException();
        }
        if (!updated) {
            log.error("failed to update an operation " + op);
            throw new InvalidDataException();
        }
        return true;
    }

    public boolean update(List<Op> ops) {
        log.info("updating operations " + ops);
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
        return true;
    }

    public boolean deleteById(long id) {
        log.info("deleting an operation by id " + id);
        boolean deleted;
        try {
            deleted = opDao.deleteById(id);
        } catch (SQLException e) {
            log.error("failed to delete an operation by id " + id, e);
            throw new InvalidDataException();
        }
        if (!deleted) {
            log.error("failed to delete an operation by id " + id);
            throw new InvalidDataException();
        }
        return true;
    }

    public boolean deleteUnscheduled() {
        log.info("deleting unscheduled operations...");
        boolean deleted;
        try {
            deleted = opDao.deleteUnscheduled();
        } catch (SQLException e) {
            log.error("failed to delete unscheduled operations", e);
            throw new InvalidDataException();
        }
        if (!deleted) {
            log.error("failed to delete unscheduled operations");
            throw new InvalidDataException();
        }
        return true;
    }
}