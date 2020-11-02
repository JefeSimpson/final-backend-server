package com.github.jefesimpson.service;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public interface Service <T> {
    Dao<T, Integer> dao() throws SQLException;

    default void create(T modelObject) throws SQLException {
        dao().create(modelObject);
    }
    default List<T> all() throws SQLException {
        return dao().queryForAll();
    }

    default T findById(int id) throws SQLException {
        return dao().queryForId(id);
    }
    default void update(T modelObject) throws SQLException{
        dao().update(modelObject);
    }
    default void deleteById(int id) throws SQLException{
        dao().deleteById(id);
    }

}
