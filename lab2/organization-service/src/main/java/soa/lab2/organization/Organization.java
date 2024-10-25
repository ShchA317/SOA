package soa.lab2.organization;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

@Getter
@Setter
public class Organization {

    @NotNull(message = "Organization cannot be null")
    private long id;

    @NotNull(message = "Full name cannot be null")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @NotNull(message = "Official address cannot be null")
    @Size(min = 5, max = 200, message = "Address must be between 5 and 200 characters")
    private String officialAddress;

    @Min(value = 1, message = "Employees count must be greater than or equal to 1")
    private int employeesCount;

    @Min(value = 1, message = "Annual turnover must be greater than or equal to 1")
    private int annualTurnover;

    @NotNull(message = "Creation date cannot be null")
    @PastOrPresent(message = "Creation date must be in the past or present")
    private String creationDate;
}
