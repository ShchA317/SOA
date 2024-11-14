package soa.lab2.organization;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Coordinates {

    @NotNull
    @Max(76)
    private Integer x;

    @NotNull
    @Min(-154)
    private Integer y;
}
