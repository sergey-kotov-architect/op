package com.sergeykotov.op.service;

import com.sergeykotov.op.dao.ActorDao;
import com.sergeykotov.op.dao.ResultCode;
import com.sergeykotov.op.domain.Actor;
import com.sergeykotov.op.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ActorService {
    private static final Logger log = LoggerFactory.getLogger(ActorService.class);

    private final ActorDao actorDao;
    private final CacheManager cacheManager;

    @Autowired
    public ActorService(ActorDao actorDao, CacheManager cacheManager) {
        this.actorDao = actorDao;
        this.cacheManager = cacheManager;
    }

    public Actor create(Actor actor) {
        log.info("creating actor, name: {}, note: {}", actor.getName(), actor.getNote());
        boolean created;
        try {
            created = actorDao.create(actor);
        } catch (SQLException e) {
            if (e.getErrorCode() == ResultCode.SQLITE_CONSTRAINT.getCode()) {
                String message = "failed to create actor " + actor + " due to unique constraint";
                log.error(message, e);
                throw new InvalidDataException(message, e);
            }
            String message = "failed to create actor " + actor + ", error code " + e.getErrorCode();
            log.error(message, e);
            throw new DatabaseException(message, e);
        }
        if (!created) {
            String message = "failed to create actor " + actor;
            log.error(message);
            throw new DatabaseException(message);
        }
        log.info("actor {} has been created", actor);
        try {
            return getAll().stream()
                    .filter(o -> o.getName().equals(actor.getName()))
                    .findAny()
                    .orElseThrow(NotFoundException::new);
        } catch (Exception e) {
            log.error("failed to return created actor " + actor, e);
            return actor;
        }
    }

    public List<Actor> getAll() {
        try {
            return actorDao.getAll();
        } catch (SQLException e) {
            String message = "failed to extract actors, error code " + e.getErrorCode();
            log.error(message, e);
            throw new ExtractionException(message, e);
        }
    }

    public Actor getById(long id) {
        return getAll().stream().filter(a -> a.getId() == id).findAny().orElseThrow(NotFoundException::new);
    }

    public void updateById(long id, Actor actor) {
        log.info("updating actor by ID {}, name: {}, note: {}", id, actor.getName(), actor.getNote());
        boolean updated;
        try {
            updated = actorDao.updateById(id, actor);
        } catch (SQLException e) {
            if (e.getErrorCode() == ResultCode.SQLITE_CONSTRAINT.getCode()) {
                String message = "failed to update actor by ID " + id + " due to unique constraint";
                log.error(message, e);
                throw new InvalidDataException(message, e);
            }
            String message = "failed to update actor by ID " + id + ", error code " + e.getErrorCode();
            log.error(message, e);
            throw new DatabaseException(message, e);
        }
        if (!updated) {
            String message = "failed to update actor by ID " + id;
            log.error(message);
            throw new InvalidDataException(message);
        }
        log.info("actor has been updated by ID {}", id);
        cacheManager.getCacheNames().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
    }

    public void deleteById(long id) {
        if (ScheduleService.generating.get()) {
            throw new ModificationException();
        }
        log.info("deleting actor by ID {}", id);
        boolean deleted;
        try {
            deleted = actorDao.deleteById(id);
        } catch (SQLException e) {
            if (e.getErrorCode() == ResultCode.SQLITE_CONSTRAINT.getCode()) {
                String message = "failed to delete actor by ID " + id + " due to database constraints";
                log.error(message, e);
                throw new InvalidDataException(message, e);
            }
            String message = "failed to delete actor by ID " + id + ", error code " + e.getErrorCode();
            log.error(message, e);
            throw new DatabaseException(message, e);
        }
        if (!deleted) {
            String message = "failed to delete actor by ID " + id;
            log.error(message);
            throw new InvalidDataException(message);
        }
        log.info("actor has been deleted by ID {}", id);
        cacheManager.getCacheNames().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
    }
}