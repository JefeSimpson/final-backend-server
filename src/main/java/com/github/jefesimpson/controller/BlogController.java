package com.github.jefesimpson.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.jefesimpson.config.Constants;
import com.github.jefesimpson.config.MapperFactory;
import com.github.jefesimpson.model.Blog;
import com.github.jefesimpson.model.ModelAccess;
import com.github.jefesimpson.model.User;
import com.github.jefesimpson.service.Service;
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

public class BlogController implements AuthorizationController<Blog> {
    private final static Logger LOGGER = LoggerFactory.getLogger(BlogController.class);
    private final UserService userService;
    private final MapperFactory mapperFactory;
    private final Service<Blog> blogService;

    public BlogController(UserService userService, MapperFactory mapperFactory, Service<Blog> blogService) {
        this.userService = userService;
        this.mapperFactory = mapperFactory;
        this.blogService = blogService;
    }

    public MapperFactory getMapper() {
        return mapperFactory;
    }

    public Service<Blog> getBlogService() {
        return blogService;
    }

    public UserService getUserServiceFunction() {
        return userService;
    }

    @Override
    public void create(Context context) {
        try {
            User user = senderOrThrowUnauthorized(context);
            Blog blog = mapperFactory.objectMapper(ModelAccess.CREATE).readValue(context.body(), Blog.class);
            LOGGER.info(String.format("Sender {%s} started to create blog {%s} ", user, blog));


            if(userService.access(user, blog).contains(ModelAccess.CREATE)){
                blogService.create(blog);
                LOGGER.info(String.format("Sender {%s} successfully created blog {%s} ", user, blog));
                context.status(Constants.CREATED_201);
            }
            else{
                LOGGER.info(String.format("Sender {%s} is not authorized to create blog {%s}. Throwing Forbidden", user, blog));
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
            Blog blog = blogService.findById(id);
            LOGGER.info(String.format("Sender {%s} started to delete blog {%s} ", user, blog));


            if (userService.access(user, blog).contains(ModelAccess.DELETE)){
                blogService.deleteById(id);
                LOGGER.info(String.format("Sender {%s} successfully deleted blog {%s} ", user, blog));
                context.status(Constants.NO_CONTENT_204);
            }
            else{
                LOGGER.info(String.format("Sender {%s} is not authorized to delete blog {%s}. Throwing Forbidden", user, blog));
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
            Blog blog = blogService.findById(id);
            LOGGER.info(String.format("Sender {%s} started to update blog {%s} ", user, blog));

            if (userService.access(user, blog).contains(ModelAccess.UPDATE)){
                Blog updated = mapperFactory.objectMapper(ModelAccess.UPDATE).readValue(context.body(), Blog.class);
                updated.setId(id);
                blogService.update(updated);

                LOGGER.info(String.format("Sender {%s} successfully updated blog {%s} ", user, blog));
                context.result(mapperFactory.objectMapper(ModelAccess.UPDATE).writeValueAsString(updated));
            }
            else{
                LOGGER.info(String.format("Sender {%s} is not authorized to update blog {%s}. Throwing Forbidden", user, blog));
                throw new ForbiddenResponse();
            }
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
            throw new BadRequestResponse();
        }
    }

    @Override
    public void getAll(Context context) {
        User user = senderOrThrowUnauthorized(context);
        LOGGER.info(String.format("Sender {%s} started to getAll blogs", user));
        try {
            List<Blog> blogs = blogService.all()
                    .stream()
                    .filter(blog -> userService.access(user, blog).contains(ModelAccess.READ))
                    .collect(Collectors.toList());
            LOGGER.info(String.format("Sender {%s} successfully gotAll blogs", user));
            context.result(mapperFactory.objectMapper(ModelAccess.READ).writeValueAsString(blogs));
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
            throw new InternalServerErrorResponse();
        }
    }

    @Override
    public void getOne(Context context, int id) {
        try {
            User user = senderOrThrowUnauthorized(context);
            Blog blog = blogService.findById(id);
            LOGGER.info(String.format("Sender {%s} started to getOne blog {%s} ", user, blog));
            if(userService.access(user, blog).contains(ModelAccess.READ)){
                LOGGER.info(String.format("Sender {%s} successfully gotOne blog {%s} ", user, blog));
                context.result(mapperFactory.objectMapper(ModelAccess.READ).writeValueAsString(blog));
            }
            else{
                LOGGER.info(String.format("Sender {%s} is not authorized to getOne blog {%s}. Throwing Forbidden", user, blog));
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
