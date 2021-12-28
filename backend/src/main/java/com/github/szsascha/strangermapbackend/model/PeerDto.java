package com.github.szsascha.strangermapbackend.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PeerDto {

    private String name;

    private String description;

    private double lat;

    private double lon;

}
