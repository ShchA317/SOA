package soa.lab3.organization.ejb;

import jakarta.ejb.Remote;
import soa.lab3.organization.model.Address;
import soa.lab3.organization.model.Organization;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Remote
public interface OrganizationService extends Serializable {
    Organization getOrganization(Long id);
    Organization createOrganization(Organization organization);
    Organization updateOrganization(Long id, Organization updatedOrganization);
    boolean deleteOrganization(Long id);
    List<Organization> getFilteredOrganizations(String creationDate, Integer annualTurnover, String sort);
    long countByEmployeesCount(Long count);
    List<Organization> searchByFullName(String substring);
    Map<Address, Long> groupByOfficialAddress();
}
