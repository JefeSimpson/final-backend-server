package com.github.jefesimpson;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.jefesimpson.config.ModelAccessMapper;
import com.github.jefesimpson.controller.BlogController;
import com.github.jefesimpson.controller.Controller;
import com.github.jefesimpson.controller.UserController;
import com.github.jefesimpson.deserializer.BlogDeserializer;
import com.github.jefesimpson.deserializer.UserDeserializer;
import com.github.jefesimpson.model.Blog;
import com.github.jefesimpson.model.ModelAccess;
import com.github.jefesimpson.model.User;
import com.github.jefesimpson.model.UserRole;
import com.github.jefesimpson.serializer.BlogSerializer;
import com.github.jefesimpson.serializer.UserSerializer;
import com.github.jefesimpson.service.BlogService;
import com.github.jefesimpson.service.Service;
import com.github.jefesimpson.service.UserService;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;

import java.util.*;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create();


        SimpleModule module = new SimpleModule();
        module.addDeserializer(User.class, new UserDeserializer())
                .addDeserializer(Blog.class, new BlogDeserializer())
                .addSerializer(User.class, new UserSerializer())
                .addSerializer(Blog.class, new BlogSerializer());

        Map<ModelAccess, Module> access = new HashMap<>();
        access.put(ModelAccess.CREATE, module);
        access.put(ModelAccess.READ, module);
        access.put(ModelAccess.UPDATE, module);
        ModelAccessMapper modelAccessMapper = new ModelAccessMapper(access);


        UserService userService = new UserService();
        Service<Blog> blogService = new BlogService();
        Controller<User> userController = new UserController(userService, modelAccessMapper);
        Controller<Blog> blogController = new BlogController(userService, modelAccessMapper, blogService);


        app.routes(() -> {
            path("users", () -> {
                get(userController::getAll);
                post(userController::create);
                path(":id", () -> {
                    get(ctx -> userController.getOne(ctx,ctx.pathParam(":id", Integer.class).get()));
                    patch(ctx -> userController.update(ctx, ctx.pathParam(":id", Integer.class).get()));
                    delete(ctx -> userController.delete(ctx, ctx.pathParam(":id", Integer.class).get()));
                });
            });
            path("blogs", () -> {
                get(blogController::getAll);
                post(blogController::create);
                path(":id", () -> {
                    get(ctx -> blogController.getOne(ctx, ctx.pathParam(":id", Integer.class).get()));
                    patch(ctx -> blogController.update(ctx, ctx.pathParam(":id", Integer.class).get()));
                    delete(ctx -> blogController.delete(ctx, ctx.pathParam(":id", Integer.class).get()));
                });
            });
        });


        app.start(9950);
    }
}
