package com.sergeykotov.op.service;

import com.sergeykotov.op.dao.ActorDao;
import com.sergeykotov.op.domain.Actor;
import com.sergeykotov.op.exception.InvalidDataException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ActorService {
    private static final Logger log = Logger.getLogger(ActorService.class);

    private final ActorDao actorDao;

    @Autowired
    public ActorService(ActorDao actorDao) {
        this.actorDao = actorDao;
    }

    public boolean create(Actor actor) {
        log.info("creating an actor " + actor);
        boolean created;
        try {
            created = actorDao.create(actor);
        } catch (SQLException e) {
            log.error("failed to create an actor " + actor, e);
            throw new InvalidDataException();
        }
        if (!created) {
            log.error("failed to create an actor " + actor);
            throw new InvalidDataException();
        }
        return true;
    }

    public List<Actor> getAll() {
        try {
            return actorDao.getAll();
        } catch (SQLException e) {
            log.error("failed to extract actors", e);
            throw new InvalidDataException();
        }
    }

    public Actor getById(long id) {
        return getAll().stream().filter(a -> a.getId() == id).findAny().orElseThrow(InvalidDataException::new);
    }

    public boolean update(Actor actor) {
        log.info("updating an actor " + actor);
        boolean updated;
        try {
            updated = actorDao.update(actor);
        } catch (SQLException e) {
            log.error("failed to update an actor " + actor, e);
            throw new InvalidDataException();
        }
        if (!updated) {
            log.error("failed to update an actor " + actor);
            throw new InvalidDataException();
        }
        return true;
    }

    public boolean deleteById(long id) {
        log.info("deleting an actor by id " + id);
        boolean deleted;
        try {
            deleted = actorDao.deleteById(id);
        } catch (SQLException e) {
            log.error("failed to delete an actor by id " + id, e);
            throw new InvalidDataException();
        }
        if (!deleted) {
            log.error("failed to delete an actor by id " + id);
            throw new InvalidDataException();
        }
        return true;
    }
}