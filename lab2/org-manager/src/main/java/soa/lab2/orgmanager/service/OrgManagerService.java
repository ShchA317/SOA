package soa.lab2.orgmanager.service;

import com.example.model.Address;
import com.example.model.Organization;
import com.example.model.OrgmanagerHireIdPostRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import soa.lab2.orgmanager.client.OrganizationsFeignClient;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrgManagerService {
    private final OrganizationsFeignClient organizationsFeignClient;

    private final static AtomicInteger lastOrgId = new AtomicInteger(0);

    private static final Logger logger = LoggerFactory.getLogger(OrgManagerService.class);


    public OrgManagerService(OrganizationsFeignClient organizationsFeignClient) {
        this.organizationsFeignClient = organizationsFeignClient;
    }

    public int mergeOrganizations(Integer id1, Integer id2, String newName, String newAddress) {
        Organization first = organizationsFeignClient.getOrganizationById(id1).getBody();
        Organization second = organizationsFeignClient.getOrganizationById(id2).getBody();

        int id = lastOrgId.incrementAndGet();
        if(first == null || second == null) throw new NoSuchElementException("организация не найдена");

        Organization newOrganization = new Organization()
                .id(id)
                .fullName(newName)
                .officialAddress(new Address().zipCode(newAddress))
                .employeesCount(first.getEmployeesCount() + second.getEmployeesCount())
                .annualTurnover(first.getAnnualTurnover() + second.getAnnualTurnover())
                .creationDate(LocalDate.now());

        if(organizationsFeignClient
                .createOrganization(newOrganization)
                .getStatusCode()
                .is2xxSuccessful()) throw new RuntimeException("Не смогли добавить организацию");

        return id;
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
