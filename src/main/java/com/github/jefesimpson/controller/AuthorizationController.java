package com.github.jefesimpson.controller;

import com.github.jefesimpson.model.User;
import com.github.jefesimpson.service.UserServiceFunction;
import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;

public interface AuthorizationController<T> extends Controller<T> {
    UserServiceFunction userServiceFunction();
    default User sender(Context context){
        if(!context.basicAuthCredentialsExist()){
            return null;
        }
        String login = context.basicAuthCredentials().getUsername();
        String password = context.basicAuthCredentials().getPassword();
        return userServiceFunction().authenticate(login, password);
    }

    default User senderOrThrowUnauthorized(Context context){
        User user = sender(context);
        if(user == null){
            throw new UnauthorizedResponse();
        }
        return user;
    }
}
