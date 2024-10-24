package soa.lab2.orgmanager.service;

import com.example.model.Organization;
import com.example.model.OrgmanagerHireIdPostRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import soa.lab2.orgmanager.client.OrganizationsFeignClient;

import java.util.NoSuchElementException;

@Service

public class OrgManagerService {
    private final OrganizationsFeignClient organizationsFeignClient;

    private static final Logger logger = LoggerFactory.getLogger(OrgManagerService.class);


    public OrgManagerService(OrganizationsFeignClient organizationsFeignClient) {
        this.organizationsFeignClient = organizationsFeignClient;
    }

    public void mergeOrganizations(Integer id1, Integer id2, String newName, String newAddress) {
        // TODO: сделать логику поглащения
    }

    // TODO: решить вопросики с валидацией
    public void hireEmployee(Integer id, OrgmanagerHireIdPostRequest hireRequest) {
        Organization organization = organizationsFeignClient.getOrganizationById(id).getBody();
        if (organization != null) {
            organization.setEmployeesCount(organization.getEmployeesCount() + 1);
        } else {
            throw new NoSuchElementException("организация не найдена");
        }
        if(organizationsFeignClient.updateOrganizationById(id, organization)
                .getStatusCode()
                .is2xxSuccessful()){
            logger.info("{} теперь {} в компании {}",
                    hireRequest.getEmployeeName(),
                    hireRequest.getPosition(),
                    organization.getName()
            );
        } else {
            throw new RuntimeException("не получилось, не фортануло");
        }
    }
}
