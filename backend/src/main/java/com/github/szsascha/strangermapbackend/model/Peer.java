package com.github.szsascha.strangermapbackend.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Peer {

    private String name;

    private String description;

    // Don't use it in Redis since position is stored separately
    private transient double lat;

    // Don't use it in Redis since position is stored separately
    private transient double lon;

    private long lastActivity;

}
