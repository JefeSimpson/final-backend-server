package com.github.jefesimpson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.jefesimpson.model.User;

import java.io.IOException;

public class UserSerializer extends StdSerializer<User> {
    public UserSerializer() {
        super(User.class);
    }

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField(User.COLUMN_ID, user.getId());
        jsonGenerator.writeStringField(User.COLUMN_LOGIN, user.getLogin());
        jsonGenerator.writeObjectField(User.COLUMN_CREATED_DATE, user.getCreatedDate());
        jsonGenerator.writeStringField(User.COLUMN_USER_ROLE, user.getUserRole().name());
        jsonGenerator.writeEndObject();
    }
}
