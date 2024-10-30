package soa.lab2.organization;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class Coordinates {

    @NotNull
    @Max(76)
    private Integer x;

    @NotNull
    @Min(-154)
    private Integer y;
}
