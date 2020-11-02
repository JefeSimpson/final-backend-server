package com.github.jefesimpson.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jefesimpson.model.ModelAccess;
import com.fasterxml.jackson.databind.Module;
import java.util.Map;

public class ModelAccessMapper implements Mapper {
    private Map<ModelAccess, Module> access;

    public ModelAccessMapper() {
    }

    public ModelAccessMapper(Map<ModelAccess, Module> access) {
        this.access = access;
    }

    public Map<ModelAccess, Module> getAccess() {
        return access;
    }

    public void setAccess(Map<ModelAccess, Module> access) {
        this.access = access;
    }

    @Override
    public ObjectMapper objectMapper(ModelAccess modelAccess) {
        ObjectMapper mapper = new ObjectMapper();
        Module module = access.get(modelAccess);
        mapper.registerModule(module);
        return mapper;
    }
}
