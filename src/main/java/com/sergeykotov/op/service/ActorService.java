package com.sergeykotov.op.service;

import com.sergeykotov.op.domain.Actor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActorService {
    private static final Logger log = Logger.getLogger(ActorService.class);

    public boolean create(Actor actor) {
        log.info("creating an actor " + actor);
        return false;
    }

    public List<Actor> getAll() {
        return null;
    }

    public Actor getById(long id) {
        return null;
    }

    public boolean update(Actor actor) {
        log.info("updating an actor " + actor);
        return false;
    }

    public boolean deleteById(long id) {
        log.info("deleting an actor by id " + id);
        return false;
    }
}