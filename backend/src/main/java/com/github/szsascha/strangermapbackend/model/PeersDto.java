package com.github.szsascha.strangermapbackend.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PeersDto {

    private List<Peer> peers;

}
