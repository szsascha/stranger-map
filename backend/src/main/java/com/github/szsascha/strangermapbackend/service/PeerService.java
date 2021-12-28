package com.github.szsascha.strangermapbackend.service;

import com.github.szsascha.strangermapbackend.model.Peer;

import java.util.List;
import java.util.UUID;

public interface PeerService {

    void cleanup();

    UUID register(String name, String description);

    void updatePosition(UUID uuid, double lat, double lon);

    List<Peer> getNearbyPeers(UUID uuid);

}
