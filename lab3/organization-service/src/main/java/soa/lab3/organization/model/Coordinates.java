package soa.lab3.organization.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
public class Coordinates implements Serializable {

    @NotNull
    @Max(76)
    private Integer x;

    @NotNull
    @Min(-154)
    private Integer y;
}
