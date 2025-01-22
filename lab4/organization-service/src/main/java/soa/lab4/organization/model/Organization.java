package soa.lab4.organization.model;

import lombok.ToString;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

@ToString
@XmlRootElement(name = "organization", namespace = "http://organization.lab4.soa/")
@XmlType(propOrder = {"id", "name", "fullName", "annualTurnover", "employeesCount", "coordinates", "creationDate", "officialAddress", "orgType"})
public class Organization {

    private Long id;
    private String name;
    private String fullName;
    private Integer annualTurnover;
    private Integer employeesCount;
    private Date creationDate;
    private Coordinates coordinates;
    private Address officialAddress;
    private String  orgType;

    public Organization() {}

    public Organization(Long id, String name, String fullName, Integer annualTurnover, Integer employeesCount, Date creationDate, Coordinates coordinates, Address officialAddress, String  orgType) {
        this.id = id;
        this.name = name;
        this.fullName = fullName;
        this.annualTurnover = annualTurnover;
        this.employeesCount = employeesCount;
        this.creationDate = creationDate;
        this.coordinates = coordinates;
        this.officialAddress = officialAddress;
        this.orgType = orgType;
    }

    @XmlElement(namespace = "http://organization.lab4.soa/")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlElement(namespace = "http://organization.lab4.soa/")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(namespace = "http://organization.lab4.soa/")
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @XmlElement(namespace = "http://organization.lab4.soa/")
    public Integer getAnnualTurnover() {
        return annualTurnover;
    }

    public void setAnnualTurnover(Integer annualTurnover) {
        this.annualTurnover = annualTurnover;
    }

    @XmlElement(namespace = "http://organization.lab4.soa/")
    public Integer getEmployeesCount() {
        return employeesCount;
    }

    public void setEmployeesCount(Integer employeesCount) {
        this.employeesCount = employeesCount;
    }

    @XmlElement(namespace = "http://organization.lab4.soa/")
    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @XmlElement(namespace = "http://organization.lab4.soa/")
    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @XmlElement(namespace = "http://organization.lab4.soa/")
    public Address getOfficialAddress() {
        return officialAddress;
    }

    public void setOfficialAddress(Address officialAddress) {
        this.officialAddress = officialAddress;
    }

    @XmlElement(namespace = "http://organization.lab4.soa/")
    public String  getOrgType() {
        return orgType;
    }

    public void setOrgType(String  orgType) {
        this.orgType = orgType;
    }
}
