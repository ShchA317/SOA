package soa.lab2.orgmanager.consul;

import com.orbitz.consul.Consul;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsulSync {
    @Autowired
    private Consul consul;

    @Autowired
    private EurekaRegistrar registrar;


    public void syncServices() {
        var response = consul.healthClient().getHealthyServiceInstances("organization-service").getResponse();
        var instance = response.get(0);


        registrar.registerInstance(
                "http://localhost:8761",
                instance.getService().getAddress(),
                8080
        );
    }
}
