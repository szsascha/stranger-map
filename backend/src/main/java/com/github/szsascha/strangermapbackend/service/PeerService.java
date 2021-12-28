package com.github.szsascha.strangermapbackend.service;

import com.github.szsascha.strangermapbackend.model.PeersDto;

import java.util.UUID;

public interface PeerService {

    void cleanup();

    UUID register(String name, String description);

    void updatePosition(UUID uuid, double lat, double lon);

    PeersDto getNearbyPeers(UUID uuid);

}
