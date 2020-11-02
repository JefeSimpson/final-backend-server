package com.github.jefesimpson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.jefesimpson.model.Blog;

import java.io.IOException;

public class BlogSerializer extends StdSerializer<Blog> {
    public BlogSerializer() {
        super(Blog.class);
    }

    @Override
    public void serialize(Blog blog, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField(Blog.COLUMN_ID, blog.getId());
        jsonGenerator.writeStringField(Blog.COLUMN_HEAD, blog.getHead());
        jsonGenerator.writeStringField(Blog.COLUMN_TEXT, blog.getText());
        jsonGenerator.writeObjectField(Blog.COLUMN_CREATED_DATE, blog.getCreatedDate());
        jsonGenerator.writeBooleanField(Blog.COLUMN_VIP_ACCESS, blog.isVipAccess());
        jsonGenerator.writeEndObject();
    }
}
