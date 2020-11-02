package com.github.jefesimpson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.jefesimpson.model.Blog;

import java.io.IOException;
import java.time.LocalDate;

public class BlogDeserializer extends StdDeserializer<Blog> {
    public BlogDeserializer() {
        super(Blog.class);
    }

    @Override
    public Blog deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
        String head = jsonNode.get(Blog.COLUMN_HEAD).asText();
        String text = jsonNode.get(Blog.COLUMN_TEXT).asText();
        Boolean vipAccess = jsonNode.get(Blog.COLUMN_VIP_ACCESS).asBoolean();
        return new Blog(0, head, text, LocalDate.now(), vipAccess);
    }
}
