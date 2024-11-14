package soa.lab2.organization;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
public class Organization {

    @NotNull
    @Positive
    private Long id;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    private Coordinates coordinates;

    @NotNull
    private Date creationDate;

    @Positive
    private int annualTurnover;

    @NotNull
    @NotEmpty
    @Size(max = 1678)
    private String fullName;

    @NotNull
    @Positive
    private Long employeesCount;

    @NotNull
    private OrganizationType type;

    @NotNull
    private Address officialAddress;
}
