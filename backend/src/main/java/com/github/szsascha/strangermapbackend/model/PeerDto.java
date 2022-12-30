package com.github.szsascha.strangermapbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PeerDto {

    private String name;

    private String description;

    private double lat;

    private double lon;

}
