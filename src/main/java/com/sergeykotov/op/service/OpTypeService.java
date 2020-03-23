package com.sergeykotov.op.service;

import com.sergeykotov.op.dao.OpTypeDao;
import com.sergeykotov.op.dao.ResultCode;
import com.sergeykotov.op.domain.OpType;
import com.sergeykotov.op.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class OpTypeService {
    private static final Logger log = LoggerFactory.getLogger(OpTypeService.class);

    private final OpTypeDao opTypeDao;
    private final CacheManager cacheManager;

    @Autowired
    public OpTypeService(OpTypeDao opTypeDao, CacheManager cacheManager) {
        this.opTypeDao = opTypeDao;
        this.cacheManager = cacheManager;
    }

    public OpType create(OpType opType) {
        log.info("creating operation type, name: {}, note: {}", opType.getName(), opType.getNote());
        boolean created;
        try {
            created = opTypeDao.create(opType);
        } catch (SQLException e) {
            if (e.getErrorCode() == ResultCode.SQLITE_CONSTRAINT.getCode()) {
                String message = "failed to create operation type " + opType + " due to unique constraint";
                log.error(message, e);
                throw new InvalidDataException(message, e);
            }
            String message = "failed to create operation type " + opType + ", error code " + e.getErrorCode();
            log.error(message, e);
            throw new DatabaseException(message, e);
        }
        if (!created) {
            String message = "failed to create operation type " + opType;
            log.error(message);
            throw new DatabaseException(message);
        }
        log.info("operation type {} has been created", opType);
        try {
            return getAll().stream()
                    .filter(o -> o.getName().equals(opType.getName()))
                    .findAny()
                    .orElseThrow(NotFoundException::new);
        } catch (Exception e) {
            log.error("failed to return created operation type " + opType, e);
            return opType;
        }
    }

    public List<OpType> getAll() {
        try {
            return opTypeDao.getAll();
        } catch (SQLException e) {
            String message = "failed to extract operation types, error code " + e.getErrorCode();
            log.error(message, e);
            throw new ExtractionException(message, e);
        }
    }

    public OpType getById(long id) {
        return getAll().stream().filter(o -> o.getId() == id).findAny().orElseThrow(NotFoundException::new);
    }

    public void updateById(long id, OpType opType) {
        log.info("updating operation type by ID {}, name: {}, note: {}", id, opType.getName(), opType.getNote());
        boolean updated;
        try {
            updated = opTypeDao.updateById(id, opType);
        } catch (SQLException e) {
            if (e.getErrorCode() == ResultCode.SQLITE_CONSTRAINT.getCode()) {
                String message = "failed to update operation type by ID " + id + " due to unique constraint";
                log.error(message, e);
                throw new InvalidDataException(message, e);
            }
            String message = "failed to update operation type by ID " + id + ", error code " + e.getErrorCode();
            log.error(message, e);
            throw new DatabaseException(message, e);
        }
        if (!updated) {
            String message = "failed to update operation type by ID " + id;
            log.error(message);
            throw new InvalidDataException(message);
        }
        log.info("operation type has been updated by ID {}", id);
        cacheManager.getCacheNames().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
    }

    public void deleteById(long id) {
        if (ScheduleService.generating.get()) {
            throw new ModificationException();
        }
        log.info("deleting operation type by ID {}", id);
        boolean deleted;
        try {
            deleted = opTypeDao.deleteById(id);
        } catch (SQLException e) {
            if (e.getErrorCode() == ResultCode.SQLITE_CONSTRAINT.getCode()) {
                String message = "failed to delete operation type by ID " + id + " due to database constraints";
                log.error(message, e);
                throw new InvalidDataException(message, e);
            }
            String message = "failed to delete operation type by ID " + id + ", error code " + e.getErrorCode();
            log.error(message, e);
            throw new DatabaseException(message, e);
        }
        if (!deleted) {
            String message = "failed to delete operation type by ID " + id;
            log.error(message);
            throw new InvalidDataException(message);
        }
        log.info("operation type has been deleted by ID {}", id);
        cacheManager.getCacheNames().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
    }
}