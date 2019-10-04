package com.sergeykotov.op.service;

import com.sergeykotov.op.dao.ActorDao;
import com.sergeykotov.op.domain.Actor;
import com.sergeykotov.op.exception.ExtractionException;
import com.sergeykotov.op.exception.InvalidDataException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ActorService {
    private static final Logger log = Logger.getLogger(ActorService.class);

    @Value("${actor.max_name_length:100}")
    private int MAX_NAME_LENGTH;

    @Value("${actor.max_note_length:4000}")
    private int MAX_NOTE_LENGTH;

    private final ActorDao actorDao;

    @Autowired
    public ActorService(ActorDao actorDao) {
        this.actorDao = actorDao;
    }

    public boolean create(Actor actor) {
        validate(actor);
        log.info("creating actor... " + actor);
        boolean created;
        try {
            created = actorDao.create(actor);
        } catch (SQLException e) {
            log.error("failed to create actor " + actor, e);
            throw new InvalidDataException();
        }
        if (!created) {
            log.error("failed to create actor " + actor);
            throw new InvalidDataException();
        }
        log.info("actor has been created: " + actor);
        return true;
    }

    public List<Actor> getAll() {
        try {
            return actorDao.getAll();
        } catch (SQLException e) {
            log.error("failed to extract actors", e);
            throw new ExtractionException();
        }
    }

    public Actor getById(long id) {
        return getAll().stream().filter(a -> a.getId() == id).findAny().orElseThrow(InvalidDataException::new);
    }

    public boolean update(Actor actor) {
        validate(actor);
        log.info("updating actor... " + actor);
        boolean updated;
        try {
            updated = actorDao.update(actor);
        } catch (SQLException e) {
            log.error("failed to update actor " + actor, e);
            throw new InvalidDataException();
        }
        if (!updated) {
            log.error("failed to update actor " + actor);
            throw new InvalidDataException();
        }
        log.info("actor has been updated: " + actor);
        return true;
    }

    public boolean deleteById(long id) {
        log.info("deleting actor by id " + id + "...");
        boolean deleted;
        try {
            deleted = actorDao.deleteById(id);
        } catch (SQLException e) {
            log.error("failed to delete actor by id " + id, e);
            throw new InvalidDataException();
        }
        if (!deleted) {
            log.error("failed to delete actor by id " + id);
            throw new InvalidDataException();
        }
        log.info("actor has been deleted by id " + id);
        return true;
    }

    private void validate(Actor actor) {
        if (actor == null) {
            throw new InvalidDataException();
        }
        String name = actor.getName();
        if (name == null || name.isEmpty() || name.length() > MAX_NAME_LENGTH) {
            throw new InvalidDataException();
        }
        String note = actor.getNote();
        if (note != null && note.length() > MAX_NOTE_LENGTH) {
            throw new InvalidDataException();
        }
    }
}