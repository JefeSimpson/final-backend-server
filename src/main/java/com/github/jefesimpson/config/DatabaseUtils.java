package com.github.jefesimpson.config;

import com.github.jefesimpson.config.Constants;
import com.github.jefesimpson.model.Blog;
import com.github.jefesimpson.model.User;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseUtils {
    private static ConnectionSource connectionSource;


    static{
        try {
            connectionSource = new JdbcConnectionSource(Constants.DB_PATH);

            TableUtils.createTableIfNotExists(connectionSource, User.class);
            TableUtils.createTableIfNotExists(connectionSource, Blog.class);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database initialize exception");
        }
    }

    public static ConnectionSource connectionSource() {
        if (connectionSource == null)
            throw new RuntimeException("Connection incorrect exception");
        return connectionSource;
    }

    public DatabaseUtils(){}
}
