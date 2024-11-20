package soa.lab2.orgmanager;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Указываем путь для CORS
                .allowedOrigins("http://localhost:3000") // Разрешаем источник
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Разрешённые методы
                .allowedHeaders("*") // Разрешённые заголовки
                .allowCredentials(true); // Разрешаем отправку cookies
    }
}
