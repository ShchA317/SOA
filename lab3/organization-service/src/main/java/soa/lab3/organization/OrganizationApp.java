package soa.lab3.organization;


import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")  // Определяем корневой путь для всех RESTful сервисов
public class OrganizationApp extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(OrganizationResource.class);  // Регистрируем наш ресурсный класс
        return resources;
    }
}

