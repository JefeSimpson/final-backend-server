package com.github.jefesimpson.service;

import com.github.jefesimpson.model.Blog;
import com.github.jefesimpson.model.ModelAccess;
import com.github.jefesimpson.model.User;

import java.util.List;

public interface UserServiceFunction extends Service<User>{
    User authenticate(String login, String password);
    boolean loginExist(String login);
    User findByLogin(String login);
    List<ModelAccess> access(User user, User target);
    List<ModelAccess> access(User user, Blog blog);
    
}
