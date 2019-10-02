package com.sergeykotov.op.dao;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {
    private static final BasicDataSource dataSource = new BasicDataSource();

    static {
        dataSource.setUrl("jdbc:sqlite:src/main/resources/schedule.sqlite");
    }

    private ConnectionPool() {
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}