package soa.lab2.orgmanager.consul;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EurekaRegistrar {

    private final RestTemplate restTemplate;

    public EurekaRegistrar() {
        this.restTemplate = new RestTemplate();
    }

    public void registerInstance(String eurekaServerUrl, String address, int port) {
        var instanceId = String.format("%s:organization-service:%d", address, port);
        var homePageUrl = String.format("http://%s:%d", address, port);

        String payload = String.format(
                """     
                           {
                             "instance": {
                               "instanceId": "%s",
                               "hostName": "%s",
                               "app": "ORGANIZATION-SERVICE",
                               "ipAddr": "%s",
                               "status": "UP",
                               "port": { "$": %d, "@enabled": true },
                               "vipAddress": "organization-service",
                               "secureVipAddress": "organization-service",
                               "dataCenterInfo": {
                                 "@class": "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo",
                                 "name": "MyOwn"
                               },
                               "homePageUrl": "%s",
                               "statusPageUrl": "%s/actuator/info",
                               "healthCheckUrl": "%s/actuator/health"
                             }
                           }
                        """,
                instanceId, address, address, port, homePageUrl, homePageUrl, homePageUrl
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                eurekaServerUrl + "/eureka/apps/organization-service",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Инстанс успешно зарегистрирован в Eureka!");
        } else {
            System.err.println("Ошибка при регистрации инстанса в Eureka: " + response.getStatusCode());
        }
    }
}
