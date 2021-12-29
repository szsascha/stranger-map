package com.github.szsascha.strangermapbackend.model;

import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class SubmitPositionDto {

    @NotNull
    private UUID uuid;

    @DecimalMin("-90")
    @DecimalMax("90")
    private double lat;

    @DecimalMin("-180")
    @DecimalMax("180")
    private double lon;

}
