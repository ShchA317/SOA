package soa.lab4.organization.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "officialAddress", namespace = "http://organization.lab4.soa/")
@XmlType(namespace = "http://organization.lab4.soa/")
@Setter
public class Address {

    private String zipCode;

    public Address() {
    }
    public Address(String zipCode) {
        this.zipCode = zipCode;
    }

    @XmlElement(namespace = "http://organization.lab4.soa/")
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
