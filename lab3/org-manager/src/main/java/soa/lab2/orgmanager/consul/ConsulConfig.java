package soa.lab2.orgmanager.consul;

import com.orbitz.consul.Consul;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsulConfig {

    @Bean
    public Consul consulClient() {
        return Consul.builder()
                .withUrl("http://localhost:8500")
                .build();
    }
}
