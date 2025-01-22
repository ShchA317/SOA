package soa.lab4.organization.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "coordinates", namespace = "http://organization.lab4.soa/")
@XmlType(propOrder = {"x", "y"}, namespace = "http://organization.lab4.soa/")
@Setter
public class Coordinates {
    public Coordinates() {
    }
    @XmlElement(name = "x", namespace = "http://organization.lab4.soa/")
    private Integer x;
    @XmlElement(name = "y", namespace = "http://organization.lab4.soa/")
    private Integer y;

}
