package com.github.jefesimpson.service;

import com.github.jefesimpson.config.DatabaseUtils;
import com.github.jefesimpson.model.Blog;
import com.github.jefesimpson.model.ModelAccess;
import com.github.jefesimpson.model.User;
import com.github.jefesimpson.model.UserRole;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BasicUserService implements UserService {
    @Override
    public User authenticate(String login, String password) {
        if(!loginExist(login)){
            throw new RuntimeException("login does not exist");
        }
        User user = findByLogin(login);
        if(BCrypt.checkpw(password, user.getPassword())){
            return user;
        }
        else{
            return null;
        }

    }

    @Override
    public boolean loginExist(String login) {
        return findByLogin(login) != null;
    }

    @Override
    public User findByLogin(String login) {
        QueryBuilder<User, Integer> queryBuilder = dao().queryBuilder();
        try {
            queryBuilder.where().eq(User.COLUMN_LOGIN, login);
            PreparedQuery<User> preparedQuery = queryBuilder.prepare();
            User user = dao().queryForFirst(preparedQuery);
            return user;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("Data exception");
        }
    }

    @Override
    public Dao<User, Integer> dao(){
        try {
            return DaoManager.createDao(DatabaseUtils.connectionSource(), User.class);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("base create dao invalid");
        }
    }

    //ADMIN - VIP,COMMON = DELETE, VIP, COMMON = READ;
    @Override
    public List<ModelAccess> access(User user, User target){
        List<ModelAccess> modelAccesses = new ArrayList<>();
        if(user==null){
            modelAccesses.add(ModelAccess.CREATE);
            return modelAccesses;
        }
        if(user.getUserRole()== UserRole.ADMIN){
            if(target.getUserRole()!=UserRole.ADMIN){
                modelAccesses.add(ModelAccess.DELETE);
            }
            modelAccesses.add(ModelAccess.CREATE);
            modelAccesses.add(ModelAccess.READ);
            modelAccesses.add(ModelAccess.UPDATE);
            return modelAccesses;
        }
        else{
            modelAccesses.add(ModelAccess.READ);
        }
        return modelAccesses;
    }

    //VIP, COMMON = READ; ADMIN = CREATE, UPDATE, DELETE, READ; VIP = READ(VIP);
    @Override
    public List<ModelAccess> access(User user, Blog blog){
        List<ModelAccess> modelAccesses = new ArrayList<>();
        if(user.getUserRole() == UserRole.ADMIN){
            modelAccesses.add(ModelAccess.READ);
            modelAccesses.add(ModelAccess.CREATE);
            modelAccesses.add(ModelAccess.UPDATE);
            modelAccesses.add(ModelAccess.DELETE);
        }
        else if(user.getUserRole() == UserRole.VIP){
            modelAccesses.add(ModelAccess.READ);
        }
        else if(user.getUserRole() == UserRole.COMMON){
            if(!blog.isVipAccess()){
                modelAccesses.add(ModelAccess.READ);
            }
        }
        return modelAccesses;
    }



}
