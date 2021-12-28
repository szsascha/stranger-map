package com.github.szsascha.strangermapbackend.service;

import com.github.szsascha.strangermapbackend.model.Peer;
import com.github.szsascha.strangermapbackend.model.PeersDto;
import com.github.szsascha.strangermapbackend.util.Location;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PeerServiceImpl implements PeerService {

    private final double RADIUS_KM = 2;

    private final Map<UUID, Peer> peers = new HashMap<>();

    @Override
    public UUID register(String name, String description) {
        final UUID uuid = UUID.randomUUID();
        if (peers.containsKey(uuid)) {
            throw new IllegalStateException("UUID already exists");
        }

        this.peers.put(
            uuid,
            Peer.builder()
                .name(name)
                .description(description)
                .build()
        );

        return uuid;
    }

    @Override
    public void updatePosition(UUID uuid, double lat, double lon) {
        Peer peer = peers.get(uuid);

        if (peer == null) {
            throw new IllegalStateException("UUID doesn't exist");
        }

        peer.setLat(lat);
        peer.setLon(lon);
    }

    @Override
    public PeersDto getNearbyPeers(UUID uuid) {
        final Peer ownPeer = peers.get(uuid);

        if (ownPeer == null) {
            throw new IllegalStateException("UUID doesn't exist");
        }

        return PeersDto.builder()
                .peers(
                        peers.entrySet().stream()
                            .filter(entry -> !entry.getKey().equals(uuid)) // Don't send own position
                            .filter(entry ->
                                    Location.distanceBetweenTwoLocationsInKm(
                                            ownPeer.getLat(),
                                            ownPeer.getLon(),
                                            entry.getValue().getLat(),
                                            entry.getValue().getLon()
                                    ) <= RADIUS_KM // Peer has to be in radius
                            )
                            .map(Map.Entry::getValue)
                            .collect(Collectors.toList())
                )
                .build();
    }

}
