package soa.lab4.organization;

import javax.jws.WebService;

import soa.lab4.organization.model.AddressGroup;
import soa.lab4.organization.model.Organization;

import java.util.List;

@WebService(serviceName = "OrganizationService", portName = "OrganizationServicePort")
public interface OrganizationService {
    Organization getOrganization(Long id);
    Organization createOrganization(Organization organization);
    Organization updateOrganization(Long id, Organization updatedOrganization);
    boolean deleteOrganization(Long id);
    List<Organization> getFilteredOrganizations(String creationDate, Integer annualTurnover, String sort);
    long countByEmployeesCount(Long count);
    List<Organization> searchByFullName(String substring);

    List<AddressGroup> groupByOfficialAddress();
}
