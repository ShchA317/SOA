package soa.lab2.organization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

    private final ObjectMapper mapper;

    public ObjectMapperContextResolver() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Регистрируем модуль для работы с Java 8 датами
        mapper.findAndRegisterModules(); // Автоматически находит и регистрирует модули Jackson
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
}

