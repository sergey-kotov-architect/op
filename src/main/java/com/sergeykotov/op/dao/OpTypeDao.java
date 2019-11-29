package com.sergeykotov.op.dao;

import com.sergeykotov.op.domain.OpType;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OpTypeDao {
    private static final String CREATE_CMD = "insert into op_type (name, note) values (?, ?);";
    private static final String GET_CMD = "select o.id, o.name, o.note from op_type o;";
    private static final String UPDATE_CMD = "update op_type set name = ?, note = ? where id = ?";
    private static final String DELETE_CMD = "delete from op_type where id = ?";

    public boolean create(OpType opType) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_CMD)) {
            preparedStatement.setString(1, opType.getName());
            preparedStatement.setString(2, opType.getNote());
            return preparedStatement.executeUpdate() == 1;
        }
    }

    public List<OpType> getAll() throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_CMD);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<OpType> opTypes = new ArrayList<>();
            while (resultSet.next()) {
                OpType opType = new OpType();
                opType.setId(resultSet.getLong("id"));
                opType.setName(resultSet.getString("name"));
                opType.setNote(resultSet.getString("note"));
                opTypes.add(opType);
            }
            return opTypes;
        }
    }

    public boolean updateById(long id, OpType opType) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CMD)) {
            preparedStatement.setString(1, opType.getName());
            preparedStatement.setString(2, opType.getNote());
            preparedStatement.setLong(3, id);
            return preparedStatement.executeUpdate() == 1;
        }
    }

    public boolean deleteById(long id) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CMD)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() == 1;
        }
    }
}