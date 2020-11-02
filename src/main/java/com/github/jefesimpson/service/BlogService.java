package com.github.jefesimpson.service;

import com.github.jefesimpson.config.DatabaseUtils;
import com.github.jefesimpson.model.Blog;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;

public class BlogService implements Service<Blog>{
    @Override
    public Dao<Blog, Integer> dao(){
        try {
            return DaoManager.createDao(DatabaseUtils.connectionSource(), Blog.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("base create dao invalid");
        }
    }
}
