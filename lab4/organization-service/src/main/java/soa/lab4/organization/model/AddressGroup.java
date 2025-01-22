package soa.lab4.organization.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(name = "officialAddressGroup", namespace = "http://organization.lab4.soa/")
@XmlType(propOrder = {"address", "count"}, namespace = "http://organization.lab4.soa/")
public class AddressGroup {

    private Address address;
    private Long count;

    public AddressGroup() {
    }

    public AddressGroup(Address address, Long count) {
        this.address = address;
        this.count = count;
    }

    @XmlElement(namespace = "http://organization.lab4.soa/")
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @XmlElement(namespace = "http://organization.lab4.soa/")
    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
