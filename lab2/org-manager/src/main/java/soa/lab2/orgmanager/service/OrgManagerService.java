package soa.lab2.orgmanager.service;

import com.example.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import soa.lab2.orgmanager.client.OrganizationsFeignClient;

import java.time.Instant;
import java.util.Date;
import java.util.NoSuchElementException;

@Service
public class OrgManagerService {
    private final OrganizationsFeignClient organizationsFeignClient;
    private static final Logger logger = LoggerFactory.getLogger(OrgManagerService.class);

    public OrgManagerService(OrganizationsFeignClient organizationsFeignClient) {
        this.organizationsFeignClient = organizationsFeignClient;
    }

    // Опасный метод, тут бы транзакционность
    public int mergeOrganizations(Integer id1, Integer id2, String newName, String newAddress) {
        Organization first = organizationsFeignClient.getOrganizationById(id1).getBody();
        Organization second = organizationsFeignClient.getOrganizationById(id2).getBody();

        if(first == null || second == null) throw new NoSuchElementException("организация не найдена");

        Organization newOrganization = new Organization()
                .name(newName)
                .fullName(newName)
                .coordinates(new Coordinates().x(first.getCoordinates().getX()).y(first.getCoordinates().getY()))
                .officialAddress(new Address().zipCode(newAddress))
                .employeesCount(first.getEmployeesCount() + second.getEmployeesCount())
                .annualTurnover(first.getAnnualTurnover() + second.getAnnualTurnover())
                .type(OrganizationType.PUBLIC) // ну такая бизнес-логика пусть будет. Сомнительно, но окей
                .creationDate(Date.from(Instant.now()));

        var response = organizationsFeignClient
                .createOrganization(newOrganization);

        if(!response.getStatusCode()
                .is2xxSuccessful()) throw new RuntimeException("Не смогли добавить организацию");

        organizationsFeignClient.deleteOrganizationById(id1);
        organizationsFeignClient.deleteOrganizationById(id2);

//        return response.getBody().getId();
        return second.getId();
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
