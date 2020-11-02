package com.github.jefesimpson.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.jefesimpson.config.Constants;
import com.github.jefesimpson.config.MapperFactory;
import com.github.jefesimpson.model.ModelAccess;
import com.github.jefesimpson.model.User;
import com.github.jefesimpson.service.UserService;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.InternalServerErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class UserController implements AuthorizationController<User> {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final MapperFactory mapperFactory;

    public UserController(UserService userService, MapperFactory mapperFactory) {
        this.userService = userService;
        this.mapperFactory = mapperFactory;
    }

    @Override
    public void create(Context context) {
        try {
            User user = sender(context);
            User target = mapperFactory.objectMapper(ModelAccess.CREATE).readValue(context.body(), User.class);
            LOGGER.info(String.format("Sender {%s} started to create user {%s} ", user, target));
            if(userService.access(user, target).contains(ModelAccess.CREATE)){
                userService.create(target);
                context.status(Constants.CREATED_201);
                LOGGER.info(String.format("Sender {%s} successfully created user {%s} ", user, target));
            }
            else{
                LOGGER.info(String.format("Sender {%s} is not authorized to create user {%s}. Throwing Forbidden", user, target));
                throw new ForbiddenResponse();
            }
        } catch (JsonProcessingException | SQLException e) {
            e.printStackTrace();
            throw new BadRequestResponse();
        }
    }

    @Override
    public void delete(Context context, int id) {
        try {
            User user = senderOrThrowUnauthorized(context);
            User target = userService.findById(id);
            LOGGER.info(String.format("Sender {%s} started to delete user {%s} ", user, target));

            if(userService.access(user, target).contains(ModelAccess.DELETE)){
                userService.deleteById(id);
                LOGGER.info(String.format("Sender {%s} successfully deleted user {%s} ", user, target));
                context.status(Constants.NO_CONTENT_204);
            }
            else{
                LOGGER.info(String.format("Sender {%s} is not authorized to delete user {%s}. Throwing Forbidden", user, target));
                throw new ForbiddenResponse();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new BadRequestResponse();
        }
    }

    @Override
    public void update(Context context, int id) {
        try {
            User user = senderOrThrowUnauthorized(context);
            User target = userService.findById(id);
            LOGGER.info(String.format("Sender {%s} started to update user {%s} ", user, target));

            if(userService.access(user,target).contains(ModelAccess.UPDATE)){
                context.result(mapperFactory.objectMapper(ModelAccess.UPDATE).writeValueAsString(target));

                User updated = mapperFactory.objectMapper(ModelAccess.UPDATE).readValue(context.body(), User.class);
                updated.setId(id);
                userService.update(updated);
                LOGGER.info(String.format("Sender {%s} successfully updated user {%s} ", user, target));
                context.result(mapperFactory.objectMapper(ModelAccess.UPDATE).writeValueAsString(updated));
            }
            else{
                LOGGER.info(String.format("Sender {%s} is not authorized to update user {%s}. Throwing Forbidden", user, target));
                throw new ForbiddenResponse();
            }
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
            throw new BadRequestResponse();
        }
    }

    @Override
    public void getAll(Context context) {
        try {
            User user = senderOrThrowUnauthorized(context);
            LOGGER.info(String.format("Sender {%s} started to getAll", user));

            List<User> users = (List<User>) userService.all()
                    .stream()
                    .filter(target -> userService.access(user, target).contains(ModelAccess.READ))
                    .collect(Collectors.toList());
            LOGGER.info(String.format("Sender {%s} successfully gotAll", user));
            context.result(mapperFactory.objectMapper(ModelAccess.READ).writeValueAsString(users));
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
            throw new InternalServerErrorResponse();
        }
    }

    @Override
    public void getOne(Context context, int id) {
        try {
            User user = senderOrThrowUnauthorized(context);
            User target = userService.findById(id);
            LOGGER.info(String.format("Sender {%s} started to getOne user {%s} ", user, target));

            if(userService.access(user, target).contains(ModelAccess.READ)){
                LOGGER.info(String.format("Sender {%s} successfully gotOne user {%s} ", user, target));
                context.result(mapperFactory.objectMapper(ModelAccess.READ).writeValueAsString(target));
            }
            else{
                LOGGER.info(String.format("Sender {%s} is not authorized to getOne user {%s}. Throwing Forbidden", user, target));
                throw new ForbiddenResponse();
            }
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
            throw new InternalServerErrorResponse();

        }
    }

    @Override
    public UserService userServiceFunction() {
        return userService;
    }
}
