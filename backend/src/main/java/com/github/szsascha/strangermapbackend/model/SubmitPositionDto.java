package com.github.szsascha.strangermapbackend.model;

import lombok.Data;

import java.util.UUID;

@Data
public class SubmitPositionDto {

    private UUID uuid;

    private double lat;

    private double lon;

}
