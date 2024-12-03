package soa.lab3.organization.ejb;

import jakarta.ejb.Stateless;
import soa.lab3.organization.model.Address;
import soa.lab3.organization.model.Organization;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Stateless
public class OrganizationServiceBean implements OrganizationService {
    private final Map<Long, Organization> organizations = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Organization getOrganization(Long id) {
        return organizations.get(id);
    }

    @Override
    public Organization createOrganization(Organization organization) {
        organization.setId(idGenerator.getAndIncrement());
        organizations.put(organization.getId(), organization);
        return organization;
    }

    @Override
    public Organization updateOrganization(Long id, Organization updatedOrganization) {
        Organization existingOrg = organizations.get(id);
        if (existingOrg != null) {
            updatedOrganization.setId(id);
            updatedOrganization.setCreationDate(existingOrg.getCreationDate());
            organizations.put(id, updatedOrganization);
        }
        return updatedOrganization;
    }

    @Override
    public boolean deleteOrganization(Long id) {
        return organizations.remove(id) != null;
    }

    @Override
    public List<Organization> getFilteredOrganizations(String creationDate, Integer annualTurnover, String sort) {
        Stream<Organization> filteredStream = organizations.values().stream();

        if (creationDate != null) {
            try {
                Date filterDate = Date.from(LocalDate.parse(creationDate).atStartOfDay(ZoneId.systemDefault()).toInstant());
                filteredStream = filteredStream.filter(org -> org.getCreationDate().after(filterDate));
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid creationDate format. Expected format: yyyy-MM-dd");
            }
        }

        if (annualTurnover != null) {
            filteredStream = filteredStream.filter(org -> org.getAnnualTurnover() < annualTurnover);
        }

        if (sort != null) {
            String[] sortParams = sort.split(",");
            if (sortParams.length == 2) {
                String field = sortParams[0];
                boolean ascending = "asc".equalsIgnoreCase(sortParams[1]);

                Comparator<Organization> comparator = getComparator(field);
                filteredStream = filteredStream.sorted(applySortOrder(comparator, ascending));
            } else {
                throw new IllegalArgumentException("Invalid sort parameter. Expected format: field,asc|desc");
            }
        }

        return filteredStream.toList();
    }

    // Метод для получения компаратора на основе поля
    private Comparator<Organization> getComparator(String field) {
        return switch (field) {
            case "name" -> Comparator.comparing(Organization::getName);
            case "creationDate" -> Comparator.comparing(Organization::getCreationDate);
            case "annualTurnover" -> Comparator.comparing(Organization::getAnnualTurnover, Comparator.nullsLast(Comparator.naturalOrder()));
            default -> throw new IllegalArgumentException("Unsupported sort field: " + field);
        };
    }

    // Метод для применения порядка сортировки
    private Comparator<Organization> applySortOrder(Comparator<Organization> comparator, boolean ascending) {
        return ascending ? comparator : comparator.reversed();
    }

    @Override
    public long countByEmployeesCount(Long count) {
        return organizations.values().stream()
                .filter(org -> org.getEmployeesCount() != null && org.getEmployeesCount() > count)
                .count();
    }

    @Override
    public List<Organization> searchByFullName(String substring) {
        return organizations.values().stream()
                .filter(org -> org.getFullName() != null && org.getFullName().contains(substring))
                .collect(Collectors.toList());
    }

    @Override
    public Map<Address, Long> groupByOfficialAddress() {
        return organizations.values().stream()
                .collect(Collectors.groupingBy(Organization::getOfficialAddress, Collectors.counting()));
    }
}
