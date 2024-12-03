package soa.lab2.orgmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OrgManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrgManagerApplication.class, args);
	}

}
