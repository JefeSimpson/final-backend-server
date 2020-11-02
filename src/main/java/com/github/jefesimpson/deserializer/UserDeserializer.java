package com.github.jefesimpson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.jefesimpson.model.User;
import com.github.jefesimpson.model.UserRole;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.time.LocalDate;

public class UserDeserializer extends StdDeserializer<User> {
    public UserDeserializer() {
        super(User.class);
    }

    @Override
    public User deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
        String login = jsonNode.get(User.COLUMN_LOGIN).asText();
        String password = jsonNode.get(User.COLUMN_PASSWORD).asText();
        UserRole userRole = UserRole.valueOf(jsonNode.get(User.COLUMN_USER_ROLE).asText().toUpperCase());
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
        return new User(0, login, passwordHash, LocalDate.now(), userRole);
    }
}
