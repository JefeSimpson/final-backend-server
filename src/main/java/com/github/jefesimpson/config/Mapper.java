package com.github.jefesimpson.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jefesimpson.model.ModelAccess;

public interface Mapper {
    ObjectMapper objectMapper(ModelAccess modelAccess);
}
