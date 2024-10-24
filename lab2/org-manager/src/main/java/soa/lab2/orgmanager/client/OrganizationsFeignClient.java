package soa.lab2.orgmanager.client;

import com.example.api.OrganizationsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "organizationsClient", url = "${api.url}/organizations")
public interface OrganizationsFeignClient extends OrganizationsApi {

}