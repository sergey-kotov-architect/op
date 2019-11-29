package com.sergeykotov.op.dao;

import com.sergeykotov.op.domain.Actor;
import com.sergeykotov.op.domain.Op;
import com.sergeykotov.op.domain.OpType;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class OpDao {
    private static final String CREATE_CMD = "insert into op (name, note, actor_id, op_type_id, dt, scheduled) " +
            "values (?, ?, ?, ?, ?, ?);";
    private static final String GET_CMD = "select o.id, o.name, o.note, o.actor_id, a.name as a_name, " +
            "a.note as a_note, o.op_type_id, t.name as t_name, t.note as t_note, o.dt, o.scheduled " +
            "from op o join actor a on o.actor_id = a.id join op_type t on o.op_type_id = t.id;";
    private static final String GET_SCHEDULED_CMD = "select o.id, o.name, o.note, o.actor_id, a.name as a_name, " +
            "a.note as a_note, o.op_type_id, t.name as t_name, t.note as t_note, o.dt " +
            "from op o join actor a on o.actor_id = a.id join op_type t on o.op_type_id = t.id where o.scheduled = 1;";
    private static final String UPDATE_CMD = "update op set name = ?, note = ?, actor_id = ?, op_type_id = ?, " +
            "dt = ?, scheduled = ? where id = ?";
    private static final String DELETE_CMD = "delete from op where id = ?";
    private static final String DELETE_UNSCHEDULED_CMD = "delete from op where scheduled = 0";

    public boolean create(Op op) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_CMD)) {
            preparedStatement.setString(1, op.getName());
            preparedStatement.setString(2, op.getNote());
            preparedStatement.setLong(3, op.getActor().getId());
            preparedStatement.setLong(4, op.getOpType().getId());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(op.getDate().atStartOfDay()));
            preparedStatement.setInt(6, op.isScheduled() ? 1 : 0);
            return preparedStatement.executeUpdate() == 1;
        }
    }

    public boolean create(List<Op> ops) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_CMD)) {
            for (Op op : ops) {
                preparedStatement.setString(1, op.getName());
                preparedStatement.setString(2, op.getNote());
                preparedStatement.setLong(3, op.getActor().getId());
                preparedStatement.setLong(4, op.getOpType().getId());
                preparedStatement.setTimestamp(5, Timestamp.valueOf(op.getDate().atStartOfDay()));
                preparedStatement.setInt(6, op.isScheduled() ? 1 : 0);
                preparedStatement.addBatch();
            }
            return Arrays.stream(preparedStatement.executeBatch()).sum() == ops.size();
        }
    }

    public List<Op> getAll() throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_CMD);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<Op> ops = new ArrayList<>();
            while (resultSet.next()) {
                Op op = new Op();
                op.setId(resultSet.getLong("id"));
                op.setName(resultSet.getString("name"));
                op.setNote(resultSet.getString("note"));

                Actor actor = new Actor();
                actor.setId(resultSet.getLong("actor_id"));
                actor.setName(resultSet.getString("a_name"));
                actor.setNote(resultSet.getString("a_note"));
                op.setActor(actor);

                OpType opType = new OpType();
                opType.setId(resultSet.getLong("op_type_id"));
                opType.setName(resultSet.getString("t_name"));
                opType.setNote(resultSet.getString("t_note"));
                op.setOpType(opType);

                op.setDate(resultSet.getTimestamp("dt").toLocalDateTime().toLocalDate());
                op.setScheduled(resultSet.getBoolean("scheduled"));
                ops.add(op);
            }
            return ops;
        }
    }

    public List<Op> getScheduled() throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_SCHEDULED_CMD);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<Op> ops = new ArrayList<>();
            while (resultSet.next()) {
                Op op = new Op();
                op.setId(resultSet.getLong("id"));
                op.setName(resultSet.getString("name"));
                op.setNote(resultSet.getString("note"));

                Actor actor = new Actor();
                actor.setId(resultSet.getLong("actor_id"));
                actor.setName(resultSet.getString("a_name"));
                actor.setNote(resultSet.getString("a_note"));
                op.setActor(actor);

                OpType opType = new OpType();
                opType.setId(resultSet.getLong("op_type_id"));
                opType.setName(resultSet.getString("t_name"));
                opType.setNote(resultSet.getString("t_note"));
                op.setOpType(opType);

                op.setDate(resultSet.getTimestamp("dt").toLocalDateTime().toLocalDate());
                op.setScheduled(true);
                ops.add(op);
            }
            return ops;
        }
    }

    public boolean updateById(long id, Op op) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CMD)) {
            preparedStatement.setString(1, op.getName());
            preparedStatement.setString(2, op.getNote());
            preparedStatement.setLong(3, op.getActor().getId());
            preparedStatement.setLong(4, op.getOpType().getId());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(op.getDate().atStartOfDay()));
            preparedStatement.setInt(6, op.isScheduled() ? 1 : 0);
            preparedStatement.setLong(7, id);
            return preparedStatement.executeUpdate() == 1;
        }
    }

    public boolean update(List<Op> ops) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CMD)) {
            for (Op op : ops) {
                preparedStatement.setString(1, op.getName());
                preparedStatement.setString(2, op.getNote());
                preparedStatement.setLong(3, op.getActor().getId());
                preparedStatement.setLong(4, op.getOpType().getId());
                preparedStatement.setTimestamp(5, Timestamp.valueOf(op.getDate().atStartOfDay()));
                preparedStatement.setInt(6, op.isScheduled() ? 1 : 0);
                preparedStatement.setLong(7, op.getId());
                preparedStatement.addBatch();
            }
            return Arrays.stream(preparedStatement.executeBatch()).sum() == ops.size();
        }
    }

    public boolean deleteById(long id) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CMD)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() == 1;
        }
    }

    public int deleteUnscheduled() throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_UNSCHEDULED_CMD)) {
            return preparedStatement.executeUpdate();
        }
    }
}