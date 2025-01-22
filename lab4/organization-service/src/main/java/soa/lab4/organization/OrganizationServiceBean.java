package soa.lab4.organization;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.soap.SOAPBinding;
import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.soap.SOAPFaultException;

import soa.lab4.organization.model.Address;
import soa.lab4.organization.model.AddressGroup;
import soa.lab4.organization.model.Organization;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@WebService(
        targetNamespace = "http://organization.lab4.soa/",
        serviceName = "OrganizationServiceBeanService",
        portName = "OrganizationServiceBeanPort"
)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public class OrganizationServiceBean implements OrganizationService {

    private final Map<Long, Organization> organizations = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @WebMethod(operationName = "getOrganization")
    @WebResult(name = "organization")
    public Organization getOrganization(@WebParam(name = "id", targetNamespace = "http://organization.lab4.soa/") Long id) {
        Organization organization = organizations.get(id);
        if (organization == null) {
            throw createSOAPFaultException("OrganizationNotFoundException",
                    "Organization with id " + id + " not found",
                    "SOAP-ENV:Client");
        }
        return organization;
    }


    @WebMethod(operationName = "createOrganization")
    @WebResult(name = "organization")
    public Organization createOrganization(@WebParam(name = "organization", targetNamespace = "http://organization.lab4.soa/") Organization organization) {
        if (organization == null) {
            throw createSOAPFaultException("BadRequestException", "Received organization is null", "SOAP-ENV:Client");
        }
        organization.setId(idGenerator.getAndIncrement());
        organizations.put(organization.getId(), organization);
        return organization;
    }

    @WebMethod(operationName = "updateOrganization")
    @WebResult(name = "organization")
    public Organization updateOrganization(
            @WebParam(name = "id", targetNamespace = "http://organization.lab4.soa/") Long id,
            @WebParam(name = "updatedOrganization", targetNamespace = "http://organization.lab4.soa/") Organization updatedOrganization) {
        Organization existingOrg = organizations.get(id);
        if (existingOrg == null) {
            throw createSOAPFaultException("OrganizationNotFoundException",
                    "Organization with id " + id + " not found",
                    "SOAP-ENV:Client");
        }
        updatedOrganization.setId(id);
        updatedOrganization.setCreationDate(existingOrg.getCreationDate());
        organizations.put(id, updatedOrganization);
        return updatedOrganization;
    }

    @WebMethod(operationName = "deleteOrganization")
    @WebResult(name = "result")
    public boolean deleteOrganization(@WebParam(name = "id", targetNamespace = "http://organization.lab4.soa/") Long id) {
        Organization existingOrg = organizations.get(id);
        if (existingOrg == null) {
            throw createSOAPFaultException("OrganizationNotFoundException",
                    "Organization with id " + id + " not found",
                    "SOAP-ENV:Client");
        }
        Organization removedOrg = organizations.remove(id);
        return true;
    }

    @WebMethod(operationName = "getFilteredOrganizations")
    @WebResult(name = "organizations")
    public List<Organization> getFilteredOrganizations(
            @WebParam(name = "creationDate", targetNamespace = "http://organization.lab4.soa/") String creationDate,
            @WebParam(name = "annualTurnover", targetNamespace = "http://organization.lab4.soa/") Integer annualTurnover,
            @WebParam(name = "sort", targetNamespace = "http://organization.lab4.soa/") String sort) {
        Stream<Organization> filteredStream = organizations.values().stream();

        if (creationDate != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date filterDate = sdf.parse(creationDate);
                filteredStream = filteredStream.filter(org -> org.getCreationDate().after(filterDate));
            } catch (Exception e) {
                throw createSOAPFaultException("BadRequestException", "Invalid creationDate format. Expected format: yyyy-MM-dd", "SOAP-ENV:Client");
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
                if (comparator != null) {
                    filteredStream = filteredStream.sorted(applySortOrder(comparator, ascending));
                }
            } else {
                throw createSOAPFaultException("BadRequestException", "Invalid sort parameter. Expected format: field,asc|desc", "SOAP-ENV:Client");
            }
        }

        return filteredStream.collect(Collectors.toList());
    }

    @WebMethod(operationName = "countByEmployeesCount")
    @WebResult(name = "count")
    public long countByEmployeesCount(@WebParam(name = "count", targetNamespace = "http://organization.lab4.soa/") Long count) {
        return organizations.values().stream()
                .filter(org -> org.getEmployeesCount() != null && org.getEmployeesCount() > count)
                .count();
    }

    @WebMethod(operationName = "searchByFullName")
    @WebResult(name = "organizations")
    public List<Organization> searchByFullName(@WebParam(name = "substring", targetNamespace = "http://organization.lab4.soa/") String substring) {
        return organizations.values().stream()
                .filter(org -> org.getFullName() != null && org.getFullName().contains(substring))
                .collect(Collectors.toList());
    }

    private Comparator<Organization> getComparator(String field) {
        return switch (field) {
            case "name" -> Comparator.comparing(Organization::getName);
            case "annualTurnover" -> Comparator.comparingInt(Organization::getAnnualTurnover);
            case "employeesCount" -> Comparator.comparingInt(Organization::getEmployeesCount);
            default -> null;
        };
    }

    private <T> Comparator<T> applySortOrder(Comparator<T> comparator, boolean ascending) {
        return ascending ? comparator : comparator.reversed();
    }

    @WebMethod(operationName = "groupByAddress")
    @WebResult(name = "officialAddressGroup")
    public List<AddressGroup> groupByOfficialAddress() {
        return organizations.values().stream()
                .collect(Collectors.groupingBy(org -> org.getOfficialAddress().getZipCode(), Collectors.counting()))
                .entrySet().stream()
                .map(entry -> new AddressGroup(new Address(entry.getKey()), entry.getValue()))
                .collect(Collectors.toList());
    }

    private SOAPFaultException createSOAPFaultException(String faultCode, String faultString, String faultSubcode) {
        try {
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage();
            SOAPBody soapBody = soapMessage.getSOAPBody();

            // Создаем SOAPFault с кодом и сообщением
            SOAPFault soapFault = soapBody.addFault(new QName("http://organization.lab4.soa/", faultCode), faultString);
            soapFault.setFaultCode(faultSubcode);

            // Выбрасываем SOAPFaultException с этим SOAPFault
            return new SOAPFaultException(soapFault);
        } catch (SOAPException e) {
            throw new RuntimeException("Error creating SOAPFault", e);
        }
    }
}
