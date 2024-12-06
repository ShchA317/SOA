package soa.lab2.orgmanager.client;

import com.example.api.OrganizationsApi;
import com.example.model.Organization;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = "organization-service", url = "http://localhost:8080", path = "/org-service/api")
public interface OrganizationsFeignClient extends OrganizationsApi {

    @Override
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/organizations/{id}",
            produces = { "application/json" }
    )
    ResponseEntity<Organization> getOrganizationById(
            @Parameter(name = "id", description = "Уникальный идентификатор организации", required = true, in = ParameterIn.PATH) @PathVariable("id") Integer id
    );

    @Override
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/organizations",
            produces = { "application/json" },
            consumes = { "application/json" }
    )
    ResponseEntity<Organization> createOrganization(
            @Parameter(name = "Organization", description = "Объект новой организации", required = true) @Valid @RequestBody Organization organization
    );

    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/organizations/{id}"
    )
    ResponseEntity<Void> deleteOrganizationById(
            @Parameter(name = "id", description = "Уникальный идентификатор организации", required = true, in = ParameterIn.PATH) @PathVariable("id") Integer id
    );

    @RequestMapping(
            method = RequestMethod.PUT,
            value = "/organizations/{id}",
            produces = { "application/json" },
            consumes = { "application/json" }
    )
    ResponseEntity<Organization> updateOrganizationById(
            @Parameter(name = "id", description = "Уникальный идентификатор организации", required = true, in = ParameterIn.PATH) @PathVariable("id") Integer id,
            @Parameter(name = "Organization", description = "Обновленные данные организации", required = true) @Valid @RequestBody Organization organization
    );
}